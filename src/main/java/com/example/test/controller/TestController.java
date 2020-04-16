package com.example.test.controller;

import com.example.test.service.DctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class TestController {
    @Autowired
    private DctService dctService;
    private static final String WATERMARKER = "watermarker";

    @RequestMapping("/dctHtml")
    public String dctHtml() {
        return "dct";
    }

    @RequestMapping("/dwtHtml")
    public String dwtHtml() {
        return "dwt";
    }

    @RequestMapping("/dct")
    @ResponseBody
    public void dct(HttpServletResponse response, @RequestParam("image") MultipartFile image, String msg) throws
            IOException {
        System.out.println(msg);
        byte[] bytes = dctService.saveAndTransformImage(image, msg);
        String imageName = WATERMARKER + "_" + image.getOriginalFilename();
        downloadImage(response, imageName, bytes);
    }

    @RequestMapping("/idct")
    @ResponseBody
    public void idct(HttpServletResponse response, @RequestParam("image") MultipartFile image)
            throws IOException {
        byte[] bytes = dctService.saveAndIdftImage(image);
        String originalFileName = image.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        downloadImage(response, WATERMARKER + "." + fileExtension, bytes);
    }

    private void downloadImage(HttpServletResponse response, String ImageName, byte[] bytes) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + ImageName + "\";");
        ServletOutputStream out = response.getOutputStream();
        out.write(bytes);
        out.close();
    }
}
