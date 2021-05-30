package com.example.ssm.shop.controller;

import com.example.ssm.shop.dto.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author admin
 */
@Controller
@Slf4j
public class FileController {

    public final String allowSuffix = ".bmp.jpg.jpeg.png.gif";

    public final String rootPath = "C:\\Users\\ThinkPad\\Desktop\\uploads";

    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult jsonResult(@RequestParam("file") MultipartFile file) {

        //1.文件后缀过滤，只允许.bmp.jpg.jpeg.png.gif这些格式的文件进行传输
        //得到上传时的文件的完整名称,如spring.jpeg
        String filename = file.getOriginalFilename();
        //文件名,如spring
        String name = filename.substring(0, filename.indexOf("."));
        //文件后缀,如.jpeg
        String suffix = filename.substring(filename.lastIndexOf("."));

        if (allowSuffix.indexOf(suffix) == -1) {
            return new JsonResult().error("不允许上传该后缀的文件！");
        }


        //2.创建文件目录

        //目标文件
        //File.separator 的作用相当于 ' \  '
        String fullPath = rootPath + File.separator + filename;
        File descFile = new File(fullPath);
        int i = 1;
        //若文件存在重命名
        String newFilename = filename;
        while (descFile.exists()) {
            newFilename = name + "(" + i + ")" + suffix;
            String parentPath = descFile.getParent();
            descFile = new File(parentPath + File.separator + newFilename);
            i++;
        }
        //判断目标文件所在的目录是否存在
        if (!descFile.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            descFile.getParentFile().mkdirs();
        }

        //3.存储文件
        //将内存中的数据写入磁盘
        try {
            //利用file.transferTo()进行上传。
            file.transferTo(descFile);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传失败，cause:{}", e);
        }
        //完整的url
        String fileUrl = "/uploads/" + newFilename;
        return JsonResult.success(fileUrl);
    }
}
