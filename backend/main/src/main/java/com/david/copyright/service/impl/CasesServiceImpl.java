package com.david.copyright.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.david.copyright.entity.Cases;

import com.david.copyright.entity.User;
import com.david.copyright.entity.Works;
import com.david.copyright.mapper.CasesMapper;
import com.david.copyright.response.ResultBody;
import com.david.copyright.service.ICasesService;
import com.david.copyright.util.FileHashUtil;
import com.david.copyright.util.GenerateWorkUri;
import com.david.copyright.util.IpfsUtil;
import com.david.copyright.util.PdfUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class CasesServiceImpl extends ServiceImpl<CasesMapper, Cases>  implements ICasesService {
    @Resource
    CasesMapper casesMapper;

    @Override
    public IPage<Cases> listCasePage(Integer pageNum, Integer pageSize, String selection) {
        IPage<Cases> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Cases> queryWrapper = new LambdaQueryWrapper<Cases>();
        if (StringUtils.isNotBlank(selection))
        {
            queryWrapper.like(Cases::getTitle, selection);
        }
        return casesMapper.selectPage(page,queryWrapper);
    }

    @Override
    public ResultBody addCase(MultipartFile multipartFile, Cases cases) throws UnsupportedEncodingException {
        String path = System.getProperty("user.dir")+ File.separator +"caseImg";
        if (null!=multipartFile )
        {
            String fileLocation=path;
            String fileName=multipartFile.getOriginalFilename();
            String encodeFileName= URLEncoder.encode(fileName,"UTF-8");
            String encodeFileLocation=URLEncoder.encode(fileLocation,"UTF-8");
            cases.setUrl("http://120.48.51.66:8083/copyright/works/downloadWorks?fileLocation="+encodeFileLocation+"&fileName="+encodeFileName);
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
            cases.setCreatetime(LocalDateTime.now());
            int insertFlag;
            System.out.println(cases.toString());
            insertFlag=casesMapper.insert(cases);
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
    public ResultBody editCase(MultipartFile multipartFile, Cases cases) throws UnsupportedEncodingException {
        if (null!=multipartFile )
        {
            String path = System.getProperty("user.dir")+ File.separator +"caseImg";
            String fileLocation=path;
            String fileName=multipartFile.getOriginalFilename();
            String encodeFileName= URLEncoder.encode(fileName,"UTF-8");
            String encodeFileLocation=URLEncoder.encode(fileLocation,"UTF-8");
            cases.setUrl("http://120.48.51.66:8083/copyright/works/downloadWorks?fileLocation="+encodeFileLocation+"&fileName="+encodeFileName);
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
            System.out.println(cases.toString());
            int updateFlag=casesMapper.updateById(cases);
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
            int updateFlag=casesMapper.updateById(cases);
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
}
