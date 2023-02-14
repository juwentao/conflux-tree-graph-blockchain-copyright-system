package com.david.copyright.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.david.copyright.dto.WorksQueryDto;
import com.david.copyright.entity.User;
import com.david.copyright.entity.Works;
import com.david.copyright.mapper.WorksMapper;
import com.david.copyright.response.ResultBody;
import com.david.copyright.service.IUserService;
import com.david.copyright.service.IWorksService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author DavidQ
 * @since 2022-03-09
 */
@Api(tags = "作品相关API接口")
@RestController
@RequestMapping("/copyright/works")
public class WorksController {
    @Autowired
    IWorksService iWorksService;


    @ApiOperation(value="获取用户的作品数量", notes="获取用户的作品数量", produces="application/json")
    @GetMapping("/getWorksNum")
    public ResultBody getWorksNum(@RequestParam Long userId) {
        List<Works> worksList=iWorksService.getWorksById(userId);
        return ResultBody.success(MapUtil.builder().put("num",worksList.size()).map());
    }

    @ApiOperation(value="获取用户的作品", notes="获取用户作品", produces="application/json")
    @GetMapping("/getWorksById")
    public ResultBody getWorksById(@RequestParam Long userId) {

        List<Works> worksList=iWorksService.getWorksById(userId);
        Collections.reverse(worksList);
        Assert.notNull(worksList, "暂时没有作品噢");
        System.out.println(worksList.toString());
        return ResultBody.success(worksList);
    }

    @ApiOperation(value = "分页作品列表")
    @GetMapping(value = "/getWorksPage")
    public ResultBody getWorksPage(WorksQueryDto worksQueryDto){
        IPage<Works> page = iWorksService.listWorksPage(worksQueryDto);
        return ResultBody.success(page);
    }


    @ApiOperation(value="删除作品", notes="删除作品", produces="application/json")
    @PostMapping("/deleteWorks")
    public ResultBody deleteWorks(String id)
    {
        if(!iWorksService.removeById(id))
        {
            return ResultBody.error("删除作品失败");
        }
        else
        {
            return ResultBody.success("删除作品成功");
        }
    }

    @ApiOperation(value="用户添加作品", notes="用户添加作品", produces="application/json")
    @PostMapping("/addWorks")
    public ResultBody addWorks(@RequestParam(value = "file") MultipartFile multipartFile,Works work) throws UnsupportedEncodingException {
        ResultBody result=iWorksService.addWorks(multipartFile,work);
        return  result;
    }

    @ApiOperation(value="test", notes="test", produces="application/json")
    @PostMapping("/test")
    public ResultBody test(MultipartFile multipartFile) {
        String path = System.getProperty("user.dir")+ File.separator +"testfile" ;
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
        return ResultBody.success("存入文件成功");
    }

    @ApiOperation(value="用户编辑作品", notes="用户编辑作品", produces="application/json")
    @PostMapping("/editWorks")
    public ResultBody editWorks(@RequestParam(value = "file",required = false) MultipartFile multipartFile,Works work) throws UnsupportedEncodingException {
        System.out.println("进来了");
        ResultBody resultBody=iWorksService.editWorks(multipartFile,work);
        return resultBody;
    }

    //下载方法最好不要有返回值
    @ApiOperation(value="下载作品相关文件", notes="下载作品相关文件", produces="application/json")
    @GetMapping("/downloadWorks")
    public void downloadFile(@RequestParam String fileLocation,String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        File file=new File(URLDecoder.decode(fileLocation+File.separator+fileName));
        if(!file.exists()){
            System.out.println("文件不存在呀");
        }
        else
        {
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            // 实现文件下载
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                os.close();
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
