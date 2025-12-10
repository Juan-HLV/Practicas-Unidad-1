package com.example.exame_u3.data.repository

import androidx.lifecycle.LiveData
import com.example.exame_u3.data.local.FavoritePokemonEntity
import com.example.exame_u3.data.local.PokemonDao
import com.example.exame_u3.data.model.PokemonDetail
import com.example.exame_u3.data.model.PokemonListResponse
import com.example.exame_u3.data.remote.PokeApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonRepository(private val pokemonDao: PokemonDao) {

    private val apiService: PokeApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApiService::class.java)
    }

    suspend fun getPokemonList(limit: Int, offset: Int): PokemonListResponse {
        return apiService.getPokemonList(limit, offset)
    }

    suspend fun getPokemonDetail(nameOrId: String): PokemonDetail {
        return try {
            val id = nameOrId.toInt()
            apiService.getPokemonDetailById(id)
        } catch (e: NumberFormatException) {
            apiService.getPokemonDetail(nameOrId)
        }
    }

    val allFavorites: LiveData<List<FavoritePokemonEntity>> = pokemonDao.getAllFavorites()

    suspend fun addFavorite(pokemon: FavoritePokemonEntity) {
        pokemonDao.insertFavorite(pokemon)
    }

    suspend fun removeFavorite(pokemon: FavoritePokemonEntity) {
        pokemonDao.deleteFavorite(pokemon)
    }

    suspend fun isFavorite(id: Int): Boolean {
        return pokemonDao.isFavorite(id)
    }
}
