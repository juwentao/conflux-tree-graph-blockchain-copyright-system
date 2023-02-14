package com.david.copyright.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.david.copyright.entity.Trade;
import com.david.copyright.entity.User;
import com.david.copyright.entity.Works;
import com.david.copyright.mapper.UserMapper;
import com.david.copyright.mapper.WorksMapper;
import com.david.copyright.service.IUserService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * Created by wangpeng on 2018/02/01.
 */
public class PdfUtils {
    // 利用模板生成pdf
    public String workCertificatePdfout(Works works,Works workData,User userData) {
        Map<String,String> map = new HashMap<>();

        map.put("depositorySubject",userData.getRealname());
        map.put("depositorySubjectId",userData.getIdnumber());
        map.put("chainTime",works.getChaintime());
        map.put("blockHeight",works.getBlockheight());
        map.put("txHash",works.getChainhash());
        map.put("fileHash",workData.getFilehash());
        map.put("workName",workData.getWorkname());
        map.put("workType",workData.getType()==1?"摄影作品":workData.getType()==2?"文字作品":workData.getType()==3?"影视作品":workData.getType()==4?"音乐作品":"美术作品");
        map.put("ownerName",workData.getCopyrightownername());
        map.put("ownerType",workData.getCopyrightownertype()==1?"自然人":workData.getCopyrightownertype()==2?"企业法人":workData.getCopyrightownertype()==3?"机关法人":workData.getCopyrightownertype()==4?"事业单位法人":workData.getCopyrightownertype()==5?"社会团体法人":"其他");
        map.put("blockChainName","树图区块链");
        map.put("ownerId",workData.getCopyrightownerid());
        map.put("fileName", URLDecoder.decode(workData.getFilename()));
        File file=new File(URLDecoder.decode(workData.getFilelocation())+File.separator+workData.getFilename());
        if(!file.exists()){
            System.out.println("文件不存在呀");
        }
        map.put("dataSize", file.length() +"字节");
        map.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        // 模板路径
        String templatePath = System.getProperty("user.dir")+ File.separator+"myTemplateForm.pdf";
        String sealPath=System.getProperty("user.dir")+ File.separator+"seal.png";
        String fontPath=System.getProperty("user.dir")+ File.separator+"simsun.ttc,1";
        String outputPath=System.getProperty("user.dir")+ File.separator +"worksfile" + File.separator + works.getId();
        Map<String,String> map2 = new HashMap<>();
        map2.put("img",sealPath);

        Map<String,Object> o=new HashMap();
        o.put("datemap",map);
        o.put("imgmap",map2);

        // 生成的新文件路径
        String newPDFPath = outputPath+File.separator+"certificatefile.pdf";

        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            BaseFont bf = BaseFont.createFont(fontPath , BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font FontChinese = new Font(bf, 5, Font.NORMAL);
            out = new FileOutputStream(newPDFPath);// 输出流
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            //文字类的内容处理
            Map<String,String> datemap = (Map<String, String>) o.get("datemap");
            form.addSubstitutionFont(bf);
            for(String key : datemap.keySet()){
                String value = datemap.get(key);
                form.setField(key,value);
            }
            //图片类的内容处理
            Map<String,String> imgmap = (Map<String,String>)o.get("imgmap");
            for(String key : imgmap.keySet()) {
                String value = imgmap.get(key);
                String imgpath = value;
                int pageNo = form.getFieldPositions(key).get(0).page;
                Rectangle signRect = form.getFieldPositions(key).get(0).position;
                float x = signRect.getLeft();
                float y = signRect.getBottom();
                //根据路径读取图片
                Image image = Image.getInstance(imgpath);
                //获取图片页面
                PdfContentByte under = stamper.getOverContent(pageNo);
                //图片大小自适应
                image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                //添加图片
                image.setAbsolutePosition(x, y);
                under.addImage(image);
            }
            stamper.setFormFlattening(true);// 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
            stamper.close();
            Document doc = new Document();
            Font font = new Font(bf, 32);
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();
            return outputPath;

        } catch (IOException e) {
            System.out.println(e);
            return null;
        } catch (DocumentException e) {
            System.out.println(e);
            return null;
        }
    }

    public String tradeCertificatePdfout(Works works, Trade trade,User user) {
        Map<String,String> map = new HashMap<>();

        map.put("name",user.getRealname());
        map.put("workname",works.getWorkname());
        map.put("txhash",trade.getTxhash());
        map.put("time", LocalDate.now().toString());
        // 模板路径
        String templatePath = System.getProperty("user.dir")+ File.separator+"myTemplate2Form.pdf";
        String sealPath=System.getProperty("user.dir")+ File.separator+"seal.png";
        String fontPath=System.getProperty("user.dir")+ File.separator+"simsun.ttc,1";
        String outputPath=System.getProperty("user.dir")+ File.separator +"tradeCertificate" + File.separator + trade.getTxhash();
        File fileDir = new File(outputPath);
        if (!fileDir.exists() && !fileDir.isDirectory())
        {
            fileDir.mkdirs();
        }
        Map<String,String> map2 = new HashMap<>();
        map2.put("img",sealPath);

        Map<String,Object> o=new HashMap();
        o.put("datemap",map);
        o.put("imgmap",map2);

        // 生成的新文件路径
        String newPDFPath = outputPath+File.separator+"certificatefile.pdf";

        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            BaseFont bf = BaseFont.createFont(fontPath , BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font FontChinese = new Font(bf, 5, Font.NORMAL);
            out = new FileOutputStream(newPDFPath);// 输出流
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            //文字类的内容处理
            Map<String,String> datemap = (Map<String, String>) o.get("datemap");
            form.addSubstitutionFont(bf);
            for(String key : datemap.keySet()){
                String value = datemap.get(key);
                form.setField(key,value);
            }
            //图片类的内容处理
            Map<String,String> imgmap = (Map<String,String>)o.get("imgmap");
            for(String key : imgmap.keySet()) {
                String value = imgmap.get(key);
                String imgpath = value;
                int pageNo = form.getFieldPositions(key).get(0).page;
                Rectangle signRect = form.getFieldPositions(key).get(0).position;
                float x = signRect.getLeft();
                float y = signRect.getBottom();
                //根据路径读取图片
                Image image = Image.getInstance(imgpath);
                //获取图片页面
                PdfContentByte under = stamper.getOverContent(pageNo);
                //图片大小自适应
                image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                //添加图片
                image.setAbsolutePosition(x, y);
                under.addImage(image);
            }
            stamper.setFormFlattening(true);// 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
            stamper.close();
            Document doc = new Document();
            Font font = new Font(bf, 32);
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();
            return outputPath;

        } catch (IOException e) {
            System.out.println(e);
            return null;
        } catch (DocumentException e) {
            System.out.println(e);
            return null;
        }
    }

    public String tradeCopyrightCertificatePdfout(Works works, Trade trade,User user,String chaintime,String blockheight) {
        Map<String,String> map = new HashMap<>();

        map.put("workname",works.getWorkname());
        map.put("worktype",works.getType()==1?"摄影作品":works.getType()==2?"文字作品":works.getType()==3?"影视作品":works.getType()==4?"音乐作品":"美术作品");
        map.put("filehash",works.getFilehash());
        map.put("filename", URLDecoder.decode(works.getFilename()));
        File file=new File(URLDecoder.decode(works.getFilelocation())+File.separator+works.getFilename());
        map.put("filesize", file.length() +"字节");
        map.put("txhash",trade.getTxhash());
        map.put("blockheight",blockheight);
        map.put("chainblockname","树图区块链");
        map.put("ownername",user.getRealname());
        map.put("ownerid",user.getIdnumber());
        map.put("oldownername",works.getCopyrightownername());
        map.put("oldownerid",works.getCopyrightownerid());
        map.put("date", LocalDate.now().toString());
        map.put("chaintime",chaintime);


        // 模板路径
        String templatePath = System.getProperty("user.dir")+ File.separator+"myTemplate3Form.pdf";

        System.out.println(System.getProperty("user.dir"));
        System.out.println(File.separator);


        String sealPath=System.getProperty("user.dir")+ File.separator+"seal.png";
        String fontPath=System.getProperty("user.dir")+ File.separator+"simsun.ttc,1";
        String outputPath=System.getProperty("user.dir")+ File.separator +"copyrightcertificatefile" + File.separator + trade.getTxhash();
        File fileDir = new File(outputPath);
        if (!fileDir.exists() && !fileDir.isDirectory())
        {
            fileDir.mkdirs();
        }
        Map<String,String> map2 = new HashMap<>();
        map2.put("img",sealPath);

        Map<String,Object> o=new HashMap();
        o.put("datemap",map);
        o.put("imgmap",map2);

        // 生成的新文件路径
        String newPDFPath = outputPath+File.separator+"certificatefile.pdf";

        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            BaseFont bf = BaseFont.createFont(fontPath , BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font FontChinese = new Font(bf, 5, Font.NORMAL);
            out = new FileOutputStream(newPDFPath);// 输出流
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            //文字类的内容处理
            Map<String,String> datemap = (Map<String, String>) o.get("datemap");
            form.addSubstitutionFont(bf);
            for(String key : datemap.keySet()){
                String value = datemap.get(key);
                form.setField(key,value);
            }
            //图片类的内容处理
            Map<String,String> imgmap = (Map<String,String>)o.get("imgmap");
            for(String key : imgmap.keySet()) {
                String value = imgmap.get(key);
                String imgpath = value;
                int pageNo = form.getFieldPositions(key).get(0).page;
                Rectangle signRect = form.getFieldPositions(key).get(0).position;
                float x = signRect.getLeft();
                float y = signRect.getBottom();
                //根据路径读取图片
                Image image = Image.getInstance(imgpath);
                //获取图片页面
                PdfContentByte under = stamper.getOverContent(pageNo);
                //图片大小自适应
                image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                //添加图片
                image.setAbsolutePosition(x, y);
                under.addImage(image);
            }
            stamper.setFormFlattening(true);// 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
            stamper.close();
            Document doc = new Document();
            Font font = new Font(bf, 32);
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();
            return outputPath;

        } catch (IOException e) {
            System.out.println(e);
            return null;
        } catch (DocumentException e) {
            System.out.println(e);
            return null;
        }
    }

    public static void main(String[] args) {

    }
}
