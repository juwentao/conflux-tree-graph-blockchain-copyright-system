package com.david.copyright.controller;

import com.david.copyright.entity.News;
import com.david.copyright.response.ResultBody;
import com.david.copyright.service.INewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Api(tags = "新闻相关接口")
@RestController
@RequestMapping("/copyright/new")
public class NewsController {
    @Autowired
    INewsService iNewsService;

    @ApiOperation(value="获取所有新闻", notes="获取所有新闻", produces="application/json")
    @PostMapping("/getNewsInfo")
    public ResultBody getNewsInfo(@RequestBody Map<String, Integer> map) {
        Integer num=map.get("num");
//        System.out.println(num);
        List<News> newsList = iNewsService.limitList(num);
        newsList.forEach(System.out::println);
        return ResultBody.success(newsList);
    }

    @ApiOperation(value="获取新闻详细信息", notes="获取新闻详细信息", produces="application/json")
    @PostMapping("/getNewById")
    public ResultBody getNewById(@RequestBody Map<String, String> map) {
        String id = map.get("id");
        News newInfo = iNewsService.getById(id);
        return ResultBody.success(newInfo);
    }

    @ApiOperation(value="添加新闻", notes="添加新闻", produces="application/json")
    @PostMapping("/addNews")
    public ResultBody addNews(@RequestParam(value = "img") MultipartFile multipartFile,News news) throws UnsupportedEncodingException {
        return iNewsService.addNews(multipartFile,news);
    }

    @ApiOperation(value="删除新闻", notes="删除新闻", produces="application/json")
    @PostMapping("/deleteNews")
    public ResultBody deleteNews(@RequestBody Map<String, String> map) {
        String id = map.get("id");
        if(iNewsService.removeById(id))
        {
            return ResultBody.success("200","删除成功");
        }
        else
        {
            return ResultBody.error("400","删除失败");
        }
    }

    @ApiOperation(value="批量删除新闻", notes="批量删除新闻", produces="application/json")
    @PostMapping("/batchDeleteNews")
    public ResultBody batchDeleteNews(@RequestBody List<String>idList){
        System.out.println(idList);
        if(iNewsService.removeByIds(idList))
        {
            return ResultBody.success("200","成功批量删除");
        }
        else
        {
            return ResultBody.error("400","批量删除失败");
        }
    }

    @ApiOperation(value="搜索新闻", notes="搜索新闻", produces="application/json")
    @PostMapping("/searchNews")
    public ResultBody searchNews(@RequestBody Map<String, String> map) {
        String selection = map.get("selection");
        int pageNum=Integer.parseInt(map.get("pageNum"));
        int pageSize=Integer.parseInt(map.get("pageSize"));
        return ResultBody.success(iNewsService.listNewPage(pageNum,pageSize,selection));
    }

    @ApiOperation(value="编辑新闻", notes="编辑新闻", produces="application/json")
    @PostMapping("/editNew")
    public ResultBody editNew(@RequestParam(value = "img",required = false) MultipartFile multipartFile,News newsInfo) throws UnsupportedEncodingException {
        return iNewsService.editNew(multipartFile,newsInfo);

    }

}
