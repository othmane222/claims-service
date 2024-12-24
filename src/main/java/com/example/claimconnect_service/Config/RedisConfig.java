package com.example.claimconnect_service.Config;

import com.example.claimconnect_service.Dto.ClaimDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, ClaimDTO> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, ClaimDTO> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // Set the value serializer
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
