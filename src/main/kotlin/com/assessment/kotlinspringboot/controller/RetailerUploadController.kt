package com.assessment.kotlinspringboot.controller

import com.assessment.kotlinspringboot.model.Transaction
import com.assessment.kotlinspringboot.service.FileStorage
import com.assessment.kotlinspringboot.service.RetailerService
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.vhl.blackmo.grass.dsl.grass
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.time.Duration
import java.time.LocalTime
import reactor.core.publisher.Flux

@Controller
class RetailerUploadController {

    @Autowired
    lateinit var fileStorage: FileStorage

    @Autowired
    lateinit var service: RetailerService

    val logger: Logger = LoggerFactory.getLogger(RetailerUploadController::class.java)

    @GetMapping("/upload")
    fun index(): String {
        return "uploadFile.html"
    }

    @GetMapping(path = ["/flux"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun streamFlux() : Flux<String> {
        return Flux.interval(Duration.ofSeconds(1))
                .map { elements -> "In Progress at "+LocalTime.now().toString() }
    }

    @PostMapping("/upload")
    fun uploadMultipartFile(@RequestParam("uploadfile") file: MultipartFile, model: Model): String {
        fileStorage.store(file);
        model.addAttribute("message", file.getOriginalFilename() + " uploaded successfully!")
        model.addAttribute("size", file.size)
        logger.info("File uploaded: {} - {} bytes", file.originalFilename, file.size)
        return "uploadFile.html"
    }

    @PostMapping("/fileupload")
    fun handleFileUpload(@RequestParam("file") file: MultipartFile): ServiceResponse {
        logger.info("handling fileupload for {}", file.name)
        val content = file.inputStream.bufferedReader().use(BufferedReader::readText)
        val transactionList = service.csvObjectMapper()

        logger.info("Total number of lines: " + content[0])
        logger.info("file content = {}", transactionList)
        return ServiceResponse.ok(content)
    }

    @GetMapping("/")
    fun home(model: Model): String {
        model["title"] = "WELCOME TO KOTLIN BACKEND SERVICES!!" //alt to java model.addAttribute("title","Homepage")
        return "home"
    }

    @GetMapping("/uploadFile")
    fun uploadFile(model: Model): String {
        model["title"] = "WELCOME TO KOTLIN BACKEND SERVICES!!" //alt to java model.addAttribute("title","Homepage")
        model["upload"] = "Upload" //alt to java model.addAttribute("title","Homepage")
        logger.info("Upload File page loaded...")
        return "upload"
    }
}

data class ServiceResponse(val isSuccess: Boolean,
                           val data: String? = null) {

    companion object {
        fun ok(data: String): ServiceResponse {
            return ServiceResponse(isSuccess = true, data = data)
        }

        fun fail(): ServiceResponse {
            return ServiceResponse(isSuccess = false)
        }
    }
}