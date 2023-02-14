package com.david.copyright.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.david.copyright.dto.CommentQueryDto;
import com.david.copyright.entity.Comment;
import com.david.copyright.entity.Trade;
import com.david.copyright.entity.User;
import com.david.copyright.response.ResultBody;
import com.david.copyright.service.ICommentService;
import com.david.copyright.service.ITradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author DavidQ
 * @since 2022-04-13
 */
@Api(tags = "评论相关API接口")
@RestController
@RequestMapping("/copyright/comment")
public class CommentController {
    @Autowired
    ICommentService iCommentService;

    @ApiOperation(value="获得某个版权作品的所有评价", notes="获得某个版权作品的所有评价", produces="application/json")
    @GetMapping("/getCommentList")
    public ResultBody getCommentList(CommentQueryDto commentQueryDto) {
        System.out.println(commentQueryDto.toString());
        return iCommentService.getCommentPageList(commentQueryDto);
    }

    @ApiOperation(value="添加评论", notes="添加评论", produces="application/json")
    @PostMapping("/addComment")
    public ResultBody addComment(Comment comment) {
        return iCommentService.addComment(comment);

    }
}
