package com.example.criminalintent.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import com.example.criminalintent.data.Crime;
import com.example.criminalintent.databinding.ListItemCrimeBinding;
import com.example.criminalintent.viewholder.CrimeViewHolder;

public class CrimeListAdapter extends ListAdapter<Crime, CrimeViewHolder> {
    private CrimeViewHolder mCrimeViewHolder;


    public CrimeListAdapter(@NonNull DiffUtil.ItemCallback<Crime> diffCallback) {
        super(diffCallback);
    }

    @Override
    public CrimeViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mCrimeViewHolder =  new CrimeViewHolder(
                ListItemCrimeBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent,
                        false),
                parent.getContext()
        );
        return mCrimeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CrimeViewHolder holder, int position) {
        Crime crime = getItem(position);
        holder.bind(crime, position);
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
