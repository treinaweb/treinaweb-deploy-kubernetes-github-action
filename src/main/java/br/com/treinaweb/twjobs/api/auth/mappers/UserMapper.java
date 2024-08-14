package br.com.treinaweb.twjobs.api.auth.mappers;

import br.com.treinaweb.twjobs.api.auth.dtos.UserRequest;
import br.com.treinaweb.twjobs.api.auth.dtos.UserResponse;
import br.com.treinaweb.twjobs.core.models.User;

public interface UserMapper {

    UserResponse toUserResponse(User user);
    User toUser(UserRequest userRequest);
    
}
