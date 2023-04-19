package com.xcx.controller;

import com.google.code.kaptcha.Producer;
import com.xcx.common.common.RedisCache;
import com.xcx.common.domain.Response;
import com.xcx.common.domain.entiy.SysUser;
import com.xcx.common.usu.Constants;
import com.xcx.common.utils.SecurityUtils;
import com.xcx.common.utils.StringUtils;
import com.xcx.common.utils.uuid.IdUtils;
import com.xcx.framework.service.SysLoginService;
import com.xcx.framework.service.SysPermissionService;
import com.xcx.framework.service.SysRegisterService;
import com.xcx.system.domain.LoginBody;
import com.xcx.system.domain.RegisterBody;
import com.xcx.system.service.ISysConfigService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
public class LoginController {

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private RedisCache redisCache;

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    /**
     * 获取验证码
     *
     * @param response
     * @return
     */
    @GetMapping("/captchaImage")
    public Response getCode(HttpServletResponse response) {
        Response success = Response.success();
        String uuid = IdUtils.fastUUID();
        String key = "captcha_codes:" + uuid;
        //生成验证码
        String capText = captchaProducer.createText();
//        String capStr = capText.substring(0, capText.lastIndexOf("@"));
//        String code = capText.substring(capText.lastIndexOf("@") + 1);
        System.out.println(capText);
        BufferedImage image = captchaProducer.createImage(capText);
        //将验证码存放到redis数据库中
        redisCache.setCacheObject(key, capText, Constants.CAPTCHA_EXPIRATION, TimeUnit.HOURS);
        //转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return Response.error(e.getMessage());
        }
        success.put("img", Base64.encodeBase64(os.toByteArray()));
        success.put("uuid", uuid);
        return success;
    }

    @PostMapping("/register")
    public Response register(@RequestBody RegisterBody registerBody) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return Response.error("当前系统没有开启注册功能");
        }
        String msg = registerService.register(registerBody);
        return StringUtils.isEmpty(msg) ? Response.success() : Response.error(msg);
    }

    /**
     * 生成令牌
     *
     * @param loginBody
     * @return
     */
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response login(@RequestBody LoginBody loginBody) {
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(), loginBody.getUuid());
        return Response.success("操作成功", token);
    }

    @GetMapping("/getInfo")
    public Response getInfo()
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        Response response = Response.success();
        response.put("user", user);
        response.put("roles", roles);
        response.put("permissions", permissions);
        return response;
    }

}
