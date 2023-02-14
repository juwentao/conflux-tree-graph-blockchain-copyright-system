package com.david.copyright.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.david.copyright.config.ZhenZi;
import com.david.copyright.dto.UserDto;
import com.david.copyright.entity.Notice;
import com.david.copyright.entity.User;
import com.david.copyright.error.CommonEnum;
import com.david.copyright.response.ResultBody;
import com.david.copyright.service.INoticeService;
import com.david.copyright.service.IUserService;
import com.david.copyright.util.Certification;
import com.david.copyright.util.JwtUtils;
import com.david.copyright.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author DavidQ
 * @since 2022-02-09
 */

@Api(tags = "用户相关API接口")
@RestController
@RequestMapping("/copyright/user")
public class UserController {
    @Autowired
    IUserService iUserService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    ZhenZi zhenZi;
    @Resource
    RedisUtil redisUtil;
    @Autowired
    INoticeService iNoticeService;

    private static int ExpireTime = 60;   // redis中存储的过期时间60s
    @ApiOperation(value = "验证码", notes = "获取验证码", produces = "application/json")
    @PostMapping("/sendMsg")
    public ResultBody SendMsg(@RequestBody Map<String, String> map) {
        //用户名已被注册
//        User user1 = iUserService.getOne(new QueryWrapper<User>().eq("username", map.get("username")));
//        if (user1 != null) {
//            System.out.println("用户名已被注册");
//            return ResultBody.error("400", "用户名已被注册");
//        }
//        //手机号已被注册
//        User user2 = iUserService.getOne(new QueryWrapper<User>().eq("phone", map.get("phone")));
//        if (user2 != null) {
//            System.out.println("手机号已被注册");
//            return ResultBody.error("400", "手机号已被注册");
//        }

        ResultBody resultBody = new ResultBody();
        resultBody.setMessage(null);
        resultBody.setResult(null);
        String phone = map.get("phone");
        System.out.println("phone:" + phone);
        try {
            JSONObject json = zhenZi.send_message(phone);

            if (json.getString("code").equals("0")) {
                resultBody.setMessage("发送成功");
//                resultBody.setSuccess(true);
                //存储在redis中
                redisUtil.set(phone,json.getString(("verifyCode")),ExpireTime);
                System.out.println(redisUtil.get(phone).toString());
                resultBody.setResult(phone + json.getString("verifyCode"));
            } else {
                resultBody.setMessage("发送失败");
            }
        } catch (Exception e) {
            resultBody.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return resultBody;
    }

    @ApiOperation(value = "实名认证", notes = "实名认证", produces = "application/json")
    @PostMapping("/certify")
    public ResultBody certify(User user) {
        if(Certification.Certification(user.getRealname(),user.getIdnumber()))
        {
            user.setIscertified(true);
            if(iUserService.updateById(user))
            {
                return ResultBody.success();
            }
            else
            {
                return ResultBody.error("实名认证失败，请重试");
            }
        }
        else
        {
            return ResultBody.error("实名认证失败,请检查输入的信息是否正确");
        }

    }

    @ApiOperation(value="用户登录", notes="用户登录", produces="application/json")
    @PostMapping("/login")
    public ResultBody login(@Validated @RequestBody UserDto userDto, HttpServletResponse response) {
        User user = iUserService.getOne(new QueryWrapper<User>().eq("username", userDto.getUsername()));
        Assert.notNull(user, "用户不存在");
        if(!(user.getPassword()).equals(SecureUtil.md5(userDto.getPassword()+userDto.getUsername()))){
            return ResultBody.error(CommonEnum.BODY_NOT_MATCH);
        }
        System.out.println(userDto.getUsername());
        String jwt = jwtUtils.generateToken(user.getId());
        System.out.println(userDto.getUsername());
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");

        return ResultBody.success(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("phone", user.getPhone())
                .put("created",user.getCreated())
                .put("idnumber",user.getIdnumber())
                .put("iscertified",user.getIscertified())
                .put("sex",user.getSex())
                .put("points",user.getPoints())
                .put("isadmin",user.getIsadmin())
                .put("realname",user.getRealname())
                .put("type",user.getType())
                .map()
        );
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", produces = "application/json")
    @PostMapping("/register")
    public ResultBody register(@Validated @RequestBody Map<String, String> map) {

        //用户名已被注册
        User user1 = iUserService.getOne(new QueryWrapper<User>().eq("username", map.get("username")));
        if (user1 != null) {
            System.out.println("用户名已被注册");
            return ResultBody.error("400", "用户名已被注册");
        }
        //手机号已被注册
        User user2 = iUserService.getOne(new QueryWrapper<User>().eq("phone", map.get("phone")));
        if (user2 != null) {
            System.out.println("手机号已被注册");
            return ResultBody.error("400", "手机号已被注册");
        }
        //验证码正确
        String verifyCode = map.get("phoneCode");
        String phone = map.get("phone");
        // redis 保存对应电话号码的值
        String containCode = redisUtil.get(phone).toString();
        if (containCode != null && containCode.equals(verifyCode)) {
            User user = new User(map.get("username"), map.get("phone"), map.get("password"));
            user.setPassword(SecureUtil.md5(user.getPassword() + user.getUsername()));
            user.setIsadmin(false);
            user.setIscertified(false);
            user.setType(true);
            if (iUserService.save(user)) {
                // 未考虑多并发
                redisUtil.del(phone);
                Notice notice = new Notice(LocalDateTime.now(), "admin", user.getUsername(), "欢迎您使用本系统", 0);
                iNoticeService.addNotice(notice);
                return ResultBody.success("200", "注册成功");
            } else {
                return ResultBody.error("400", "注册失败");
            }
        } else {
            return ResultBody.error("400", "验证码错误");
        }

    }

}
