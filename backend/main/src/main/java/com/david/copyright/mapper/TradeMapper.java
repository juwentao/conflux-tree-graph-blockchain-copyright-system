package com.david.copyright.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.david.copyright.dto.SalesQueryDto;
import com.david.copyright.dto.TradeQueryDto;
import com.david.copyright.entity.Trade;
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
public interface TradeMapper extends BaseMapper<Trade> {
    List<SaleVo> getTradePageList(Page page, @Param("tradeQueryDto") TradeQueryDto tradeQueryDto);

}
