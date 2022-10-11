package com.example.picbedcore

import com.example.picbedcore.controller.PicController
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class PicBedCoreApplication
fun main(args: Array<String>) {
    runApplication<PicBedCoreApplication>(*args)
}
