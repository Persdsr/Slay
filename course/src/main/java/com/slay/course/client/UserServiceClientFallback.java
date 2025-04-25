package com.slay.course.client;

import com.slay.course.dto.response.user.UserLiteDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class UserServiceClientFallback implements UserServiceClient {

    @Override
    public UserLiteDTO getUserLiteInfo(Integer userId) {
        return null;
    }

    @Override
    public Map<Integer, UserLiteDTO> getUsersLiteBatch(List<Integer> userIds) {
        return Map.of();
    }
}
