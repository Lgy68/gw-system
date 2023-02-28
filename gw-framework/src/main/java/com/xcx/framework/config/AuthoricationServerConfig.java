package com.xcx.framework.config;//package com.xcx.www.gwsystem.config;
//
//import com.xcx.www.gwsystem.service.UserDetailsServiceImpl;
//import com.xcx.www.gwsystem.domain.LoginUser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableAuthorizationServer
//public class AuthoricationServerConfig extends AuthorizationServerConfigurerAdapter {
//
//    @Autowired
//    AuthenticationManager authenticationManager;
//
//    @Autowired
//    RedisConnectionFactory redisConnectionFactory;
//
//    @Autowired
//    UserDetailsServiceImpl userDetailsService;
//
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security
//                // 令牌密钥端点的 Spring Security 访问规则（例如，像“isAuthenticated()”这样的 SpEL 表达式）。默认为空，被解释为“denyAll()”（无访问访问）。
//                //  /oauth/token_key  公开的
//                .tokenKeyAccess("permitAll()")
//                // 检查令牌端点的 Spring Security 访问规则（例如 SpEL 表达式像“isAuthenticated()”）。默认为空，解释为“denyAll()”（无访问权限）。
//                // /oauth/check_token  检查密钥需要身份认证
//                .checkTokenAccess("isAuthenticated()")
//                //允许客户端的表单身份验证（申请令牌）
//                .allowFormAuthenticationForClients();
//    }
//
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                .withClient("nobug")
//                .secret(new BCryptPasswordEncoder().encode("givemefive"))
//                .authorizedGrantTypes("password", "refresh_token")
//                .scopes("all")
//                .accessTokenValiditySeconds(60 * 60 * 24)
//                .refreshTokenValiditySeconds(60 * 60 * 24 * 7);
//    }
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints
//                // 指定token存储位置  缓存了token，所以没到期则不刷新
//                .tokenStore(tokenStore())
//                // 自定义生成令牌
//                .tokenEnhancer(tokenEnhancer())
//                .userDetailsService(userDetailsService)
//                .authenticationManager(authenticationManager)
//                // 是否重复使用 refresh_token
//                .reuseRefreshTokens(false)
//                //自定义认证异常（账号密码输入错误）处理封装返回类
//                .exceptionTranslator(new MyWebResponseExceptionTranslator())
//                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
//    }
//
//    @Bean
//    public TokenStore tokenStore(){
//        MyRedisTokenStore redisTokenStore = new MyRedisTokenStore(redisConnectionFactory);
//        redisTokenStore.setPrefix("oauth_access");
//        return redisTokenStore;
//    }
//
//    @Bean
//    public TokenEnhancer tokenEnhancer() {
//        return (accessToken, authentication) -> {
//            final Map<String,Object> map = new HashMap<String, Object>();
//            LoginUser loginUser = (LoginUser) authentication.getUserAuthentication().getPrincipal();
//            map.put("license","made by xucx");
//            map.put("userId",loginUser.getUser().getUserId());
//            map.put("username",loginUser.getUsername());
//            ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(map);
//            return accessToken;
//        };
//    }
//
//}
