package com.david.copyright.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.david.copyright.dto.TradeQueryDto;
import com.david.copyright.entity.Sale;
import com.david.copyright.entity.Trade;
import com.david.copyright.response.ResultBody;
import com.david.copyright.service.ISaleService;
import com.david.copyright.service.ITradeService;
import com.david.copyright.service.IWorksService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author DavidQ
 * @since 2022-04-13
 */
@Api(tags = "交易相关API接口")
@RestController
@RequestMapping("/copyright/trade")
public class TradeController {
    @Autowired
    ITradeService iTradeService;

    @ApiOperation(value="用户购买版权使用权", notes="用户购买版权使用权", produces="application/json")
    @PostMapping("/buyUsageRights")
    public ResultBody buy(Trade trade) throws UnsupportedEncodingException {
        return iTradeService.buyUsageRights(trade);
    }

    @ApiOperation(value="用户购买版权著作权", notes="用户购买版权著作权", produces="application/json")
    @PostMapping("/buyCopyright")
    public ResultBody buyCopyright(Trade trade,String chaintime,String blockheight) throws UnsupportedEncodingException {
        return iTradeService.buyCopyright(trade,chaintime,blockheight);
    }

    @ApiOperation(value="用户购买版权使用权", notes="用户购买版权使用权", produces="application/json")
    @GetMapping("/isBuy")
    public ResultBody isBuy(String userid, String workid) {
        QueryWrapper<Trade> queryWrapper=new QueryWrapper<Trade>();
        queryWrapper.eq("buyid",userid);
        queryWrapper.eq("workid",workid);
        Trade trade=iTradeService.getOne(queryWrapper);
        if(null!=trade)
        {
            return ResultBody.success(true);
        }
        else
        {
            return ResultBody.success(false);
        }
    }

    @ApiOperation(value="用户获取自己购买的作品", notes="用户获取自己购买的作品", produces="application/json")
    @GetMapping("/search")
    public ResultBody search(TradeQueryDto tradeQueryDto)  {
        return iTradeService.search(tradeQueryDto);
    }

    @ApiOperation(value="统计某个用户当日的收益", notes="统计某个用户当日的收益", produces="application/json")
    @GetMapping("/getIncome")
    public ResultBody getIncome(Long userid)  {
        return iTradeService.getIncome(userid);
    }

}
