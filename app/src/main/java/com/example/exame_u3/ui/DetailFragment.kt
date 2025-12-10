package com.example.exame_u3.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.exame_u3.R
import com.example.exame_u3.databinding.FragmentDetailBinding
import com.example.exame_u3.ui.viewmodel.PokemonViewModel

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val viewModel: PokemonViewModel by activityViewModels()
    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)

        val pokemonName = args.pokemonName
        viewModel.loadPokemonDetail(pokemonName)
        
        viewModel.selectedPokemon.observe(viewLifecycleOwner) { detail ->
            binding.tvDetailName.text = detail.name
            binding.tvDetailHeight.text = "Height: ${detail.height}"
            binding.tvDetailWeight.text = "Weight: ${detail.weight}"
            binding.tvDetailTypes.text = "Types: ${detail.types.joinToString { it.type.name }}"
            
            Glide.with(requireContext())
                .load(detail.sprites.frontDefault)
                .into(binding.ivDetailSprite)
                
            updateFavoriteIcon(detail.id)

            binding.btnFavorite.setOnClickListener {
               viewModel.toggleFavorite(detail)
            }
        }
        
        viewModel.allFavorites.observe(viewLifecycleOwner) { favorites ->
             viewModel.selectedPokemon.value?.let { current ->
                 val isFav = favorites.any { it.id == current.id }
                 binding.btnFavorite.setImageResource(
                     if (isFav) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
                 )
             }
        }
    }
    
    private fun updateFavoriteIcon(id: Int) {
          // Handled by allFavorites observer
    }
}
