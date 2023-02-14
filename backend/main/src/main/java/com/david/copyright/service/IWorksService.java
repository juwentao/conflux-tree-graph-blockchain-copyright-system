package com.david.copyright.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.david.copyright.dto.SalesQueryDto;
import com.david.copyright.dto.WorksQueryDto;
import com.david.copyright.entity.Sale;
import com.david.copyright.entity.Works;
import com.baomidou.mybatisplus.extension.service.IService;
import com.david.copyright.response.ResultBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author DavidQ
 * @since 2022-03-09
 */
public interface IWorksService extends IService<Works> {
    List<Works> getWorksById(Long userId);
    ResultBody addWorks(@RequestParam(value = "file") MultipartFile multipartFile, Works work) throws UnsupportedEncodingException;
    IPage<Works> listWorksPage(WorksQueryDto queryWorks);

    public ResultBody editWorks(@RequestParam(value = "file",required = false) MultipartFile multipartFile,Works work) throws UnsupportedEncodingException;

}
