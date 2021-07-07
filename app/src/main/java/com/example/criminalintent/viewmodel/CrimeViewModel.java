package com.example.criminalintent.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.criminalintent.container.AppContainer;
import com.example.criminalintent.data.Crime;
import com.example.criminalintent.db.CrimeRepository;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * If you need the application context
 * (which has a lifecycle that lives as long as the application does),
 * use AndroidViewModel
 */

public class CrimeViewModel extends AndroidViewModel {

    private List<Crime> mCrimes;
    public LiveData<List<Crime>> mListLiveData;
    private CrimeRepository mRepository;

    private MutableLiveData<Crime> mMutableLiveData = new MutableLiveData<>();
    public LiveData<PagedList<Crime>> mPageList;
    public CrimeViewModel(Application application) {
        super(application);
        mRepository = new CrimeRepository(application);
    }

    public void initAllCrimes() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setPrefetchDistance(1)          // 设置距离每页最后数据项时加载下一页
                .setInitialLoadSizeHint(10)      // 设置首次加载的数量
                .setPageSize(10)                 // 设置每页加载数量
                .setEnablePlaceholders(false)     // 是否启用UI占位符
                .build();

        mPageList = new LivePagedListBuilder<>(
                mRepository.mCrimeDao.loadAllCrime(),
                config
        ).build();
    }

    public LiveData<List<Crime>> getListLiveData() {
        if (mListLiveData == null) {
            mListLiveData =  mRepository.getListLiveData();
        }
        return mListLiveData;
    }

    public List<Crime> getCrimes(){
        if (mCrimes == null) {
            QueryRoomTask task = new QueryRoomTask();
            FutureTask<List<Crime>> futureTask = new FutureTask<>(task);
            new Thread(futureTask).start();

            try {
                mCrimes = futureTask.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return mCrimes;
    }

    public void insert(Crime crime) {
        new Thread(() -> {
            mRepository.insertCrime(crime);
        }).start();
    }

    public void delete(Crime crime) {
        new Thread(() -> {
            mRepository.deleteCrime(crime);
        }).start();
    }

    public void update(Crime crime) {
        new Thread(() -> {
           mRepository.updateCrime(crime);
        }).start();
    }

}

class QueryRoomTask implements Callable<List<Crime>> {

    @Override
    public List<Crime> call() {
//        Thread.sleep(100);
        List<Crime> list = new AppContainer().mCrimeRepository.getListCrimes();
        return list;
    }
}
