package com.demo.yiman.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseDialogFragment extends DialogFragment {
    private Unbinder unbinder;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewData();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = createDialog();
        unbinder = ButterKnife.bind(this, dialog);
        return dialog;
    }
    protected abstract Dialog createDialog();
    protected abstract void initViewData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (unbinder!=null){
                unbinder.unbind();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
