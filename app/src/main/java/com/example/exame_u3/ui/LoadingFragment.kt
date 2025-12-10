package com.example.exame_u3.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.exame_u3.R
import com.example.exame_u3.databinding.FragmentLoadingBinding
import com.example.exame_u3.ui.viewmodel.PokemonViewModel

class LoadingFragment : Fragment(R.layout.fragment_loading) {

    private val viewModel: PokemonViewModel by activityViewModels() // Share ViewModel
    private lateinit var binding: FragmentLoadingBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoadingBinding.bind(view)

        setupObservers()
        // Trigger initial load
        viewModel.loadInitialPokemon()
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnRetry.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                binding.tvLoading.text = error
                binding.btnRetry.visibility = View.VISIBLE
            } else {
                 binding.tvLoading.text = "Loading..."
                 binding.btnRetry.visibility = View.GONE
            }
        }

        viewModel.pokemonList.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                // Navigate to List
                 findNavController().navigate(R.id.action_loadingFragment_to_listFragment)
            }
        }
        
        binding.btnRetry.setOnClickListener {
            viewModel.loadInitialPokemon()
        }
    }
}
