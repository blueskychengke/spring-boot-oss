package com.neo.service.serviceImpl;

import com.neo.exception.ImgException;
import com.neo.service.UpImgService;
import com.neo.util.OSSClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UpImgServiceImpl implements UpImgService {
    public static final Logger logger = LoggerFactory.getLogger(UpImgServiceImpl.class);
    @Override
    public String updateHead(MultipartFile file) throws ImgException {
        if (file == null || file.getSize() <= 0) {
            throw new ImgException("file不能为空");
        }
        OSSClientUtil ossClient=new OSSClientUtil();
        String name = ossClient.uploadImg2Oss(file);
        String imgUrl = ossClient.getImgUrl(name);
        String[] split = imgUrl.split("\\?");
        return split[0];
    }
}
