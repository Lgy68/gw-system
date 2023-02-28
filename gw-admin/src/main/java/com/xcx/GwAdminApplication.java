package com.xcx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@MapperScan("com.xcx.system.mapper")
public class GwAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(GwAdminApplication.class, args);

        System.out.println("(♥◠‿◠)ﾉﾞ  应用启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "  __ _  ___  _ __   __ ___      _____ _ __  \n" +
                " / _` |/ _ \\| '_ \\ / _` \\ \\ /\\ / / _ \\ '_ \\ \n" +
                "| (_| | (_) | | | | (_| |\\ V  V /  __/ | | |\n" +
                " \\__, |\\___/|_| |_|\\__, | \\_/\\_/ \\___|_| |_|\n" +
                " |___/             |___/                    ");
    }

}
