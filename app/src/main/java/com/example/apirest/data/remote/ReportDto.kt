package com.example.apirest.data.remote


import android.os.Build
import androidx.annotation.RequiresApi
import com.example.apirest.model.Report
import java.time.LocalDate

data class ReportDto(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val type: String = "",
    val photoUrl: String = "",
    val ownerId: String = "",
    val date: String = ""
) {
    // Convierte el DTO (datos de la API) al modelo de dominio

    @RequiresApi(Build.VERSION_CODES.O)
    fun toDomain(): Report= Report(
        id =id,
        title =title,
        description =description,
        type =type,
        photoUrl =photoUrl,
        ownerId =ownerId,
        date = runCatching { LocalDate.parse(date) }.getOrDefault(LocalDate.now())
    )

    companion object{
        fun fromDomain(report: Report): ReportDto= ReportDto(
            // Convierte el modelo de dominio al DTO que se enviará a la API
            id=report.id,
            title = report.title,
            description = report.description,
            type = report.type,
            photoUrl = report.photoUrl,
            ownerId = report.ownerId,
            date=report.date.toString()

        )
    }
}