package com.example.exame_u3.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.exame_u3.R
import com.example.exame_u3.data.local.FavoritePokemonEntity

class FavoritesAdapter(
    private val onItemClick: (FavoritePokemonEntity) -> Unit,
    private val onDeleteClick: (FavoritePokemonEntity) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    private var favoritesList = listOf<FavoritePokemonEntity>()

    fun submitList(list: List<FavoritePokemonEntity>) {
        favoritesList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favoritesList[position])
    }

    override fun getItemCount(): Int = favoritesList.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPokemon: ImageView = itemView.findViewById(R.id.ivPokemon)
        private val tvName: TextView = itemView.findViewById(R.id.tvPokemonName)
        private val ivFavorite: ImageView = itemView.findViewById(R.id.ivFavorite)

        fun bind(pokemon: FavoritePokemonEntity) {
            tvName.text = pokemon.name
            Glide.with(itemView.context)
                .load(pokemon.spriteUrl)
                .into(ivPokemon)

            // In Favorites screen, this icon acts as delete/trash
            ivFavorite.setImageResource(android.R.drawable.ic_menu_delete)
            ivFavorite.visibility = View.VISIBLE
            ivFavorite.setOnClickListener {
                onDeleteClick(pokemon)
            }

            itemView.setOnClickListener {
                onItemClick(pokemon)
            }
        }
    }
}
