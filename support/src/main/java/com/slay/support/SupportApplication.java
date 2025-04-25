package com.slay.support;

import com.slay.support.POJO.StorageProperties;
import com.slay.support.config.EnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@EnableCaching
@EnableJpaAuditing
@EnableFeignClients
public class SupportApplication {

    public static void main(String[] args) {
        EnvConfig.load();
        SpringApplication.run(SupportApplication.class, args);
    }

}
