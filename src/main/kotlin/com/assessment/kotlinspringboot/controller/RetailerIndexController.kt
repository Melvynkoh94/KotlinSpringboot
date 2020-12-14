package com.assessment.kotlinspringboot.controller

import com.assessment.kotlinspringboot.model.Transaction
import com.assessment.kotlinspringboot.service.FileStorage
import com.assessment.kotlinspringboot.service.RetailerService
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

    @Autowired
    lateinit var service: RetailerService

    val log = LoggerFactory.getLogger(this::class.java)

    /*
     * Retrieve transaction infos
     */
    @GetMapping("/index")
    fun getAllList(model: Model): String {
        val transactionList = service.csvObjectMapper()
        model.addAttribute("files", transactionList)
        return "index.html"
    }

    @GetMapping("/index/{page}/{size}/next")
    fun getNextPageList(@PathVariable page: Int, @PathVariable size: Int): String{
        val newPage: Int = page.inc()
        return "redirect:/index/$newPage/$size"
    }

    @GetMapping("/index/{page}/{size}/prev")
    fun getPrevPageList(@PathVariable page: Int, @PathVariable size: Int): String{
        val newPage: Int = page.dec()
        if(page == 1) return "redirect:/index/$page/$size"
        return "redirect:/index/$newPage/$size"
    }

    @GetMapping("/index/{page}/{size}")
    fun getPaginatedList(model: Model, @PathVariable page: Int, @PathVariable size: Int): String{
        val transactionList = service.csvObjectMapper()
        val (startIndex, endIndex) = service.startAndEndIndex(page, size)
        val transactionSubList = transactionList.subList(startIndex, endIndex)

        model.addAttribute("currentPage", page)
        model.addAttribute("size", size)
        model.addAttribute("files", transactionSubList)
        log.info("Paginated list: {}", transactionSubList.forEach { println(it.toString()) })

        return "index.html"
    }

    @GetMapping("/index/search/{column}/{search}")
    fun searchList(model: Model, @PathVariable column: String, @PathVariable search: String): String{
        val transactionList = service.csvObjectMapper()
        val searchResult: List<Transaction> = when (column.toLowerCase().trim()) {
            "invoiceno" -> transactionList.filter { item -> search.equals(item.InvoiceNo, ignoreCase = true)}
            "stockcode" -> transactionList.filter { item -> search.equals(item.StockCode, ignoreCase = true)}
            "description" -> transactionList.filter { item -> search.equals(item.Description, ignoreCase = true)}
            "invoicedate" -> transactionList.filter { item -> search == item.InvoiceDate }
            "customerid" -> transactionList.filter { item -> search.toIntOrNull() == item.CustomerID }
            "country" -> transactionList.filter { item -> search.equals(item.Country, ignoreCase = true) }
            else -> transactionList.filter { item -> search.equals(item.InvoiceNo, ignoreCase = true)}
        }

        model.addAttribute("files", searchResult)
        log.info("Search results: {}", searchResult.forEach { println(it.toString()) })
        return "index.html"
    }

    @GetMapping("/index/{page}/{size}/download")
    fun downloadFile(@PathVariable filename: String): ResponseEntity<Resource> {
        val file = fileStorage.loadFile()
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}