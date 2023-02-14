package com.david.copyright.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.david.copyright.dto.CommentQueryDto;
import com.david.copyright.entity.Comment;
import com.david.copyright.entity.Trade;
import com.david.copyright.mapper.CommentMapper;
import com.david.copyright.mapper.TradeMapper;
import com.david.copyright.response.ResultBody;
import com.david.copyright.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.david.copyright.vo.CommentVo;
import com.david.copyright.vo.SaleVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author DavidQ
 * @since 2022-04-13
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    @Resource
    CommentMapper commentMapper;
    @Resource
    TradeMapper tradeMapper;

    @Override
    public ResultBody getCommentPageList(CommentQueryDto commentQueryDto) {
        System.out.println(commentQueryDto.toString());
        Page<CommentVo> page=new Page<>(commentQueryDto.getCurrentPage(),commentQueryDto.getPageSize());

        List<SaleVo> list=commentMapper.getCommentList(page,commentQueryDto);

        return ResultBody.success(MapUtil.builder().put("record",list).put("page",page).map());
    }

    @Override
    public ResultBody addComment(Comment comment) {
        comment.setDate(LocalDate.now());
        if(commentMapper.insert(comment)>0)
        {

            System.out.println(comment.getId());
            System.out.println(comment.getTradeid());
            Trade trade=tradeMapper.selectById(comment.getTradeid());
            trade.setIscomment(true);
            tradeMapper.updateById(trade);
            return ResultBody.success("评论成功");
        }
        else
        {
            return ResultBody.error("评论失败咯");
        }

    }
}
