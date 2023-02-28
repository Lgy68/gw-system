package com.xcx.framework.config;//package com.xcx.www.gwsystem.config;
//
//import com.alibaba.fastjson.JSON;
//import com.xcx.www.domain.Response;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
//import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
//import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
//import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.access.AccessDeniedHandler;
//
//@Configuration
//@EnableResourceServer
//public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
//
//    @Value("${oauth.exclude.path}")
//    private String[] exclude;
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
////        resources.resourceId("rids").stateless(true);//配置资源id，与授权服务器配置的资源id一致，基于令牌认证
//        //将token异常和权限异常放入
//        resources.accessDeniedHandler(this.accessDeniedHandler()).authenticationEntryPoint(this.authenticationEntryPoint());
//    }
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .cors().disable()
//                .authorizeRequests().antMatchers(HttpMethod.OPTIONS,new String[]{"/**"}).permitAll().and()
//                .authorizeRequests().antMatchers(this.exclude).permitAll()
//                .and()
//                .authorizeRequests().anyRequest().authenticated()
//                .and()
//                .httpBasic();
//    }
//
//    //处理token异常
//    @Bean
//    public AuthenticationEntryPoint authenticationEntryPoint(){
//        OAuth2AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
//        entryPoint.setExceptionTranslator(this.webResponseExceptionTranslator());
//        return entryPoint;
//    }
//
//    //处理权限相关异常
//    @Bean
//    public AccessDeniedHandler accessDeniedHandler(){
//        OAuth2AccessDeniedHandler handler = new OAuth2AccessDeniedHandler();
//        handler.setExceptionTranslator(this.webResponseExceptionTranslator());
//        return handler;
//    }
//
//    /**
//     * 异常翻译处理器
//     * 调用WebResponseExceptionTranslator的实现类，对异常进行翻译封装处理
//     * @return
//     */
//    @Bean
//    public WebResponseExceptionTranslator webResponseExceptionTranslator(){
//        return new DefaultWebResponseExceptionTranslator(){
//            public ResponseEntity translate(Exception e) throws Exception {
//                ResponseEntity responseEntity = super.translate(e);
//                OAuth2Exception body = (OAuth2Exception) responseEntity.getBody();
//                HttpHeaders headers = new HttpHeaders();
//                headers.setAll(responseEntity.getHeaders().toSingleValueMap());
//                Response response = Response.error(4002,"authorized failed",body.getSummary());
//                return new ResponseEntity(JSON.toJSONString(response),headers, HttpStatus.OK);
//            }
//        };
//    }
//}
