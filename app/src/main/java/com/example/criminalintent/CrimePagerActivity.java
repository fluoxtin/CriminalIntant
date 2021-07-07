package com.example.criminalintent;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.criminalintent.container.AppContainer;
import com.example.criminalintent.data.Crime;
import com.example.criminalintent.databinding.ActivityCrimePagerBinding;
import com.example.criminalintent.fragment.CrimeFragment;
import com.example.criminalintent.viewmodel.CrimeViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


public class CrimePagerActivity extends AppCompatActivity {

    private static final String EXTRA_CRIME =
            "com.example.criminalintent.crime";

    private ActivityCrimePagerBinding mActivityCrimePagerBinding;
    private Crime mCrime;
    private LiveData<List<Crime>> mCrimes;
    private List<Crime> mList;
    private ViewPager2 viewPager2;


    public static Intent newIntent(Context packageContext, Crime crime) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME, crime);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityCrimePagerBinding = ActivityCrimePagerBinding.inflate(getLayoutInflater());
        setContentView(mActivityCrimePagerBinding.getRoot());

        viewPager2 = mActivityCrimePagerBinding.crimeViewPager;
        final CrimeViewModel model =
                new ViewModelProvider((ViewModelStoreOwner) SingletonContext.sContext)
                        .get(CrimeViewModel.class);

        mCrime = (Crime) getIntent().getSerializableExtra(EXTRA_CRIME);
        FragmentActivity fragmentActivity = CrimePagerActivity.this;
        mList = model.getCrimes();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getId().equals(mCrime.getId())) {
                        viewPager2.setCurrentItem(i, false);
                        break;
                    }
                }
            }
        });

        viewPager2.setAdapter(new FragmentStateAdapter(fragmentActivity) {
            @Override
            public int getItemCount() {
                return mList.size();       // this count can not be greater than data size
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return CrimeFragment.newInstance(mList.get(position));
            }
        });

        // todo we use list instead livedata, but it is not a good ways
//        for (int i = 0; i < mCrimes.getValue().size(); i++) {
//            if (mCrimes.getValue().get(i).getId() == mCrime.getId()){
//                mActivityCrimePagerBinding.crimeViewPager.setCurrentItem(i);
//                break;
//            }
//        }


    }
}
