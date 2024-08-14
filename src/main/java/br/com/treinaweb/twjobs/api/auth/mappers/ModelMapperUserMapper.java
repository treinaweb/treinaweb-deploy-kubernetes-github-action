package br.com.treinaweb.twjobs.api.auth.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.treinaweb.twjobs.api.auth.dtos.UserRequest;
import br.com.treinaweb.twjobs.api.auth.dtos.UserResponse;
import br.com.treinaweb.twjobs.core.models.User;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ModelMapperUserMapper implements UserMapper {

    private final ModelMapper modelMapper;

    @Override
    public UserResponse toUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public User toUser(UserRequest userRequest) {
        return modelMapper.map(userRequest, User.class);
    }
    
}
