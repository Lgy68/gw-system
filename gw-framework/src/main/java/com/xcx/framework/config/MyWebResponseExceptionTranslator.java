package com.xcx.framework.config;//package com.xcx.www.gwsystem.config;
//
//import com.xcx.www.gwsystem.domain.HttpStatus;
//import com.xcx.www.gwsystem.domain.Response;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MyWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
//
//    @Override
//    public ResponseEntity translate(Exception e) throws Exception {
//        Response response = new Response(HttpStatus.USERPASSERROR,"账号密码错误",e.getMessage());
//        return new ResponseEntity(response, org.springframework.http.HttpStatus.OK);
//    }
//
//}
