package com.jone.record;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@SpringBootApplication
public class RecordApplication {
    private static final Logger loger = LoggerFactory.getLogger(RecordApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RecordApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
        //TODO 如果kbase数据库链接需要启动后就进行初始化的话可以在这里进行初始化，这样可以获取到配置文件中的配置信息
    }
}
