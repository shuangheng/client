package com.app.demos.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.demos.R;

public class FragmentOne extends Fragment implements OnClickListener
{

    private Button mBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.lay2, container, false);
        mBtn = (Button) view.findViewById(R.id.lay2_btn);
        mBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v)
    {
        Fragment3 fTwo = new Fragment3();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.replace(R.id.id_content, fTwo);
        tx.addToBackStack(null);
        tx.commit();

    }

}