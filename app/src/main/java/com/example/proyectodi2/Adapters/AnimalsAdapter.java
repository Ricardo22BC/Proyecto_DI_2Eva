package com.example.proyectodi2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectodi2.R;
import com.example.proyectodi2.databinding.ItemLayoutBinding;
import com.example.proyectodi2.models.Animals;
import com.example.proyectodi2.views.DashboardActivity;

import java.util.ArrayList;
import java.util.List;

public class AnimalsAdapter extends RecyclerView.Adapter<AnimalsAdapter.AnimalViewHolder> {
    private List<Animals> animalsList;
    private OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(Animals animal);
    }

    public AnimalsAdapter(OnItemClickListener listener) {
        this.animalsList = new ArrayList<>();
        this.listener = listener;
    }

    public void updateList(List<Animals> newList) {
        this.animalsList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLayoutBinding binding = ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new AnimalViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        Animals animal = animalsList.get(position);
        holder.binding.setAnimals(animal);
        // Añadir contentDescription
        holder.binding.imageView.setContentDescription("Imagen de " + animal.getTitle());
        // Cargar imagen usando Glide
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

        return animalsList != null ? animalsList.size() : 0;
    }

    static class AnimalViewHolder extends RecyclerView.ViewHolder {
        private final ItemLayoutBinding binding;

        public AnimalViewHolder(@NonNull ItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }
    }
}