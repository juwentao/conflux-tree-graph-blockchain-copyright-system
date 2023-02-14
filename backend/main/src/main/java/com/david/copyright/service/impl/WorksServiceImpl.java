package com.david.copyright.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.david.copyright.dto.WorksQueryDto;
import com.david.copyright.entity.User;
import com.david.copyright.entity.Works;
import com.david.copyright.mapper.UserMapper;
import com.david.copyright.mapper.WorksMapper;
import com.david.copyright.response.ResultBody;
import com.david.copyright.service.IWorksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.david.copyright.util.FileHashUtil;
import com.david.copyright.util.GenerateWorkUri;
import com.david.copyright.util.IpfsUtil;
import com.david.copyright.util.PdfUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author DavidQ
 * @since 2022-03-09
 */
@Service
public class WorksServiceImpl extends ServiceImpl<WorksMapper, Works> implements IWorksService {
    @Resource
    WorksMapper worksMapper;
    @Resource
    UserMapper userMapper;

    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-ddHHmmss");

    public List<Works> getWorksById(Long userId)
    {
        QueryWrapper<Works> queryWrapper=new QueryWrapper<Works>();
        queryWrapper.eq("userid", userId);
        queryWrapper.eq("status", 1);
        List<Works> worksList=worksMapper.selectList(queryWrapper);
        return worksList;
    }

    @Override
    public ResultBody editWorks(MultipartFile multipartFile, Works work) throws UnsupportedEncodingException {
        System.out.println(work);
        if (null!=multipartFile )
        {
            System.out.println("有文件");
            String driName = "worksfile";
            String ID= work.getId();
            String path = System.getProperty("user.dir")+ File.separator +driName + File.separator + ID ;
            String fileName=multipartFile.getOriginalFilename();
            work.setFilename(fileName);
            String encodeFileName=URLEncoder.encode(fileName,"UTF-8");
            work.setFilelocation(URLEncoder.encode(path,"UTF-8"));
            work.setFiledownloadurl("http://120.48.51.66:8083/copyright/works/downloadWorks?fileLocation="+work.getFilelocation()+"&fileName="+encodeFileName);
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
            String fileHashMD5=FileHashUtil.MD5.checksum(file);
            work.setFilehash(fileHashMD5);

            String workUriLocation=GenerateWorkUri.generateWorkUri(work,path);
            if(null==workUriLocation)
            {
                return ResultBody.error("生成workUri失败！请重试");
            }
            String workUri;
            try
            {
                workUri=IpfsUtil.upload(workUriLocation);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return ResultBody.error("上传Uri至ipfs失败！请重试");
            }
            work.setWorkuri(workUri);
            int updateFlag=worksMapper.updateById(work);
            if(updateFlag!=0)
            {
                return ResultBody.success("200","修改成功",work);
            }
            else
            {
                return ResultBody.error("修改失败");
            }
        }
        else
        {
            //若chainhash不为null说明此次编辑操作为上链操作后的编辑操作，需要生成存证证明证书
//            if(null!=work.getChainhash())
//            {
//                LambdaUpdateWrapper<Works> objectLambdaUpdateWrapper = Wrappers.lambdaUpdate();
//                objectLambdaUpdateWrapper.eq(Works::getId, work.getId());
//                objectLambdaUpdateWrapper.set(Works::getChainhash, work.getChainhash());
//                objectLambdaUpdateWrapper.set(Works::getStatus, work.getStatus());
//                objectLambdaUpdateWrapper.set(Works::getChaintime, work.getChaintime());
//                objectLambdaUpdateWrapper.set(Works::getBlockheight, work.getBlockheight());
//                PdfUtils pdfUtils=new PdfUtils();
//                Works workData=worksMapper.selectOne(new QueryWrapper<Works>().eq("id", work.getId()));
//                User userData= userMapper.selectById(workData.getUserid());
//                String certificatefilelocation= pdfUtils.workCertificatePdfout(work,workData,userData);
//                String downloadPdfUrl="http://120.48.51.66:8083/copyright/works/downloadWorks?fileLocation="+URLEncoder.encode(certificatefilelocation,"UTF-8")+"&fileName="+URLEncoder.encode("certificatefile.pdf","UTF-8");
//                if(null==certificatefilelocation)
//                {
//                    return ResultBody.error("修改失败");
//                }
//                objectLambdaUpdateWrapper.set(Works::getCertificatefilelocation, downloadPdfUrl);
//                int updateFlag=worksMapper.update(null, objectLambdaUpdateWrapper);
//                return ResultBody.success("200","修改成功",work);
//                if(updateFlag!=0)
//                {
//                    return ResultBody.success("200","修改成功",work);
//                }
//                else
//                {
//                    return ResultBody.error("修改失败");
//                }
//            }
//            else
//            {
                int updateFlag=worksMapper.updateById(work);
                if(updateFlag!=0)
                {
                    return ResultBody.success("200","修改成功",work);

                }
                else
                {
                    return ResultBody.error("修改失败");
                }
//            }

        }
    }

    @Override
    public ResultBody addWorks(MultipartFile multipartFile, Works work) throws UnsupportedEncodingException {
        String driName = "worksfile";
        String ID= IdWorker.getIdStr(work);
        String path = System.getProperty("user.dir")+ File.separator +driName + File.separator + ID ;
        if (null!=multipartFile )
        {
            String fileLocation=path;
            String fileName=multipartFile.getOriginalFilename();
            work.setFilename(fileName);
            String encodeFileName=URLEncoder.encode(fileName,"UTF-8");
            work.setFilelocation(URLEncoder.encode(fileLocation,"UTF-8"));
            work.setFiledownloadurl("http://120.48.51.66:8083/copyright/works/downloadWorks?fileLocation="+work.getFilelocation()+"&fileName="+encodeFileName);
            work.setStatus(0);
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
            String fileHashMD5=FileHashUtil.MD5.checksum(file);
            work.setFilehash(fileHashMD5);
            work.setId(ID);

            String workUriLocation=GenerateWorkUri.generateWorkUri(work,path);
            if(null==workUriLocation)
            {
                return ResultBody.error("生成workUri失败！请重试");
            }
            String workUri;
            try
            {
                workUri=IpfsUtil.upload(workUriLocation);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return ResultBody.error("上传Uri至ipfs失败！请重试");
            }
            work.setWorkuri(workUri);
            int insertFlag;
            insertFlag=worksMapper.insert(work);
            System.out.println(work.toString());
            if(insertFlag!=0)
            {
                return ResultBody.success("200","添加成功",work);
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
    public IPage<Works> listWorksPage(WorksQueryDto queryWorks) {
        IPage<Works> page = new Page<>(queryWorks.getCurrentPage(),queryWorks.getPageSize());
        //条件查询
        LambdaQueryWrapper<Works> queryWrapper = new LambdaQueryWrapper<Works>();

        System.out.println(queryWorks.toString());
        if (StringUtils.isNotBlank(queryWorks.getUserId()))
        {
            queryWrapper.eq(Works::getUserid, queryWorks.getUserId());
        }
        if(null!=queryWorks.getType())
        {
            queryWrapper.eq(Works::getType, queryWorks.getType());
        }
        if (StringUtils.isNotBlank(queryWorks.getWorkname()))
        {
            queryWrapper.like(Works::getWorkname, queryWorks.getWorkname());
        }
        queryWrapper.orderBy(true,false,Works::getCreatetime);
        return worksMapper.selectPage(page,queryWrapper);

    }
}
