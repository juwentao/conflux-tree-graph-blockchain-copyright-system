package com.david.copyright.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.david.copyright.entity.News;
import com.david.copyright.mapper.NewsMapper;
import com.david.copyright.response.ResultBody;
import com.david.copyright.service.INewsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements INewsService {
    @Resource
    NewsMapper newsMapper;
    @Override
    public IPage<News> listNewPage(Integer pageNum, Integer pageSize, String selection) {
        IPage<News> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<News>();
        if (StringUtils.isNotBlank(selection))
        {
            queryWrapper.like(News::getTitle, selection);
        }
        return newsMapper.selectPage(page,queryWrapper);
    }
    @Override
    public ResultBody addNews(MultipartFile multipartFile, News news) throws UnsupportedEncodingException {
        String path = System.getProperty("user.dir")+ File.separator +"newsImg";
        if (null!=multipartFile )
        {
            String fileLocation=path;
            String fileName=multipartFile.getOriginalFilename();
            String encodeFileName= URLEncoder.encode(fileName,"UTF-8");
            String encodeFileLocation=URLEncoder.encode(fileLocation,"UTF-8");
            news.setUrl("http://120.48.51.66:8083/copyright/works/downloadWorks?fileLocation="+encodeFileLocation+"&fileName="+encodeFileName);
            File fileDir = new File(path);
            if (!fileDir.exists() && !fileDir.isDirectory())
            {
                fileDir.mkdirs();
            }
            File file=new File(fileDir , Objects.requireNonNull(multipartFile.getOriginalFilename()));
            try
            {
                multipartFile.transferTo(file);
            }
            catch (IOException e)
            {
                return ResultBody.error("存入文件时发生错误！请重试");
            }
            news.setCreatetime(LocalDateTime.now());
            int insertFlag;
            System.out.println(news.toString());
            insertFlag=newsMapper.insert(news);
            if(insertFlag!=0)
            {
                return ResultBody.success("200","添加成功");
            }
            else
            {
                return ResultBody.error("添加失败");
            }

        }
        else
        {
            return ResultBody.error("未收到文件！请上传文件");
        }
    }

    @Override
    public ResultBody editNew(MultipartFile multipartFile, News newsInfo) throws UnsupportedEncodingException {
        if (null!=multipartFile )
        {
            String path = System.getProperty("user.dir")+ File.separator +"newsImg";
            String fileLocation=path;
            String fileName=multipartFile.getOriginalFilename();
            String encodeFileName= URLEncoder.encode(fileName,"UTF-8");
            String encodeFileLocation=URLEncoder.encode(fileLocation,"UTF-8");
            newsInfo.setUrl("http://120.48.51.66:8083/copyright/works/downloadWorks?fileLocation="+encodeFileLocation+"&fileName="+encodeFileName);
            File fileDir = new File(path);
            if (!fileDir.exists() && !fileDir.isDirectory())
            {
                fileDir.mkdirs();
            }
            File file=new File(fileDir , Objects.requireNonNull(multipartFile.getOriginalFilename()));
            try
            {
                multipartFile.transferTo(file);
            }
            catch (IOException e)
            {
                return ResultBody.error("存入文件时发生错误！请重试");
            }
            System.out.println(newsInfo.toString());
            int updateFlag=newsMapper.updateById(newsInfo);
            if(updateFlag!=0)
            {
                return ResultBody.success("200","修改成功");
            }
            else
            {
                return ResultBody.error("修改失败");
            }
        }
        else
        {
            int updateFlag=newsMapper.updateById(newsInfo);
            if(updateFlag!=0)
            {
                return ResultBody.success("200","修改成功");
            }
            else
            {
                return ResultBody.error("修改失败");
            }
        }
    }

    @Override
    public List<News> limitList(Integer num) {
        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<News>();
        if (null!=num)
        {
            queryWrapper.last("limit "+num);
        }

        return newsMapper.selectList(queryWrapper);
    }
}
