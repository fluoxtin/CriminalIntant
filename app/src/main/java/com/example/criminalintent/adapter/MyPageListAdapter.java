package com.example.criminalintent.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.example.criminalintent.data.Crime;
import com.example.criminalintent.databinding.ListItemCrimeBinding;
import com.example.criminalintent.viewholder.CrimeViewHolder;

public class MyPageListAdapter extends PagedListAdapter<Crime, CrimeViewHolder> {

    public MyPageListAdapter(@NonNull DiffUtil.ItemCallback<Crime> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public CrimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CrimeViewHolder(
                ListItemCrimeBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent,
                        false),
                parent.getContext()
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CrimeViewHolder holder, int position) {
        holder.bind(getItem(position), position);
    }

    public static class CrimeDiff extends DiffUtil.ItemCallback<Crime> {

        @Override
        public boolean areItemsTheSame(@NonNull Crime oldItem, Crime newItem) {
            return oldItem.equals(newItem);
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Crime oldItem, @NonNull Crime newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
    }
}
