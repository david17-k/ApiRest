package com.example.apirest.core.util


import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // Obtener un texto
    fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }

    // Obtener un texto con argumentos
    fun getString(
        @StringRes resId: Int,
        vararg formatArgs: Any
    ): String {
        return context.getString(resId, *formatArgs)
    }

    // Obtener un color
    fun getColor(@ColorRes resId: Int): Int {
        return ContextCompat.getColor(context, resId)
    }

    // Obtener una dimensión en píxeles
    fun getDimension(@DimenRes resId: Int): Float {
        return context.resources.getDimension(resId)
    }

    // Obtener un arreglo de textos
    fun getStringArray(@ArrayRes resId: Int): Array<String> {
        return context.resources.getStringArray(resId)
    }

    // Obtener un texto plural
    fun getQuantityString(
        @PluralsRes resId: Int,
        quantity: Int
    ): String {
        return context.resources.getQuantityString(resId, quantity)
    }
}