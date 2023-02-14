package com.david.copyright.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.david.copyright.dto.TradeQueryDto;
import com.david.copyright.entity.*;
import com.david.copyright.mapper.*;
import com.david.copyright.response.ResultBody;
import com.david.copyright.service.ITradeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.david.copyright.util.PdfUtils;
import com.david.copyright.vo.SaleVo;
import com.david.copyright.vo.TradeVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class TradeServiceImpl extends ServiceImpl<TradeMapper, Trade> implements ITradeService {
    @Resource
    TradeMapper tradeMapper;

    @Resource
    SaleMapper saleMapper;

    @Resource
    WorksMapper worksMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    NoticeMapper noticeMapper;

    @Override
    public ResultBody buyUsageRights(Trade trade) throws UnsupportedEncodingException {
        trade.setIscomment(false);
        trade.setTradetime(LocalDateTime.now());
        Sale sale=saleMapper.selectById(trade.getSaleid());
        sale.setDownloads(sale.getDownloads()+1);
        Works works=worksMapper.selectById(trade.getWorkid());
        User user=userMapper.selectById(trade.getBuyid());
        PdfUtils pdfUtils=new PdfUtils();
        String certificatefilelocation= pdfUtils.tradeCertificatePdfout(works,trade,user);
        String downloadPdfUrl="http://120.48.51.66:8083/copyright/works/downloadWorks?fileLocation="+ URLEncoder.encode(certificatefilelocation,"UTF-8")+"&fileName="+URLEncoder.encode("certificatefile.pdf","UTF-8");
        trade.setTradecertification(downloadPdfUrl);
        if(null==certificatefilelocation)
        {
            return ResultBody.error("购买");
        }

        if(tradeMapper.insert(trade)>0)
        {

            saleMapper.updateById(sale);
            Notice notice=new Notice(LocalDateTime.now(), "admin", userMapper.selectById(trade.getSellid()).getUsername(), "您的作品"+works.getWorkname()+"已成交一笔交易", 0);
            noticeMapper.insert(notice);
            return  ResultBody.success();
        }
        else
        {
            return ResultBody.error("添加到数据库失败");
        }
    }

    @Override
    public ResultBody buyCopyright(Trade trade,String chaintime,String blockheight) throws UnsupportedEncodingException {
        System.out.println(trade.toString());
        System.out.println(chaintime);
        System.out.println(blockheight);
        trade.setIscomment(false);
        trade.setTradetime(LocalDateTime.now());
        Sale sale=saleMapper.selectById(trade.getSaleid());
        sale.setStatus(3);
        Works works=worksMapper.selectById(trade.getWorkid());
        works.setStatus(2);
        User user=userMapper.selectById(trade.getBuyid());
        PdfUtils pdfUtils=new PdfUtils();
        String certificatefilelocation= pdfUtils.tradeCopyrightCertificatePdfout(works,trade,user,chaintime,blockheight);
        String downloadPdfUrl="http://120.48.51.66:8083/copyright/works/downloadWorks?fileLocation="+ URLEncoder.encode(certificatefilelocation,"UTF-8")+"&fileName="+URLEncoder.encode("certificatefile.pdf","UTF-8");
        trade.setTradecertification(downloadPdfUrl);
        if(null==certificatefilelocation)
        {
            return ResultBody.error("购买");
        }

        if(tradeMapper.insert(trade)>0)
        {

            saleMapper.updateById(sale);
            worksMapper.updateById(works);
            Notice notice=new Notice(LocalDateTime.now(), "admin", userMapper.selectById(trade.getSellid()).getUsername(), "您的作品"+works.getWorkname()+"已完成著作权转让", 0);
            noticeMapper.insert(notice);
            return  ResultBody.success();
        }
        else
        {
            return ResultBody.error("添加到数据库失败");
        }
    }

    @Override
    public ResultBody search(TradeQueryDto tradeQueryDto) {
        Page<TradeVo> page=new Page<>(tradeQueryDto.getCurrentPage(),tradeQueryDto.getPageSize());

        List<SaleVo> list=tradeMapper.getTradePageList(page,tradeQueryDto);

        return ResultBody.success(MapUtil.builder().put("record",list).put("page",page).map());
    }

    @Override
    public ResultBody getIncome(Long userid) {
        List<Trade> list=tradeMapper.selectList(new QueryWrapper<Trade>().eq("sellid",userid));
        BigDecimal income = BigDecimal.ZERO;
        for(Trade trade:list)
        {
            if(trade.getTradetime().isAfter(LocalDate.now().atTime(0,0,0))&&trade.getTradetime().isBefore(LocalDate.now().atTime(23,59,59)))
            {
                income=income.add(trade.getTradeprice());
            }
        }
        return ResultBody.success(MapUtil.builder().put("income",income).map());
    }
}
