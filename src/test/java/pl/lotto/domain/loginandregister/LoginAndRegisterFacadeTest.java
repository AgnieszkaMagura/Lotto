package pl.lotto.domain.loginandregister;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import pl.lotto.domain.loginandregister.dto.RegisterUserDto;
import pl.lotto.domain.loginandregister.dto.RegistrationResultDto;
import pl.lotto.domain.loginandregister.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LoginAndRegisterFacadeTest {

    LoginAndRegisterFacade loginAndRegisterFacade = new LoginAndRegisterFacade(new InMemoryUserRepositoryTestImpl());
    @Test
    public void should_return_correct_register_result_when_user_registered() {
        //given
        String username = "John";
        String password = "JohnPassword";
        RegisterUserDto registerRequest = new RegisterUserDto(username, password);
        //when
        RegistrationResultDto registerResult = loginAndRegisterFacade.register(registerRequest);
        //then
        assertAll(
                () -> assertThat(registerResult.id()).isNotNull(),
                () -> assertThat(registerResult.username()).isEqualTo("John"),
                () -> assertThat(registerResult.created()).isTrue()
        );
    }

    @Test
    public void should_return_correct_user_from_database_when_found_by_username() {
        //given
        String username = "John";
        String password = "JohnPassword";
        RegisterUserDto registerRequest = new RegisterUserDto(username, password);
        loginAndRegisterFacade.register(registerRequest);
        //when
        UserDto userByUsername = loginAndRegisterFacade.findByUsername("John");
        //then
        assertAll(
                () -> assertThat(userByUsername.id()).isNotNull(),
                () -> assertThat(userByUsername.username()).isEqualTo("John"),
                () -> assertThat(userByUsername.password()).isEqualTo("JohnPassword")
        );
    }

    @Test
    public void should_throw_runtime_exception_with_username_not_found_message_if_user_not_found_in_database() {
        //given
        String username = "randomUsername";
        //when
        Throwable throwable = catchThrowable(() -> loginAndRegisterFacade.findByUsername(username));
        //then
        AssertionsForClassTypes.assertThat(throwable)
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("randomUsername");
    }
}
