package com.blog.bloggy.common.config;

import java.time.Duration;

public class CacheExpireConfig {

    public static final Duration DEFAULT_CACHE_EXPIRE_TIME = Duration.ofMinutes(10L);

    public static final Duration POST_CACHE_EXPIRE_TIME = Duration.ofMinutes(5L);

}