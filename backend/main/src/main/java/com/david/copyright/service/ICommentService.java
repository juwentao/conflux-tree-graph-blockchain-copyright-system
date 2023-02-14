package com.david.copyright.service;

import com.david.copyright.dto.CommentQueryDto;
import com.david.copyright.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.david.copyright.response.ResultBody;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author DavidQ
 * @since 2022-04-13
 */
public interface ICommentService extends IService<Comment> {
    ResultBody getCommentPageList(CommentQueryDto commentQueryDto);
    ResultBody addComment(Comment comment);
}
