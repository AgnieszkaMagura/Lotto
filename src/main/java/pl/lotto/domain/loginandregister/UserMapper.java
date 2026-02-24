package pl.lotto.domain.loginandregister;


import pl.lotto.domain.loginandregister.dto.RegisterUserDto;
import pl.lotto.domain.loginandregister.dto.UserDto;

class UserMapper {
    static UserDto mapFromUserToUserDto(User user) {
        return UserDto.builder()
                .id(user.id())
                .username(user.username())
                .password(user.password())
                .build();
    }
    static User mapFromRegisterRequestToUser(RegisterUserDto registerUserDto) {
        return User.builder().username(registerUserDto.username()).password(registerUserDto.password()).build();
    }
}
