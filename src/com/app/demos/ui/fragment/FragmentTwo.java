package com.app.demos.ui.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.demos.R;

public class FragmentTwo extends Fragment implements OnClickListener
{

    private Button mBtn ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.lay3, container, false);
        mBtn = (Button) view.findViewById(R.id.lay3_btn);
        mBtn.setOnClickListener(this);
        return view ;
    }
    @Override
    public void onClick(View v)
    {/*
        FragmentThree fThree = new FragmentThree();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.hide(this);
        tx.add(R.id.id_content , fThree, "THREE");
//      tx.replace(R.id.id_content, fThree, "THREE");
        tx.addToBackStack(null);
        tx.commit();
        */
    }


}