package com.jone.record;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class RecordApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RecordApplication.class);
        app.setBannerMode (Banner.Mode.OFF);
        app.run (args);
    }
}
