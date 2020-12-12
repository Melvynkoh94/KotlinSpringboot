package com.assessment.kotlinspringboot.utils

import com.assessment.kotlinspringboot.model.Transaction
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.vhl.blackmo.grass.dsl.grass
import java.io.File

object Util {
    @ExperimentalStdlibApi
    fun csvFileMapper(fileInput: File): List<Transaction> {
        val csvContents = csvReader().readAllWithHeader(fileInput.inputStream())
        return grass<Transaction>().harvest(csvContents)
    }

}