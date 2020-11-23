package cn.edu.seu.alumni_background.config.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Configuration
@PropertySource(value = "classpath:cache/redis-config.properties")
public class RedisCacheConfigurer {

    @Value("${redis.hostname}")
    private String hostname;

    @Value("${redis.port}")
    private String port;

    @Value("${redis.password}")
    private String password;

    // 首先配置连接池, 使用默认的
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxWaitMillis(100000L);  // 设置最长等待时间
//        jedisPoolConfig
        return jedisPoolConfig;
    }

    // 配置独立配置信息, 包括 host 等等的
    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration rsc = new RedisStandaloneConfiguration();
        rsc.setHostName(hostname);
        rsc.setPort(Integer.parseInt(port));
        if (!password.equals("")) {
            rsc.setPassword(password);
        }
        return rsc;
    }

    // 客户端配置
    @Bean
    public JedisClientConfiguration jedisClientConfiguration() {
        JedisClientConfiguration.JedisClientConfigurationBuilder builder =
            JedisClientConfiguration.builder();
        return builder.readTimeout(
            Duration.ofMinutes(3L)
        ).usePooling().poolConfig(jedisPoolConfig()).build();
    }

    // 工厂配置
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(
            redisStandaloneConfiguration(), jedisClientConfiguration()
        );
    }

    // Template 工具类配置
    @Bean
    public RedisTemplate myString2ObjectRedisTemplate() {
        RedisTemplate<String, Object> res =
            new RedisTemplate<>();
        GenericJackson2JsonRedisSerializer serializer =
            new GenericJackson2JsonRedisSerializer();
        // 设置序列化器
        res.setKeySerializer(new StringRedisSerializer());  // 修复无法显示 IP 地址字符串的错误
        res.setValueSerializer(serializer);
        res.setConnectionFactory(jedisConnectionFactory());
        return res;
    }

}
