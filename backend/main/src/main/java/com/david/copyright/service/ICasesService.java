package com.david.copyright.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.david.copyright.entity.Cases;
import com.david.copyright.response.ResultBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

public interface ICasesService extends IService<Cases> {
    IPage<Cases> listCasePage(Integer pageNum, Integer pageSize, String selection);
    ResultBody addCase(MultipartFile multipartFile, Cases cases) throws UnsupportedEncodingException;
    ResultBody editCase(MultipartFile multipartFile, Cases cases) throws UnsupportedEncodingException;

}
