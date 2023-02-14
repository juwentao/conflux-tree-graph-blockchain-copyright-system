package com.david.copyright.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.david.copyright.dto.CommentQueryDto;
import com.david.copyright.dto.SalesQueryDto;
import com.david.copyright.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.david.copyright.vo.SaleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author DavidQ
 * @since 2022-04-13
 */
public interface CommentMapper extends BaseMapper<Comment> {
    List<SaleVo> getCommentList(Page page, @Param("commentQueryDto") CommentQueryDto commentQueryDto);
}
