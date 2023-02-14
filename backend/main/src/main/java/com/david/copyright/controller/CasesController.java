package com.david.copyright.controller;

import com.david.copyright.entity.Cases;
import com.david.copyright.response.ResultBody;
import com.david.copyright.service.ICasesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Api(tags = "案例相关接口")
@RestController
@RequestMapping("/copyright/case")
public class CasesController {
    @Autowired
    ICasesService iCasesService;


    @ApiOperation(value="获取所有在售版权作品列表", notes="获取所有案例详情", produces="application/json")
    @GetMapping("/getCaseInfo")
    public ResultBody getCaseInfo() {
        List<Cases> caseList=iCasesService.list();
//        caseList.forEach(System.out::println);
        return ResultBody.success("200","成功获取案例列表",caseList);
    }

    @ApiOperation(value="获取案例详情", notes="获取案例详情", produces="application/json")
    @PostMapping("/getCaseById")
    public ResultBody getCaseById(@RequestBody Map<String, String> map) {
        String id = map.get("id");
        Cases cases = iCasesService.getById(id);
        if (cases != null) {
            return ResultBody.success("200","查找成功",cases);

        } else {
            return ResultBody.error("查找失败");
        }
    }

    @ApiOperation(value="添加案例", notes="添加案例", produces="application/json")
    @PostMapping("/addCase")
    public ResultBody addCase(@RequestParam(value = "img") MultipartFile multipartFile,Cases cases) throws UnsupportedEncodingException {
        return iCasesService.addCase(multipartFile,cases);
    }

    @ApiOperation(value="删除案例", notes="删除案例", produces="application/json")
    @PostMapping("/deleteCase")
    public ResultBody deleteCase(@RequestBody Map<String, String> map) {
        String id = map.get("id");
        if(iCasesService.removeById(id))
        {
            return ResultBody.success("200","成功删除该案例");
        }
        else
        {
            return ResultBody.error("400","删除失败");
        }
    }

    @ApiOperation(value="批量删除案例", notes="批量删除案例", produces="application/json")
    @PostMapping("/batchDeleteCase")
    public ResultBody batchDeleteCase(@RequestBody List<String>idList){
        System.out.println(idList);
        if(iCasesService.removeByIds(idList))
        {
            return ResultBody.success("200","成功批量删除");
        }
        else
        {
            return ResultBody.error("400","批量删除失败");
        }
    }

    @ApiOperation(value="查找案例", notes="查找案例", produces="application/json")
    @PostMapping("/searchCase")
    public ResultBody searchCase(@RequestBody Map<String, String> map) {
        String selection = map.get("selection");
        int pageNum=Integer.parseInt(map.get("pageNum"));
        int pageSize=Integer.parseInt(map.get("pageSize"));
        return ResultBody.success(iCasesService.listCasePage(pageNum,pageSize,selection));
    }

    @ApiOperation(value="编辑案例", notes="编辑案例", produces="application/json")
    @PostMapping("/editCase")
    public ResultBody editCase(@RequestParam(value = "img",required = false) MultipartFile multipartFile,Cases cases) throws UnsupportedEncodingException {
        return iCasesService.editCase(multipartFile,cases);
    }
}
