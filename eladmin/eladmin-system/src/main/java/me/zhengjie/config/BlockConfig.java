package me.zhengjie.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "block")
@Data
public class BlockConfig {

    private String url;
    private Integer port;
    private String username;
    private String password;
}
