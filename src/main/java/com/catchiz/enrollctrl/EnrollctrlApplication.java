package com.catchiz.enrollctrl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.catchiz.enrollctrl.mapper")
@EnableTransactionManagement
@ConfigurationPropertiesScan
public class EnrollctrlApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnrollctrlApplication.class, args);
    }

}
