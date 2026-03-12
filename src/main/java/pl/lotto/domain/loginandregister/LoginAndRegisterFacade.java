package pl.lotto.domain.loginandregister;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import pl.lotto.domain.loginandregister.dto.RegisterUserDto;
import pl.lotto.domain.loginandregister.dto.RegistrationResultDto;
import pl.lotto.domain.loginandregister.dto.UserDto;

@AllArgsConstructor
@Component
public class LoginAndRegisterFacade {


    private final LoginRepository repository;

    public UserDto findByUsername(String username) {
        return repository.findByUsername(username)
                .map(UserMapper::mapFromUserToUserDto)
                .orElseThrow(() -> new BadCredentialsException(username));
    }

    public RegistrationResultDto register(RegisterUserDto registerUserDto) {
        if (repository.existsByUsername(registerUserDto.username())) {
            throw new UserAlreadyExistsException("User with username " + registerUserDto.username() + " already exists");
        }
        final User user = User.builder()
                .username(registerUserDto.username())
                .password(registerUserDto.password())
                .build();
        User savedUser = repository.save(user);
        return new RegistrationResultDto(savedUser.id(), true, savedUser.username());
    }
}
