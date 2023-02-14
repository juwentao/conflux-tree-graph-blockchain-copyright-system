package com.david.copyright.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.david.copyright.entity.Notice;
import com.david.copyright.mapper.NoticeMapper;
import com.david.copyright.service.INoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author DavidQ
 * @since 2022-04-15
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {
    @Resource
    NoticeMapper noticeMapper;


    @Override
    public void changeState(int id,int newState) {
        Notice notice = noticeMapper.selectById(id);
        notice.setisRead(newState);
        noticeMapper.updateById(notice);
    }

    @Override
    public List<Notice> limitList(String name,int type) {
        List<Notice>noticeList=noticeMapper.selectList(new QueryWrapper<Notice>().eq("receive_name", name).eq("is_read",type));
        noticeList.sort(Comparator.comparing(Notice::getSendTime));
        return noticeList;
    }

    @Override
    public void addNotice(Notice notice) {
        noticeMapper.insert(notice);
    }

    @Override
    public List<Notice> getChatInfo(String sendName, String receiveName) {
        List<Notice>noticeList1=noticeMapper.selectList(new QueryWrapper<Notice>().eq("send_name", sendName).eq("receive_name", receiveName));
        List<Notice>noticeList2=noticeMapper.selectList(new QueryWrapper<Notice>().eq("send_name",receiveName).eq("receive_name",sendName));
        noticeList1.addAll(noticeList2);
        noticeList1.sort(Comparator.comparing(Notice::getSendTime));
        return noticeList1;
    }

    @Override
    public void deleteChatInfo(String sendName, String receiveName) {
        List<Notice>List1=noticeMapper.selectList(new QueryWrapper<Notice>()
                .eq("send_name", sendName)
                .eq("receive_name", receiveName)
                .eq("receive_delete",0));
        for(Notice notice:List1){
            notice.setReceiveDelete(1);
            noticeMapper.updateById(notice);
        }
        List<Notice>List2=noticeMapper.selectList(new QueryWrapper<Notice>()
                .eq("send_name",receiveName)
                .eq("receive_name", sendName)
                .eq("send_delete",0));
        for(Notice notice:List2){
            notice.setSendDelete(1);
            noticeMapper.updateById(notice);
        }
    }

    @Override
    public void setChatInfoState(String sendName, String receiveName) {
        List<Notice>List1=noticeMapper.selectList(new QueryWrapper<Notice>()
                .eq("send_name", sendName)
                .eq("receive_name", receiveName)
                .eq("is_read",0));
        //全部更新
        for(Notice notice:List1){
            notice.setisRead(1);
            noticeMapper.updateById(notice);
        }
        System.out.println("已全部设置为已读");
    }

    @Override
    public List<Notice> getLatestInfo(String username) {
        List<Notice> noticeList= noticeMapper.selectList(new QueryWrapper<Notice>()
                .eq("receive_name",username)
                .orderByDesc("send_time"));
        HashSet<String> s=new HashSet<>();
        List<Notice> ans=new ArrayList<>();
        for(Notice notice:noticeList){
            if(!s.contains(notice.getSendName())){
                ans.add(notice);
                s.add(notice.getSendName());
            }
        }
        return ans;
    }
}
