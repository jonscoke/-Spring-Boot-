package com.youthhealth.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.youthhealth.modules.*.mapper")
public class MybatisConfig {
}
