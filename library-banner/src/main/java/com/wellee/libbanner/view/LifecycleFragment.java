package com.wellee.libbanner.view;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

public class LifecycleFragment extends Fragment implements LifecycleOwner {

    LifecycleRegistry mLifecycleRegistry;

    @Override
    public void onResume() {
        super.onResume();
        Log.d("LifecycleFragment", "onResume");
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("LifecycleFragment", "onPause");
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("LifecycleFragment", "onHiddenChanged hidden = " + hidden);
    }

    public LifecycleFragment() {
        initLifecycle();
    }

    private void initLifecycle() {
        mLifecycleRegistry = new LifecycleRegistry(this);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

}
