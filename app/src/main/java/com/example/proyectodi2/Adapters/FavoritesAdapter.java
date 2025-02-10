package com.example.proyectodi2.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectodi2.databinding.ItemLayoutBinding;
import com.example.proyectodi2.models.Animals;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {
    private List<Animals> favoritesList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Animals animal);
    }

    public FavoritesAdapter(OnItemClickListener listener) {
        this.favoritesList = new ArrayList<>();
        this.listener = listener;
    }

    public void updateList(List<Animals> newList) {
        this.favoritesList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType ) {
        ItemLayoutBinding binding = ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new FavoriteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Animals animal = favoritesList.get(position);
        holder.binding.setAnimals(animal);
        Glide.with(holder.itemView.getContext())
                .load(animal.getImageUrl())
                .into(holder.binding.imageView);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(animal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoritesList != null ? favoritesList.size() : 0;
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private final ItemLayoutBinding binding;

        public FavoriteViewHolder(@NonNull ItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
