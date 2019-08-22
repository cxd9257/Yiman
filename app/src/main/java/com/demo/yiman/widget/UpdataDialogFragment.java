package com.demo.yiman.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.yiman.R;

public class UpdataDialogFragment extends DialogFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setWindowAnimations(R.style.diaog_animations);
        View view = inflater.inflate(R.layout.dialog_updata,null);
        return view;
    }

    public static  UpdataDialogFragment newInstance(){
        UpdataDialogFragment updataDialogFragment = new UpdataDialogFragment();
        return updataDialogFragment;
    }
}
