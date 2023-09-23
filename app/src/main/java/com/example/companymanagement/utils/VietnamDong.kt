package com.example.companymanagement.utils

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class VietnamDong (var amount: BigDecimal){

    private val currency : String

    private val vnLocale = Locale("vi", "VN")
    private val vnCurrency = NumberFormat.getCurrencyInstance(vnLocale)

    init {
        currency = vnCurrency.format(amount)
    }

    override fun toString(): String {
        return currency
    }

}