package com.david.copyright.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.david.copyright.entity.News;
import com.david.copyright.response.ResultBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface INewsService extends IService<News> {
    IPage<News> listNewPage(Integer pageNum, Integer pageSize, String selection);
    ResultBody addNews(MultipartFile multipartFile, News news) throws UnsupportedEncodingException;
    ResultBody editNew(MultipartFile multipartFile,News newsInfo) throws UnsupportedEncodingException;
    List<News> limitList(Integer num);
}
