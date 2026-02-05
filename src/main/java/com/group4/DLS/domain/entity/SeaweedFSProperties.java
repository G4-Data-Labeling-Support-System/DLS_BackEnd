package com.group4.DLS.domain.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "seaweedfs")
@Data
public class SeaweedFSProperties {

    private Master master;
    private Filer filer;

    @Data
    public static class Master {
        private String url;
    }

    @Data
    public static class Filer {
        private String url;
    }

}

