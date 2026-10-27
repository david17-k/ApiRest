package com.example.apirest.service

import com.example.apirest.data.remote.ReportDto
import com.example.apirest.model.Report
import retrofit2.http.GET

interface ApiService {
    @GET("reports")
    suspend fun getReports(): List<ReportDto>
}
