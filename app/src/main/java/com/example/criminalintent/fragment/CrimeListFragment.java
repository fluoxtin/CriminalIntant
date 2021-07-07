package com.example.criminalintent.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.criminalintent.CrimeListActivity;
import com.example.criminalintent.CrimePagerActivity;
import com.example.criminalintent.R;
import com.example.criminalintent.SingletonContext;
import com.example.criminalintent.adapter.CrimeListAdapter;
import com.example.criminalintent.adapter.MyPageListAdapter;
import com.example.criminalintent.data.Crime;
import com.example.criminalintent.databinding.FragmentCrimeListBinding;
import com.example.criminalintent.sqlite.CrimeBaseHelper;
import com.example.criminalintent.viewmodel.CrimeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CrimeListFragment extends Fragment {

    private static final  String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private FragmentCrimeListBinding mFragmentCrimeListBinding;
    private CrimeListAdapter mAdapter;
    private MyPageListAdapter mMyPageListAdapter;
    public CrimeViewModel mViewModel;
    private boolean mSubtitleVisible;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        new SingletonContext(getContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        mFragmentCrimeListBinding = FragmentCrimeListBinding.inflate(getLayoutInflater());

        mRecyclerView = mFragmentCrimeListBinding.crimeRecyclerView;
        mFab = mFragmentCrimeListBinding.fab;

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crime crime = new Crime();
                Intent intent = CrimePagerActivity
                        .newIntent(getActivity(), crime);
                mViewModel.insert(crime);
                startActivity(intent);
            }
        });
        updateUIWithViewModel();
        return mFragmentCrimeListBinding.getRoot();


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case    R.id.show_subtitle :
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
//                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        int crimeCount = mViewModel.getCrimes().size();
        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);
        if (!mSubtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUIWithViewModel() {
        mAdapter = new CrimeListAdapter(
                new CrimeListAdapter.CrimeDiff()
        );

        mMyPageListAdapter = new MyPageListAdapter(
                new MyPageListAdapter.CrimeDiff()
        );
//        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mViewModel = new ViewModelProvider(this).get(CrimeViewModel.class);
        mViewModel.initAllCrimes();
//        mViewModel.getListLiveData().observe(getViewLifecycleOwner(), crimes -> {
//            mAdapter.submitList(crimes);
////          mAdapter.notifyDataSetChanged();
//        });

        mRecyclerView.setAdapter(mMyPageListAdapter);
        mViewModel.mPageList.observe(getViewLifecycleOwner(), crimes -> {
            mMyPageListAdapter.submitList(crimes);
        });
    }
}
