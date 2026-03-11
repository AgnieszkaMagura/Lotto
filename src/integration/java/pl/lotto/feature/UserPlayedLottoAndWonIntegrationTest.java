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

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        wireMockServer.stubFor(WireMock.get("/api/v1.0/random?min=1&max=99&count=25")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody("[1, 2, 3, 4, 5, 6, 82, 82, 83, 83, 86, 57, 10, 81, 53, 93, 50, 54, 31, 88, 15, 43, 79, 32, 43]")
                ));

        // step 2: system fetched winning numbers for draw date: 14.03.2026 12:00
        // Zmieniamy na 14 marca, bo w IntegrationConfiguration mamy 11 marca (najbliższa sobota)
        LocalDateTime drawData = LocalDateTime.of(2026, 3, 14, 12, 0, 0);

        await()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                            try {
                                return !winningNumbersGeneratorFacade.retrieveWinningNumberByDate(drawData)
                                        .getWinningNumbers().isEmpty();
                            } catch (WinningNumbersNotFoundException e) {
                                return false;
                            }
                        }
                );

        // step 3: user tried to get JWT token (should fail because not registered)
        ResultActions failedLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        failedLoginRequest.andExpect(status().isUnauthorized());

        // step 4: user made POST /inputNumbers without token (should be 403)
        mockMvc.perform(post("/inputNumbers")
                .content("{\"inputNumbers\": [1, 2, 3, 4, 5, 6]}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());

        // step 5: register user
        ResultActions performRegister = mockMvc.perform(post("/register")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        performRegister.andExpect(status().isCreated());

        // step 6: login user (now should work)
        ResultActions successLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        MvcResult mvcResultToken = successLoginRequest.andExpect(status().isOk()).andReturn();
        String jsonTokenResponse = mvcResultToken.getResponse().getContentAsString();
        JwtResponseDto jwtResponse = objectMapper.readValue(jsonTokenResponse, JwtResponseDto.class);
        String token = jwtResponse.token();

        // step 7: post numbers with token
        ResultActions performPostInputNumbers = mockMvc.perform(post("/inputNumbers")
                .header("Authorization", "Bearer " + token)
                .content("{\"inputNumbers\": [1,2,3,4,5,6]}")
                .contentType(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = performPostInputNumbers.andExpect(status().isOk()).andReturn();
        NumberReceiverResponseDto numberReceiverResultDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), NumberReceiverResponseDto.class);
        String ticketId = numberReceiverResultDto.ticketDto().hash();

        // step 8: check draw date in ticket matches our expected drawData
        assertThat(numberReceiverResultDto.ticketDto().drawDate()).isEqualTo(drawData);

        // step 9: simulate time passing - 3 days and 55 minutes from Wed 11.03 10:00 gives Sat 14.03 10:55
        clock.plusDaysAndMinutes(3, 55);

        // step 10: system generates result
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

        // step 11: move clock to 12:01 (after the draw)
        clock.plusMinutes(66); // 10:55 + 66 min = 12:01

        // step 12: get results
        ResultActions performGetMethod = mockMvc.perform(get("/results/" + ticketId)
                .header("Authorization", "Bearer " + token));

        MvcResult mvcResultGetMethod = performGetMethod.andExpect(status().isOk()).andReturn();
        ResultAnnouncerResponseDto finalResult = objectMapper.readValue(mvcResultGetMethod.getResponse().getContentAsString(), ResultAnnouncerResponseDto.class);

        assertAll(
                () -> assertThat(finalResult.message()).isEqualTo("Congratulations, you won!"),
                () -> assertThat(finalResult.responseDto().hash()).isEqualTo(ticketId),
                () -> assertThat(finalResult.responseDto().hitNumbers()).hasSize(6)
        );
    }
}