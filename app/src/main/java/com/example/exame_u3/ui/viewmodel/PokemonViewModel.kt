package com.example.exame_u3.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.exame_u3.data.local.AppDatabase
import com.example.exame_u3.data.local.FavoritePokemonEntity
import com.example.exame_u3.data.model.PokemonDetail
import com.example.exame_u3.data.model.PokemonResult
import com.example.exame_u3.data.repository.PokemonRepository
import kotlinx.coroutines.launch

class PokemonViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PokemonRepository
    
    init {
        val messageDao = AppDatabase.getDatabase(application).pokemonDao()
        repository = PokemonRepository(messageDao)
    }

    // List
    private val _pokemonList = MutableLiveData<List<PokemonResult>>()
    val pokemonList: LiveData<List<PokemonResult>> = _pokemonList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Search
    private val _searchResult = MutableLiveData<PokemonDetail?>()
    val searchResult: LiveData<PokemonDetail?> = _searchResult

    // Detail
    private val _selectedPokemon = MutableLiveData<PokemonDetail>()
    val selectedPokemon: LiveData<PokemonDetail> = _selectedPokemon

    // Favorites
    val allFavorites: LiveData<List<FavoritePokemonEntity>> = repository.allFavorites

    fun loadInitialPokemon() {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                val response = repository.getPokemonList(limit = 50, offset = 0)
                _pokemonList.value = response.results
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Error loading Pokemon: ${e.message}"
            }
        }
    }

    fun searchPokemon(name: String) {
        _isLoading.value = true
        _errorMessage.value = null
        _searchResult.value = null
        viewModelScope.launch {
            try {
                val detail = repository.getPokemonDetail(name.lowercase().trim())
                _searchResult.value = detail
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Pokemon not found or error: ${e.message}"
            }
        }
    }
    
    fun loadPokemonDetail(nameOrId: String) {
        _isLoading.value = true
         viewModelScope.launch {
            try {
                val detail = repository.getPokemonDetail(nameOrId)
                _selectedPokemon.value = detail
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Error loading details: ${e.message}"
            }
        }
    }

    fun toggleFavorite(detail: PokemonDetail) {
        viewModelScope.launch {
            if (repository.isFavorite(detail.id)) {
                repository.removeFavorite(FavoritePokemonEntity(detail.id, detail.name, detail.sprites.frontDefault ?: ""))
            } else {
                repository.addFavorite(FavoritePokemonEntity(detail.id, detail.name, detail.sprites.frontDefault ?: ""))
            }
            // Force refresh of isFavorite check if needed, but LiveData usually handles lists. 
            // For single item checking, we might need a separate mechanism or just observe the list.
        }
    }
    
    fun removeFromFavorites(entity: FavoritePokemonEntity) {
        viewModelScope.launch {
            repository.removeFavorite(entity)
        }
    }
    
    // Helper to check if a specific ID is in the Favorites list
    fun isFavorite(id: Int, favorites: List<FavoritePokemonEntity>?): Boolean {
        return favorites?.any { it.id == id } == true
    }
}
