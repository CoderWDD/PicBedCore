package com.example.picbedcore.controller

import com.example.picbedcore.constants.FilePathConstant
import com.example.picbedcore.constants.TokenConstants
import com.example.picbedcore.dto.PagesDTO
import com.example.picbedcore.util.DTOUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.UUID
import javax.servlet.http.HttpServletRequest

@RestController
class PicController {
    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, String>

    @Autowired
    lateinit var request: HttpServletRequest
    // 上传图片
    @PostMapping("/images/upload")
    fun uploadImages(@RequestBody multipartFiles: List<MultipartFile>): List<String>{
        if (!checkToken(request, redisTemplate)) throw IllegalArgumentException("Token is Invalid")
        if (multipartFiles.isEmpty()) throw IllegalArgumentException("Image list is empty")
        val filePath = FilePathConstant.IMAGE_PATH
        File(filePath).mkdir()
        val imageUrlList = mutableListOf<String>()
        multipartFiles.forEach{
            val fileName = UUID.randomUUID().toString() + "-" + it.originalFilename
            val file = File(filePath + fileName)
            file.writeBytes(it.bytes)
            imageUrlList.add(fileName)
        }
        return imageUrlList
    }

    // 获取图片列表
    @GetMapping("/list/{page}/{size}")
    fun getImageList(@PathVariable(name = "page") page: Int, @PathVariable(name = "size") size: Int): PagesDTO<String> {
        if (!checkToken(request, redisTemplate)) throw IllegalArgumentException("Token is Invalid")
        if (page < 1) throw IllegalArgumentException("Page should not be less than 1")
        if (size < 1) throw IllegalArgumentException("Size should not be less than 1")
        val file = File(FilePathConstant.IMAGE_PATH)
        // 获取文件夹下的所有文件列表
        val files = file.listFiles()?.asList() ?: throw IllegalArgumentException("No images")
        // 按时间顺序排序
        val sortFiles = files.sortedWith { file1, file2 ->
            val diff = file1.lastModified() - file2.lastModified()
            when {
                diff > 0L -> 1
                diff == 0L -> 0
                else -> -1
            }
        }
        val pathList = mutableListOf<String>()
        sortFiles.forEach { pathList.add(it.path) }
        return DTOUtil.listToPageDTO(pathList, page, size)
    }

    @PostMapping("/images/delete/{imagePath}")
    fun deleteImage(@PathVariable(value = "imagePath") imagePath: String): String{
        if (!checkToken(request, redisTemplate)) throw IllegalArgumentException("Token is Invalid")
        if (imagePath.isEmpty()) throw IllegalArgumentException("Image Path should not be empty")
        val file = File(imagePath)
        if (!file.isFile) throw IllegalArgumentException("Image Path wrong")
        file.delete()
        return "Delete Success"
    }

    private fun checkToken(request: HttpServletRequest, redisTemplate: RedisTemplate<String,String>): Boolean{
        var token = request.getHeader("Authorization") ?: return false
        if (token.startsWith("Bearer ")) {
            token = token.substring(7)
        }
        val remoteToken = redisTemplate.opsForValue().get(TokenConstants.TOKEN_KEY)
        return token == remoteToken
    }
}