package com.example.exame_u3.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exame_u3.R
import com.example.exame_u3.databinding.FragmentListBinding
import com.example.exame_u3.ui.adapter.PokemonAdapter
import com.example.exame_u3.ui.viewmodel.PokemonViewModel

class ListFragment : Fragment(R.layout.fragment_list) {
    
    private val viewModel: PokemonViewModel by activityViewModels()
    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: PokemonAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = PokemonAdapter { pokemon ->
            val action = ListFragmentDirections.actionListFragmentToDetailFragment(pokemon.name)
            findNavController().navigate(action)
        }
        binding.rvPokemon.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPokemon.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.pokemonList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }
}
