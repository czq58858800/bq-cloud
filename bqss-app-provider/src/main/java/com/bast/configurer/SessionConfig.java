package com.bast.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**共享Session
 * 保证分布式应用的可伸缩性
 * maxInactiveIntervalInSeconds: 设置Session失效时间
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400)
public class SessionConfig {
}