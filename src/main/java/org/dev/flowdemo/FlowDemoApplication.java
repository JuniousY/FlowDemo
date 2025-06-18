package org.dev.flowdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.dev.flowdemo.mapper")
public class FlowDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowDemoApplication.class, args);
    }

}
