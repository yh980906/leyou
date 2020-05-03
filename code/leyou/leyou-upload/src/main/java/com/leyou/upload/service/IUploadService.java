package com.leyou.upload.service;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {

    String upload(MultipartFile multipartFile);
}
