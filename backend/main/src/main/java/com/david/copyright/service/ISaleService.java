package com.david.copyright.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.david.copyright.dto.SalesQueryDto;
import com.david.copyright.entity.Sale;
import com.baomidou.mybatisplus.extension.service.IService;
import com.david.copyright.entity.Works;
import com.david.copyright.response.ResultBody;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author DavidQ
 * @since 2022-04-13
 */
public interface ISaleService extends IService<Sale> {

    ResultBody getSalePageList(SalesQueryDto salesQueryDto);

    ResultBody getDetailById(Integer sid);

    IPage<Sale> getSalesPage(SalesQueryDto salesQueryDto);
}
