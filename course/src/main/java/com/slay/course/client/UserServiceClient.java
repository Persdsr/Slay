package com.slay.course.client;

import com.slay.course.dto.response.user.UserLiteDTO;
import com.slay.course.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "user-service",
        url = "http://localhost:8080",
        configuration = FeignClientConfig.class,
        fallback = UserServiceClientFallback.class
)
@Primary
public interface UserServiceClient {

    @GetMapping("/api/user/lite")
    UserLiteDTO getUserLiteInfo(@RequestParam("userId") Integer userId);

    @GetMapping("/api/user/lite/batch")
    Map<Integer, UserLiteDTO> getUsersLiteBatch(@RequestParam("userIds") List<Integer> userIds);

}
