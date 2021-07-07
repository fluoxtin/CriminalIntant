package com.example.criminalintent.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.criminalintent.CrimeListActivity;
import com.example.criminalintent.R;
import com.example.criminalintent.SingletonContext;
import com.example.criminalintent.data.Crime;
import com.example.criminalintent.databinding.FragmentCrimeBinding;
import com.example.criminalintent.viewmodel.CrimeViewModel;

import java.util.Date;

import static android.widget.CompoundButton.*;

public class CrimeFragment extends Fragment {                 //与模型及视图对象交互的控制器，用于显示特
                                                              //定模型Crime的明细信息，并在用户修改后立
                                                              //即更新
    private static final String ARG_CRIME = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;                 //创建请求代码常量
    private static final int REQUEST_CONTACT = 1;

    private FragmentCrimeBinding mFragmentCrimeBinding;
    private boolean isTitleChanged;

    private Crime mCrime;
    public static CrimeFragment newInstance(Crime crime) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME, crime);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {                      //只是创建了一个fragment实例
        super.onCreate(savedInstanceState);                                //并没有创建fragment视图
        mCrime = (Crime) getArguments().getSerializable(ARG_CRIME);

    }


    //实例化fragment视图的布局，然后将实例化的View返回给托管activity。
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFragmentCrimeBinding = FragmentCrimeBinding.inflate(getLayoutInflater());
        isTitleChanged = false;

        final EditText crimeTitle = mFragmentCrimeBinding.crimeTitle;
        final Button crimeDate = mFragmentCrimeBinding.crimeDate;
        final CheckBox crimeSolved = mFragmentCrimeBinding.crimeSolved;
        final Button crimeSuspect = mFragmentCrimeBinding.crimeSuspect;
        final Button crimeReport = mFragmentCrimeBinding.crimeReport;
        final Button crimeSubmit = mFragmentCrimeBinding.crimeSubmit;

        crimeTitle.setText(mCrime.getTitle());
        crimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //This space intentionally left blank
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //调用CharSequence(代表用户输入).toString()方法添加title
                mCrime.setTitle(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                //
                isTitleChanged = true;
            }
        });

        crimeDate.setText(mCrime.getDate().toString());

        crimeDate.setEnabled(false);    //禁用日期按钮
        crimeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
//                DatePickerFragment dialog = new DatePickerFragment();
                //跳转到DatePickerFragment
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                //将CrimeFragment设为DatePickerFragment实例的目标Fragment
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        crimeSolved.setChecked(mCrime.isSolved());
        crimeSolved.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        crimeReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                        .putExtra(Intent.EXTRA_SUBJECT,
                                getString(R.string.crime_report_subject));
                intent = Intent.createChooser(intent, getString(R.string.send_report));
                startActivity(intent);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);

        crimeSuspect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null) {
            crimeSuspect.setText(mCrime.getSuspect());
        }
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager
                .resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            crimeSuspect.setEnabled(false);
        }

        // update data
        crimeSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo if info changed then saved else not saved
//                CrimeLab.get(getActivity()).updateCrime(mCrime);
                CrimeViewModel crimeViewModel =
                        new ViewModelProvider((ViewModelStoreOwner) SingletonContext.sContext)
                        .get(CrimeViewModel.class);
                crimeViewModel.update(mCrime);
                if (isTitleChanged) {
                    Toast.makeText(getActivity(), "The title has changed !", Toast.LENGTH_SHORT)
                            .show();
                    isTitleChanged = false;
                } else {
                    Toast.makeText(getActivity(),
                            "Hey, guy! There is nothing changed !",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        return  mFragmentCrimeBinding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {              //请求代码一致 则获取数据并更新
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            mFragmentCrimeBinding.crimeDate.setText(mCrime.getDate().toString());
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME
            };

            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);
            try {
                if (c.getCount() == 0) {
                    return;
                }
                 c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mFragmentCrimeBinding.crimeSuspect.setText(suspect);
            } finally {
                c.close();
            }
        }
    }
//
//    private void updateDate() {
//        mFragmentCrimeBinding.crimeDate.setText(mCrime.getDate().toString());
//    }

    private String getCrimeReport() {
        String solvedString;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_no_suspect);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect,  suspect);
        }

        String report = getString(R.string.crime_report,
                mCrime.getTitle(),
                dateString,
                solvedString,
                suspect);

        return report;
    }
}
