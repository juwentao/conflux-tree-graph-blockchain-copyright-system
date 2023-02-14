package com.david.copyright.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.david.copyright.entity.Notice;
import com.david.copyright.mapper.NoticeMapper;
import com.david.copyright.response.ResultBody;
import com.david.copyright.service.INoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.security.sasl.SaslServer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author DavidQ
 * @since 2022-04-15
 */
@Api(tags = "通知相关接口")
@RestController
@RequestMapping("/copyright/notice")
public class NoticeController {
    @Autowired
    INoticeService iNoticeService;

    @PostMapping("/send")
    @ApiOperation(value = "给别人发消息", notes = "给别人发消息", produces = "application/json")
    ResultBody send_to_user(@RequestBody @Validated Map<String, String> map) {
        //如果是给系统发送反馈消息的话，接受receiveName为admin
        String sendName = map.get("sendName");
        String receiveName = map.get("receiveName");
        String content = map.get("content");
//        String time =
        LocalDateTime sendTime = LocalDateTime.now();
        Notice notice = new Notice(sendTime, sendName, receiveName, content, 0);
        iNoticeService.addNotice(notice);
        System.out.println(sendName + " " + content + " " + receiveName);
        ResultBody result = new ResultBody();
        result.setMessage("成功发送");
        System.out.println(content);
        return result;
    }

    @ApiOperation(value = "获取当前用户通知", notes = "获取当前用户通知", produces = "application/json")
    @PostMapping("/getNotice")
    ResultBody get_notice(@RequestBody Map<String, String> map) {
        String username = map.get("username");
        int type = Integer.parseInt(map.get("type"));
        System.out.println(username + type);
        List<Notice> noticeList = iNoticeService.limitList(username, type);
        ResultBody result = new ResultBody();
        result.setResult(noticeList);
        result.setCode("200");
        result.setMessage("获取成功");
        return result;
    }

    @ApiOperation(value = "修改通知状态", notes = "修改通知状态", produces = "application/json")
    @PostMapping("/changeState")
    ResultBody change_state(@RequestBody Map<String, String> map) {
        int id = Integer.parseInt(map.get("id"));
        int newState = Integer.parseInt(map.get("type"));
        System.out.println(id);
        iNoticeService.changeState(id, newState);
        ResultBody result = new ResultBody();
        result.setCode("200");
        result.setMessage("修改成功");
        return result;
    }

    @ApiOperation(value = "检索私信对象的历史消息记录", notes = "检索私信对象的历史消息记录", produces = "appliaction/json")
    @PostMapping("/getHistory")
    ResultBody get_history(@RequestBody Map<String, String> map) {
        ResultBody result = new ResultBody();
        String sendName = map.get("sendName");
        String receiveName = map.get("receiveName");
        List<Notice> noticeList = iNoticeService.getChatInfo(sendName, receiveName);
        result.setResult(noticeList);
        System.out.println(noticeList);
        result.setCode("200");
        result.setMessage("修改成功");
        return result;
    }

    @ApiOperation(value = "删除私信对象的历史消息记录", notes = "删除私信对象的历史消息记录", produces = "appliaction/json")
    @PostMapping("/deleteHistory")
    ResultBody del_history(@RequestBody Map<String, String> map) {
        ResultBody result = new ResultBody();
        String sendName = map.get("sendName");
        String receiveName = map.get("receiveName");
        iNoticeService.deleteChatInfo(sendName, receiveName);
        result.setCode("200");
        result.setMessage("删除成功");
        return result;
    }

    @ApiOperation(value = "已读私信对象的历史消息记录", notes = "已读私信对象的历史消息记录", produces = "appliaction/json")
    @PostMapping("/setIsRead")
    ResultBody set_isRead(@RequestBody Map<String, String> map) {
        ResultBody result = new ResultBody();
        String sendName = map.get("sendName");
        String receiveName = map.get("receiveName");
        iNoticeService.setChatInfoState(sendName, receiveName);
        result.setCode("200");
        result.setMessage("成功设为已读");
        return result;
    }

    @ApiOperation(value = "私信对象的最新消息", notes = "私信对象的最新消息", produces = "appliaction/json")
    @PostMapping("/getLatestInfo")
    ResultBody get_latest_info(@RequestBody Map<String,String>map){
        ResultBody result = new ResultBody();
        String username = map.get("username");
        List<Notice>latestNoticeList=iNoticeService.getLatestInfo(username);
        result.setResult(latestNoticeList);
        result.setCode("200");
        result.setMessage("查询成功");
        return result;
    }
}
