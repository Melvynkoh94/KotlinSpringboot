package com.assessment.kotlinspringboot.service

import com.assessment.kotlinspringboot.model.Transaction
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.opencsv.bean.CsvToBean
import com.opencsv.bean.CsvToBeanBuilder
import com.vhl.blackmo.grass.dsl.grass
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.Exception


object Util {
    @ExperimentalStdlibApi
    fun csvFileMapper(fileInput: File): List<Transaction> {
        val csvContents = csvReader().readAllWithHeader(fileInput.inputStream())
        return grass<Transaction>().harvest(csvContents)
    }

    fun csvObjectMapper(): List<Transaction>{
        var fileReader: BufferedReader? = null
        var csvToBean: CsvToBean<Transaction>?
        var transactions: List<Transaction>? = null
        try{
            fileReader = BufferedReader(FileReader("uploadedDatafile/data.csv"))
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

//    fun csvObjectMapper(reader: BufferedReader?): List<Transaction>? {
//        var csvToBean: CsvToBean<Transaction>
//        var transactionList: List<Transaction>? = null
//        csvToBean = CsvToBeanBuilder<Transaction>(reader)
//                .withType(Transaction::class.java)
//                .withIgnoreLeadingWhiteSpace(true)
//                .build()
//
//        transactionList = csvToBean.parse()
//
//        return transactionList
//    }

}