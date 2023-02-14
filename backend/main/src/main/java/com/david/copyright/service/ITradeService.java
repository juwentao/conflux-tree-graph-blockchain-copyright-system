package com.david.copyright.service;

import com.david.copyright.dto.TradeQueryDto;
import com.david.copyright.entity.Trade;
import com.baomidou.mybatisplus.extension.service.IService;
import com.david.copyright.response.ResultBody;

import java.io.UnsupportedEncodingException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author DavidQ
 * @since 2022-04-13
 */
public interface ITradeService extends IService<Trade> {
    ResultBody buyUsageRights(Trade trade) throws UnsupportedEncodingException;

    ResultBody buyCopyright(Trade trade,String chaintime,String blockheight) throws UnsupportedEncodingException;

    ResultBody search(TradeQueryDto tradeQueryDto);

    ResultBody getIncome(Long userid);
}
