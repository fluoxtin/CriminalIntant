package com.example.criminalintent;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.criminalintent.R;


public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();  //新增一个抽象方法，SingleFragmentActivity的子类
                                                   //会实现该方法，来返回由activity托管的fragment实例
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fragmentManager = getSupportFragmentManager();        //获取FragmentManager
        //使用R.id.fragment_container的容器视图资源ID，向FragmentManager请求并获取fragment。如果要获取的
        //在fragment队列中，FragmentManager就直接返回它。
        //Activity销毁时，fragment会被FragmentManager队列保存
        // 这样Activity重建时，新的FragmentManager首先获取保存的队列，然后重建fragment队列
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        //如果指定视图容器资源ID的fragment不存在，fragment变量为空值，这时新建CrimeFragment，并启动新的
        // fragment事务，将新建的fragment添加到队列中
        if (fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
