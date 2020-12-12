package com.assessment.kotlinspringboot.controller

import com.assessment.kotlinspringboot.model.Transaction
import com.assessment.kotlinspringboot.utils.FileStorage
import com.assessment.kotlinspringboot.utils.Util.csvFileMapper
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

    /*
     * Retrieve transaction infos
     */
    @ExperimentalStdlibApi
    @GetMapping("/index")
    fun getListFiles(model: Model): String {

        val transactionList: List<Transaction> = csvFileMapper(fileStorage.loadFile().file)

        model.addAttribute("files", transactionList)
        return "index.html"
    }

    /*
     * Download Files
     */
    @GetMapping("/files/{filename}")
    fun downloadFile(@PathVariable filename: String): ResponseEntity<Resource> {
        val file = fileStorage.loadFile()
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}