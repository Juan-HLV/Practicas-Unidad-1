package com.example.exame_u3.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exame_u3.R
import com.example.exame_u3.databinding.FragmentFavoritesBinding
import com.example.exame_u3.ui.adapter.FavoritesAdapter
import com.example.exame_u3.ui.viewmodel.PokemonViewModel

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val viewModel: PokemonViewModel by activityViewModels()
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var adapter: FavoritesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritesBinding.bind(view)

        adapter = FavoritesAdapter(
            onItemClick = { favorite ->
                 val action = FavoritesFragmentDirections.actionFavoritesFragmentToDetailFragment(favorite.name)
                 findNavController().navigate(action)
            },
            onDeleteClick = { favorite ->
                viewModel.removeFromFavorites(favorite)
            }
        )
        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorites.adapter = adapter

        viewModel.allFavorites.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.tvEmptyFavorites.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}
