package com.example.apirest.repository

import com.example.apirest.model.Report

interface ReportRepository {
    suspend fun getReports(): List<Report>
}
