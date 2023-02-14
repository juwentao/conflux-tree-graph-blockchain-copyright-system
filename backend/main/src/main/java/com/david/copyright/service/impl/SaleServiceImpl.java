package com.david.copyright.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.david.copyright.dto.SalesQueryDto;
import com.david.copyright.entity.Sale;
import com.david.copyright.entity.User;
import com.david.copyright.entity.Works;
import com.david.copyright.mapper.SaleMapper;
import com.david.copyright.mapper.UserMapper;
import com.david.copyright.mapper.WorksMapper;
import com.david.copyright.response.ResultBody;
import com.david.copyright.service.ISaleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.david.copyright.vo.SaleVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.net.URLDecoder;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author DavidQ
 * @since 2022-04-13
 */
@Service
public class SaleServiceImpl extends ServiceImpl<SaleMapper, Sale> implements ISaleService {

    @Resource
    SaleMapper saleMapper;

    @Resource
    WorksMapper worksMapper;

    @Resource
    UserMapper userMapper;

    @Override
    public ResultBody getSalePageList(SalesQueryDto salesQueryDto) {
        System.out.println(salesQueryDto.toString());
        Page<SaleVo> page=new Page<>(salesQueryDto.getCurrentPage(),salesQueryDto.getPageSize());

        List<SaleVo> list=saleMapper.getSalePageList(page,salesQueryDto);

        return ResultBody.success(MapUtil.builder().put("record",list).put("page",page).map());

    }

    @Override
    public ResultBody getDetailById(Integer sid) {
        Sale sale=saleMapper.selectById(sid);
        String workId=sale.getWorkid();
        Works works=worksMapper.selectById(workId);
        User user=userMapper.selectById(works.getUserid());
        File file=new File(URLDecoder.decode(works.getFilelocation())+File.separator+works.getFilename());
        Long filesize=file.length();
        String copyrightownername=works.getCopyrightownername();
        String copyrightownerid=works.getCopyrightownerid();
        String ownername=null;
        char[] cname=copyrightownername.toCharArray();
        if(cname.length ==1){
            ownername =  copyrightownername;
        }
        if(cname.length == 2){
            ownername =  copyrightownername.replaceFirst(copyrightownername.substring(1),"*");
        }
        if (cname.length > 2) {
            ownername =  copyrightownername.replaceFirst(copyrightownername.substring(1,cname.length-1) ,"*");
        }

        char[] cid =  copyrightownerid.toCharArray();

        for(int i=0; i<cid.length;i++){
            if(i>2 && i<15){
                cid[i] = '*';
            }
        }
        String ownerid=String.valueOf(cid);;
        return ResultBody.success(MapUtil.builder()
                .put("sid",sale.getSid())
                .put("status",sale.getStatus())
                .put("title",sale.getTitle())
                .put("description",sale.getDescription())
                .put("price",sale.getPrice())
                .put("downloads",sale.getDownloads())
                .put("type",works.getType())
                .put("chaintime",works.getChaintime())
                .put("blockheight",works.getBlockheight())
                .put("workname",works.getWorkname())
                .put("filename",works.getFilename())
                .put("filehash",works.getFilehash())
                .put("workid",works.getId())
                .put("filesize",filesize)
                .put("ownername",ownername)
                .put("ownerid",ownerid)
                .put("userid",user.getId())
                .put("username",user.getUsername())
                .map());

    }

    @Override
    public IPage<Sale> getSalesPage(SalesQueryDto salesQueryDto) {
        IPage<Sale> page = new Page<>(salesQueryDto.getCurrentPage(),salesQueryDto.getPageSize());
        //条件查询
        LambdaQueryWrapper<Sale> queryWrapper = new LambdaQueryWrapper<Sale>();
        System.out.println(salesQueryDto.toString());

        if(null!=salesQueryDto.getStatus())
        {
            queryWrapper.eq(Sale::getStatus, salesQueryDto.getStatus());
        }
        if (StringUtils.isNotBlank(salesQueryDto.getUserid()))
        {
            queryWrapper.eq(Sale::getUserid, salesQueryDto.getUserid());
        }
        return saleMapper.selectPage(page,queryWrapper);
    }


}
