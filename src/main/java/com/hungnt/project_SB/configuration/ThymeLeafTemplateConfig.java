package com.hungnt.project_SB.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class ThymeLeafTemplateConfig {
    @Bean
    public ITemplateResolver iTemplateResolver(){
        // Duong dan va thuoc tinh cho template HTML
        ClassLoaderTemplateResolver classLoaderTemplateResolver = new ClassLoaderTemplateResolver();
        classLoaderTemplateResolver.setPrefix("/templates/");
        classLoaderTemplateResolver.setSuffix(".html");
        classLoaderTemplateResolver.setTemplateMode("HTML");
        classLoaderTemplateResolver.setCharacterEncoding("UTF-8");
        return classLoaderTemplateResolver;
    }

    @Bean
    public SpringTemplateEngine springTemplateEngine(ITemplateResolver iTemplateResolver){
        // Su dung iTemplateResolver khoi tao va cau hinh SpringTemplateEngine
        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        springTemplateEngine.setTemplateResolver(iTemplateResolver);
        return springTemplateEngine;
    }
}
