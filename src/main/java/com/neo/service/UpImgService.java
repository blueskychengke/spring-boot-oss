package com.neo.service;

import com.neo.exception.ImgException;
import org.springframework.web.multipart.MultipartFile;


public interface UpImgService {

    String updateHead(MultipartFile file)throws ImgException;
}
