package com.neo.controller;

import com.alibaba.fastjson.JSONObject;
import com.neo.service.UpImgService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UploadController {
    //Save the uploaded file to this folder
    private Logger logger = Logger.getLogger(UploadController.class);

    private static String UPLOADED_FOLDER = "E://temp//";

    @Autowired
    private UpImgService upImgService;

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/uploads") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }



    @RequestMapping(value = "/upload", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String headImgUpload(HttpServletRequest request, MultipartFile file) {
        Map<String, Object> value = new HashMap<String, Object>();
        try {
            String url = upImgService.updateHead(file);
            // logger.debug("图片路径{}",url);
            value.put("data", url);
            value.put("code", 0);
            value.put("msg", "图片上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            value.put("code", 2000);
            value.put("msg", "图片上传失败");
        }
        return JSONObject.toJSONString(value);
    }

}