package com.leyou.upload.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.upload.service.IUploadService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadServiceImpl implements IUploadService {
    //用来存储文件类型
    private static List<String> CONTENT_TYPES = Arrays.asList("image/jpeg", "image/gif","image/png","\tapplication/x-png");
    //用来存储日志
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadServiceImpl.class);

    @Autowired
    private FastFileStorageClient storageClient;
    /**
     * 图片上传
     * @param file
     * @return
     */
    @Override
    public String upload(MultipartFile file) {
        //1.检验文件类型

        //1.1获取文件名称
        String originalFilename = file.getOriginalFilename();
        //1.2获取文件类型
        String contentType = file.getContentType();
        //1.3检验文件类型是否符合规定
        if(!CONTENT_TYPES.contains(contentType)){
            // 文件类型不合法，直接返回null
            LOGGER.info("文件类型不合法：{}", originalFilename);
            return null;
        }
        //2.检验文件内容

        try {
            //2.1读取
            BufferedImage bufferdImage = ImageIO.read(file.getInputStream());
            //2.2判断是否为空
            if(bufferdImage==null){
                LOGGER.info("文件内容不合法：{}", originalFilename);
                return null;
            }
            /*
            //3.文件没有问题，所以可以保存至服务器
            file.transferTo(new File("F:\\IdeaProjects\\leyou1\\tools\\images\\"+originalFilename));
            //4.返回rul地址 回显
            return "http://image.leyou.com/"+originalFilename;

            下面是使用Fast
            */


            // Fast保存到服务器
            String ext = StringUtils.substringAfterLast(originalFilename, ".");
            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);

            return "http://image.leyou.com/"+storePath.getFullPath();
        } catch (IOException e) {
            LOGGER.info("服务器内部错误：{}", originalFilename);
            e.printStackTrace();
        }
        return null;
    }
}
