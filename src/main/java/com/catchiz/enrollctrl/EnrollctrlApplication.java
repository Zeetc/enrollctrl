package com.catchiz.enrollctrl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.catchiz.enrollctrl.mapper")
@EnableTransactionManagement
public class EnrollctrlApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnrollctrlApplication.class, args);
    }

}
