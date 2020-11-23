package cn.edu.seu.alumni_background.component.redis;

import cn.edu.seu.alumni_background.validation.NotEmpty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;

import java.util.concurrent.TimeUnit;

@Validated
public interface StringToObjectRedisStringManager {

    RedisTemplate<String, Object> getTemplate();

    Object getValue(@NotEmpty String key);

    void setValue(@NotEmpty String key, @NotEmpty Object value);

    void setValueWithTTL(
        @NotEmpty String key, @NotEmpty Object value,
        @NotEmpty Long ttl, @NotEmpty TimeUnit timeUnit
    );

    // 线程安全
    Long incrementOrNew(
        @NotEmpty String key,
        @NotEmpty Long ttl,
        @NotEmpty TimeUnit timeUnit
    );
}
