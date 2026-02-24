package pl.lotto.domain.loginandregister;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginAndRegisterConfiguration {

    @Bean
    public LoginAndRegisterFacade loginAndRegisterFacade(LoginRepository loginRepository) {
        return new LoginAndRegisterFacade(loginRepository);
    }
}
