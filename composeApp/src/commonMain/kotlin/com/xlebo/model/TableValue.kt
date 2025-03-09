package com.xlebo.model

interface TableValue {

    fun getHeaders(): List<String>

    fun getWeightsForPreview(): List<Float>
}