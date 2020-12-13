package com.assessment.kotlinspringboot.service

import com.assessment.kotlinspringboot.model.Transaction
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.opencsv.bean.CsvToBean
import com.opencsv.bean.CsvToBeanBuilder
import com.vhl.blackmo.grass.dsl.grass
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.Exception
import java.nio.file.Path
import java.nio.file.Paths

@Service
@PropertySource("classpath:application.properties")
class RetailerService {

    val rootLocation: Path = Paths.get("uploadedDatafile")
    @Value("\${retail.dataFilename}") val filename: String = ""

    @ExperimentalStdlibApi
    fun csvFileMapper(fileInput: File): List<Transaction> {
        val csvContents = csvReader().readAllWithHeader(fileInput.inputStream())
        return grass<Transaction>().harvest(csvContents)
    }

    fun csvObjectMapper(): List<Transaction>{
        var fileReader: BufferedReader
        var csvToBean: CsvToBean<Transaction>?
        var transactions: List<Transaction>? = null
        try{
            fileReader = BufferedReader(FileReader(rootLocation.resolve(filename).toString()))
            csvToBean = CsvToBeanBuilder<Transaction>(fileReader)
                    .withType(Transaction::class.java)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()

            transactions = csvToBean.parse()
        } catch (e: Exception){
            println("Error reading CSV file!")
        }
        return transactions!!
    }

    fun startAndEndIndex(page: Int, size: Int): Pair<Int, Int>{
        val end: Int
        val start: Int = if (page == 1){
            end = size
            0
        } else {
            end = (page - 1) * size + size
            (page - 1) * size
        }
        return Pair(start, end)
    }

}