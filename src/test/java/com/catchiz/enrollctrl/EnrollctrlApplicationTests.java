package com.catchiz.enrollctrl;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class EnrollctrlApplicationTests {

    @Test
    void contextLoads() {
        List<String> abs=new ArrayList<>();
        abs.add("sas");
        abs.add("sad");
        JSON.toJSONString(abs);
    }

}
