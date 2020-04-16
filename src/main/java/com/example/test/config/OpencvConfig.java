package com.example.test.config;

import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
@Configuration
public class OpencvConfig {
    @Bean
    public void init() {
        ApplicationHome home = new ApplicationHome(getClass());
        File jarFile = home.getSource();
        String path;
        if (jarFile!=null){
            path =  jarFile.getParentFile().getAbsolutePath();
        } else {
            path =  "C:\\Users\\ryan\\Desktop\\waterMarker\\src\\main\\resources\\lib";
        }
        System.load(path+"/opencv_java411.dll");
    }
}