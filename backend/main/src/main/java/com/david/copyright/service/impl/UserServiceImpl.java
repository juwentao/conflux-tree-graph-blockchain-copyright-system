package com.david.copyright.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.david.copyright.entity.User;
import com.david.copyright.entity.Works;
import com.david.copyright.mapper.UserMapper;
import com.david.copyright.response.ResultBody;
import com.david.copyright.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.david.copyright.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author DavidQ
 * @since 2022-03-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    UserMapper userMapper;
    public ResultBody certify(String id,String name, String idnumber)
    {
        System.out.println(id+name+idnumber);
        String host = "https://dfidveri.market.alicloudapi.com";
        String path = "/verify_id_name";
        String method = "POST";
        String appcode = "1322cbda23e84becb6b30d6b987c97c7";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("id_number", idnumber);
        bodys.put("name", name);

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            String body=EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSON.parseObject(body);
            System.out.println(jsonObject.toJSONString());
            System.out.println(response.getStatusLine().getStatusCode());
            if(response.getStatusLine().getStatusCode()==200)
            {
                System.out.println(1);
                String state = jsonObject.getString("state");
                if(state.equals("1"))
                {
                    LambdaUpdateWrapper<User> objectLambdaUpdateWrapper = Wrappers.lambdaUpdate();
                    objectLambdaUpdateWrapper.eq(User::getId,id);
                    objectLambdaUpdateWrapper.set(User::getIscertified,true);
                    objectLambdaUpdateWrapper.set(User::getRealname, name);
                    objectLambdaUpdateWrapper.set(User::getIdnumber, idnumber);
                    int updateFlag=userMapper.update(null, objectLambdaUpdateWrapper);
                    if(updateFlag!=0)
                    {
                        return ResultBody.success("200","实名认证成功");
                    }
                    else
                    {
                        return ResultBody.error("实名认证失败，请重试");
                    }
                }
                else
                {
                    System.out.println(2);
                    return ResultBody.error("实名认证失败，请检查证件号及真是姓名是否正确");
                }
            }
            else
            {
                System.out.println(3);
                return ResultBody.error("实名认证失败，请检查证件号及真是姓名是否正确");
            }

            //获取response的body
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBody.error("实名认证过程中出错");
        }
    }
}
