package com.depromeet.todo.builder;

import com.depromeet.todo.dto.api.UserApiResult;
import com.depromeet.todo.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserApiResultBuilder {

    public UserApiResult buildUserApiResult(User user) {

        UserApiResult result = new UserApiResult();

        result.setUserId(user.getId());
        result.setUsername(user.getUsername());

        return result;
    }
}
