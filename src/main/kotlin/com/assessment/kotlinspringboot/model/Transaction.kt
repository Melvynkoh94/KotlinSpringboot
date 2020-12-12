package com.assessment.kotlinspringboot.model

import com.opencsv.bean.CsvBindByName

data class Transaction(
    @CsvBindByName
    val InvoiceNo: String? = null,

    @CsvBindByName
    val StockCode: String = "",

    @CsvBindByName
    val Description: String = "",

    @CsvBindByName
    val Quantity: Int? = null,

    @CsvBindByName
    val InvoiceDate: String? = null,

    @CsvBindByName
    val UnitPrice: Double? = null,

    @CsvBindByName
    val CustomerID: Int? = null,

    @CsvBindByName
    val Country: String? = null
)
