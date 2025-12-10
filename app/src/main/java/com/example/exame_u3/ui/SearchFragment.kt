package com.example.exame_u3.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.exame_u3.R
import com.example.exame_u3.data.model.PokemonDetail
import com.example.exame_u3.data.model.PokemonResult
import com.example.exame_u3.databinding.FragmentSearchBinding
import com.example.exame_u3.ui.viewmodel.PokemonViewModel

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel: PokemonViewModel by activityViewModels()
    private lateinit var binding: FragmentSearchBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString()
            if (query.isNotEmpty()) {
                viewModel.searchPokemon(query)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarSearch.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.cardResult.root.visibility = View.GONE 
            binding.tvErrorSearch.visibility = View.GONE
        }
        
         viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
             if (error != null && !binding.progressBarSearch.isShown) {
                 binding.tvErrorSearch.text = error
                 binding.tvErrorSearch.visibility = View.VISIBLE
             }
         }

        viewModel.searchResult.observe(viewLifecycleOwner) { detail ->
            if (detail != null) {
                binding.cardResult.root.visibility = View.VISIBLE
                val tvName = binding.cardResult.root.findViewById<TextView>(R.id.tvPokemonName)
                val ivImg = binding.cardResult.root.findViewById<ImageView>(R.id.ivPokemon)
                
                tvName.text = detail.name
                 Glide.with(requireContext())
                    .load(detail.sprites.frontDefault)
                    .into(ivImg)
                    
                binding.cardResult.root.setOnClickListener {
                     val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(detail.name)
                     findNavController().navigate(action)
                }
            }
        }
    }
}
