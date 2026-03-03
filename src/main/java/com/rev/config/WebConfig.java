package com.rev.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Expose C:/revplay/music/ as /music/ in the application
        String musicDir = "C:/revplay/music/";
        Path path = Paths.get(musicDir);
        String absolutePath = path.toFile().getAbsolutePath();

        registry.addResourceHandler("/music/**")
                .addResourceLocations("file:/" + absolutePath + "/");

        // Expose C:/revplay/images/ as /images/ in the application
        String imagesDir = "C:/revplay/images/";
        Path imgPath = Paths.get(imagesDir);
        String imgAbsolutePath = imgPath.toFile().getAbsolutePath();

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/" + imgAbsolutePath + "/", "classpath:/static/images/");
    }
}
