package com.catchiz.enrollctrl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class EnrollctrlApplicationTests {

    @Test
    void contextLoads() {
        String a="test";
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String encode = encoder.encode(a);
        System.out.println(encode);
        String encode2 = encoder.encode(a);
        System.out.println(encode2);
        System.out.println(encoder.matches("test",encode2));
    }

}
