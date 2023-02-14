package com.david.copyright.service;

import com.david.copyright.entity.Notice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author DavidQ
 * @since 2022-04-15
 */
public interface INoticeService extends IService<Notice> {

    void changeState(int id,int newState);
    List<Notice> limitList(String name,int type);
    void addNotice(Notice notice);
    List<Notice> getChatInfo(String sendName, String receiveName);
    void deleteChatInfo(String sendName, String receiveName);
    void setChatInfoState(String sendName, String receiveName);
    List<Notice> getLatestInfo(String username);
}
