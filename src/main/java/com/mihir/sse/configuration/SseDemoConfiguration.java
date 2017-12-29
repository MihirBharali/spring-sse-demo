package com.mihir.sse.configuration;

import com.mihir.sse.processors.StudentDataManager;
import com.mihir.sse.repository.StudentRepository;
import com.mihir.sse.repository.StudentRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class SseDemoConfiguration {


    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
        jedisConFactory.setHostName("localhost");
        jedisConFactory.setPort(6379);
        return jedisConFactory;
    }

    @Bean
    public StudentDataManager redisService(){
        return new StudentDataManager();
    }


    @Bean
    public StudentRepository studentRepository() {
        return new StudentRepositoryImpl(redisTemplate());
    }
}
