package com.xcx;


import org.jodconverter.office.OfficeException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;

@SpringBootTest
class GwAdminApplicationTests {

    @Test
    void contextLoads() throws OfficeException {
//        String path = "D:\\warehouse\\Develop\\环境配置.doc";
//        String toPath = "D:\\warehouse\\Develop\\环境配置.pdf";
//        OfficeToPdfUtil.convertByLocal(new File(path),new File(toPath));
//        OfficeToPdfUtil.stopServer();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("admin123"));
    }

}
