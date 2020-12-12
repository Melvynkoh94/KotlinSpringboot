package com.assessment.kotlinspringboot.controller

import com.assessment.kotlinspringboot.model.Transaction
import com.assessment.kotlinspringboot.service.FileStorage
import com.assessment.kotlinspringboot.service.Util.csvObjectMapper
import com.assessment.kotlinspringboot.service.Util.startAndEndIndex
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class RetailerIndexController {
    @Autowired
    lateinit var fileStorage: FileStorage

    val log = LoggerFactory.getLogger(this::class.java)

    /*
     * Retrieve transaction infos
     */
    @GetMapping("/index")
    fun getAllList(model: Model): String {
        val transactionList = csvObjectMapper()
        model.addAttribute("files", transactionList)
        return "index.html"
    }

    @GetMapping("/index/{page}/{size}")
    fun getPaginatedList(model: Model, @PathVariable page: Int, @PathVariable size: Int): String{
        val transactionList = csvObjectMapper()
        val (startIndex, endIndex) = startAndEndIndex(page, size)
        val transactionSubList = transactionList.subList(startIndex, endIndex)

        model.addAttribute("count", transactionList.size)
        model.addAttribute("files", transactionSubList)
        log.info("Paginated list: {}", transactionSubList.forEach { println(it.toString()) })

        return "index.html"
    }

    @GetMapping("/files/{filename}")
    fun downloadFile(@PathVariable filename: String): ResponseEntity<Resource> {
        val file = fileStorage.loadFile()
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}