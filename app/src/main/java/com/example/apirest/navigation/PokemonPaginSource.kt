package com.example.apirest.navigation

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.apirest.data.remote.PokemonDto
import com.example.apirest.service.PokemonApi

class PokemonPaginSource(  private val api: PokemonApi): PagingSource<Int, PokemonDto>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, PokemonDto> {

        return try {

            val offset= params.key ?:0

            val response=api.getPokemon(
                limit = params.loadSize,
                offset=offset
            )
            LoadResult.Page(
                data =response.results,
                prevKey = null,
                nextKey = offset+params.loadSize
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(
        state: PagingState<Int, PokemonDto>): Int?=null






}