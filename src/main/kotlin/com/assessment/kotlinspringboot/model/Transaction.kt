package com.assessment.kotlinspringboot.model

data class Transaction(val InvoiceNo: Int = 0,
                       val StockCode: String = "",
                       val Description: String = "",
                       val Quantity: Int? = null,
                       val InvoiceDate: String? = null,
                       val UnitPrice: Double? = null,
                       val CustomerID: Int? = null,
                       val Country: String? = null)
