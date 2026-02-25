package pl.lotto.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.lotto.BaseIntegrationTest;
import pl.lotto.domain.loginandregister.dto.RegistrationResultDto;
import pl.lotto.domain.numbergenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.numbergenerator.WinningNumbersNotFoundException;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lotto.domain.resultchecker.PlayerResultNotFoundException;
import pl.lotto.domain.resultchecker.ResultCheckerFacade;
import pl.lotto.domain.resultchecker.dto.PlayerDto;
import pl.lotto.infrastructure.loginandregister.controller.dto.JwtResponseDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserPlayedLottoAndWonIntegrationTest extends BaseIntegrationTest {

    @Autowired
    public WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;

    @Autowired
    public ResultCheckerFacade resultCheckerFacade;

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("lotto.number-generator.http.client.config.port", () -> wireMockServer.getPort());
        registry.add("lotto.number-generator.http.client.config.uri", () -> WIRE_MOCK_HOST);
    }

    @Test
    public void should_user_win_and_system_should_generate_winners() throws Exception {
        // step 1: external service returns 6 random numbers (1, 2, 3, 4, 5, 6)
        // given
        wireMockServer.stubFor(WireMock.get("/api/v1.0/random?min=1&max=99&count=25")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [1, 2, 3, 4, 5, 6, 82, 82, 83, 83, 86, 57, 10, 81, 53, 93, 50, 54, 31, 88, 15, 43, 79, 32, 43]
                                """.trim()
                        )));
        // step 2: system fetched winning numbers for draw date: 07.02.2026 12:00
        // given
        LocalDateTime drawDate = LocalDateTime.of(2026, 2, 7, 12, 0, 0);
        // when & then
        await()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                            try {
                                return !winningNumbersGeneratorFacade.retrieveWinningNumberByDate(drawDate)
                                        .getWinningNumbers().isEmpty();
                            } catch (WinningNumbersNotFoundException e) {
                                return false;
                            }
                        }
                );

        // step 3: user tried to get JWT token by requesting POST /token with username=someUser,
        // password=somePassword and system returned UNAUTHORIZED(401)
        // given & when
        ResultActions failedLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        failedLoginRequest
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("""
                        {
                          "message": "Bad Credentials",
                          "status": "UNAUTHORIZED"
                        }
                        """.trim()));

        // step 4: user made POST /inputNumbers with 6 numbers (1, 2, 3, 4, 5, 6) at 07-02-2026 10:00
        // and system returned FORBIDDEN(403)
        // given & when
        ResultActions performPostInputNumbersWithoutAuthorization = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                        "inputNumbers": [1, 2, 3, 4, 5, 6]
                        }
                        """.trim()
                ).contentType(MediaType.APPLICATION_JSON)
        );
        // then
        performPostInputNumbersWithoutAuthorization.andExpect(status().isForbidden());

        // step 5: user made POST /register with username=someUser, password=somePassword
        // and system registered user with status OK(200)
        // given && when
        ResultActions performRegister = mockMvc.perform(post("/register")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        // then
        MvcResult mvcResultRegistration = performRegister.andExpect(status().isCreated()).andReturn();
        String jsonResultRegistration = mvcResultRegistration.getResponse().getContentAsString();
        RegistrationResultDto registrationResultDto = objectMapper.readValue(
                jsonResultRegistration, RegistrationResultDto.class);
        assertAll(
                () -> assertThat(registrationResultDto.username()).isEqualTo("someUser"),
                () -> assertThat(registrationResultDto.created()).isTrue(),
                () -> assertThat(registrationResultDto.id()).isNotNull()
        );


        // step 6: user tried to get JWT token by requesting POST /token with username=someUser,
        // password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
        // given & when
        ResultActions successLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        MvcResult mvcResultToken = successLoginRequest.andExpect(status().isOk()).andReturn();
        String jsonTokenResponse = mvcResultToken.getResponse().getContentAsString();
        JwtResponseDto jwtResponse = objectMapper.readValue(jsonTokenResponse, JwtResponseDto.class);
        String token = jwtResponse.token();
        assertAll(
                () -> assertThat(jwtResponse.username()).isEqualTo("someUser"),
                () -> assertThat(token).matches(Pattern.compile("^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$"))
        );


        // step 7: user made POST /inputNumbers with 6 numbers (1, 2, 3, 4, 5, 6) at 04-02-2026 11:00
        // and system returned OK(200) with message: “success” and Ticket (DrawDate:07.02.2026 12:00 (Saturday),
        // TicketId: sampleTicketId)
        // given && when
        ResultActions performPostInputNumbers = mockMvc.perform(post("/inputNumbers")
                .header("Authorization", "Bearer " + token)
                .content("""
                        {
                        "inputNumbers": [1,2,3,4,5,6]
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON));
        // then
        MvcResult mvcResult = performPostInputNumbers.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        NumberReceiverResponseDto numberReceiverResultDto = objectMapper.readValue(json, NumberReceiverResponseDto.class);
        String ticketId = numberReceiverResultDto.ticketDto().hash();
        assertAll(
                () -> assertThat(numberReceiverResultDto.ticketDto().drawDate()).isEqualTo(drawDate),
                () -> assertThat(numberReceiverResultDto.message()).isEqualTo("SUCCESS"),
                () -> assertThat(ticketId).isNotNull()
        );

        // step 8: user made GET /results/notExistingId and system returned 404(NOT_FOUND) and body with
        // (message: Not found for id: notExistingId and status NOT_FOUND)
        // given & when
        ResultActions performGetResultsWithNotExistingId = mockMvc.perform(get("/results/notExistingId")
                .header("Authorization", "Bearer " + token));
        // then
        performGetResultsWithNotExistingId.andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                        "message": "notExistingId",
                        "status": "NOT_FOUND"
                        }
                        """.trim()
                ));

        // step 9: 3 days and 55 minutes passed, and it is 5 minute before draw (07.02.2026 11:55)
        // given && when && then
        clock.plusDaysAndMinutes(3, 55);


        // step 10: system generated result for TicketId: sampleTicketId with draw date 07.02.2026 12:00,
        // and saved it with 6 hits
        await().atMost(20, TimeUnit.SECONDS)
                .pollInterval(Duration.ofSeconds(1L))
                .until(() -> {
                            try {
                                PlayerDto result = resultCheckerFacade.findByTicketId(ticketId);
                                return !result.numbers().isEmpty();
                            } catch (PlayerResultNotFoundException exception) {
                                return false;
                            }
                        }
                );


        // step 11: 6 minutes passed, and it is 1 minute after the draw (07.02.2026 12:01)
        clock.plusMinutes(6);


        // step 12: ser made GET /results/sampleTicketId and system returned 200 (OK)
        // given && when
        ResultActions performGetMethod = mockMvc.perform(get("/results/" + ticketId)
                .header("Authorization", "Bearer " + token));
        // then
        MvcResult mvcResultGetMethod = performGetMethod.andExpect(status().isOk()).andReturn();
        String jsonGetMethod = mvcResultGetMethod.getResponse().getContentAsString();
        ResultAnnouncerResponseDto finalResult = objectMapper.readValue(jsonGetMethod, ResultAnnouncerResponseDto.class);
        assertAll(
                () -> assertThat(finalResult.message()).isEqualTo("Congratulations, you won!"),
                () -> assertThat(finalResult.responseDto().hash()).isEqualTo(ticketId),
                () -> assertThat(finalResult.responseDto().hitNumbers()).hasSize(6));
    }
}

