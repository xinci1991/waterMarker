package com.example.test.service;

import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class DctService {

    public final String FILE_PATH_PRE;

    public DctService() {
        ApplicationHome home = new ApplicationHome(getClass());
        File jarFile = home.getSource();
        if (jarFile!=null){
            FILE_PATH_PRE =  jarFile.getParentFile().getAbsolutePath()+"/img_dct/";
        } else {
            FILE_PATH_PRE =  "/img_dct/";
        }
    }

    public byte[] saveAndTransformImage(MultipartFile image, String text) throws IOException {
        String srcPath = FILE_PATH_PRE + "src/";
        String originalFileName = image.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String newFileName = UUID.randomUUID() + "." + fileExtension;

        File file = new File(srcPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(srcPath + newFileName);
        image.transferTo(file);

       return null;
    }

    public byte[] saveAndIdftImage(MultipartFile image) throws IOException {
        String path = FILE_PATH_PRE + "idct/";
        String originalFileName = image.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String newFileName = UUID.randomUUID() + "." + fileExtension;

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(path + newFileName);
        image.transferTo(file);

        return null;
    }

}
