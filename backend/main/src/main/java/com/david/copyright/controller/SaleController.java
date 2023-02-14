package com.david.copyright.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.david.copyright.dto.SalesQueryDto;
import com.david.copyright.entity.Sale;
import com.david.copyright.entity.Works;
import com.david.copyright.response.ResultBody;
import com.david.copyright.service.ISaleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author DavidQ
 * @since 2022-04-13
 */
@Api(tags = "售单相关API接口")
@RestController
@RequestMapping("/copyright/sale")
public class SaleController {
    @Autowired
    ISaleService iSaleService;

    @ApiOperation(value="用户添加售单", notes="用户添加售单", produces="application/json")
    @GetMapping("/getOnSaleNum")
    public ResultBody getOnSaleNum() {
        List<Sale> sales=iSaleService.list(new QueryWrapper<Sale>().ne("status",0));
        return ResultBody.success(MapUtil.builder().put("num",sales.size()).map());
    }

    @ApiOperation(value="用户添加售单", notes="用户添加售单", produces="application/json")
    @PostMapping("/addSale")
    public ResultBody addSale(Sale sale) {
        sale.setDownloads(0);
        if(iSaleService.save(sale))
        {
            return ResultBody.success("添加成功");
        }
        else
        {
            return ResultBody.error("添加失败");
        }
    }

    @ApiOperation(value="用户编辑售单", notes="用户编辑售单", produces="application/json")
    @PostMapping("/editSale")
    public ResultBody editSale(Sale sale) {
        if(iSaleService.updateById(sale))
        {
            return ResultBody.success("修改成功");
        }
        else
        {
            return ResultBody.error("修改失败");
        }
    }

    @ApiOperation(value="用户多条件搜索售单", notes="用户多条件搜索售单", produces="application/json")
    @GetMapping("/searchSale")
    public ResultBody searchSale(SalesQueryDto salesQueryDto) {
        System.out.println(salesQueryDto.toString());
        return iSaleService.getSalePageList(salesQueryDto);
    }

    @ApiOperation(value="分页获取售单", notes="分页获取售单", produces="application/json")
    @GetMapping("/getSalesPage")
    public ResultBody getSalesPage(SalesQueryDto salesQueryDto) {
        IPage<Sale> page = iSaleService.getSalesPage(salesQueryDto);
        return ResultBody.success(page);
    }

    @ApiOperation(value="根据id获取详细信息", notes="根据id获取详细信息", produces="application/json")
    @GetMapping("/getDetailById")
    public ResultBody getDetailById(Integer sid) {
        System.out.println(sid);
        return iSaleService.getDetailById(sid);
    }
}
