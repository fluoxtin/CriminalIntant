package com.example.criminalintent.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.criminalintent.data.Crime;
import com.example.criminalintent.CrimePagerActivity;
import com.example.criminalintent.fragment.CrimeListFragment;
import com.example.criminalintent.databinding.ListItemCrimeBinding;

public class CrimeViewHolder extends RecyclerView.ViewHolder{

    private ListItemCrimeBinding mItemCrimeBinding;

    private Context mContext;
    private Crime mCrime;

    //实例化组件
    public CrimeViewHolder(ListItemCrimeBinding binding, Context context) {
        super(binding.getRoot());
        mContext = context;
        mItemCrimeBinding = binding;
        itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = CrimePagerActivity.newIntent(mContext, mCrime);
                mContext.startActivity(intent);
            }
        });                  // itemView是ViewHolder中的对象
    }
    public void bind(Crime crime, int position) {
        mCrime = crime;
//        mItemCrimeBinding.crimeSolved.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        mItemCrimeBinding.setCrime(mCrime);
    }

}
