package com.david.copyright.config;
import com.alibaba.fastjson.JSONObject;
import com.zhenzi.sms.ZhenziSmsClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.sound.midi.SoundbankResource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "register")
public class ZhenZi {
    private String apiUrl="https://sms_developer.zhenzikj.com";
    private String appId="111235";
    private String appSecret="5161378d-61ba-4410-b02e-a209a2996fcb";

    private ZhenZi() {

    }

    //发送短信的核心方法
    public JSONObject send_message(String phone) throws Exception {
        JSONObject json = null;
        ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("number", phone);
        params.put("templateId", "8678");
        String[] templateParams = new String[2];
        //随机数生成验证码
        String code = String.valueOf(new Random().nextInt(899999) + 100000);
        templateParams[0] = code;
        templateParams[1] = "5";
        params.put("templateParams", templateParams);
        String result = client.send(params);

        json = JSONObject.parseObject(result);
        json.put("verifyCode", templateParams[0]);
        System.out.println(json);
        return json;
    }

}
