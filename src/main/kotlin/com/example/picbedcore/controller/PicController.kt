package com.example.picbedcore.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Controller

@Controller
class PicController {
    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, String>

    
}