package com.example.picbedcore.config

import com.example.picbedcore.constants.FilePathConstant
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig: WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/picBed/image/**").addResourceLocations("file:${FilePathConstant.IMAGE_PATH}")
    }
}