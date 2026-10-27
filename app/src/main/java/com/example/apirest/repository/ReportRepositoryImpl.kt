package com.example.apirest.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.apirest.model.Report
import com.example.apirest.service.ApiService
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val apiService: ApiService

): ReportRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getReports(): List<Report> {
        return apiService.getReports().map { it.toDomain() }
    }
}