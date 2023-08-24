package com.gisfy.inclenJson.Adapters;

import com.gisfy.inclenJson.Fragment.AboutFrag;
import com.gisfy.inclenJson.Fragment.BaselineSurvey;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyAdapter extends FragmentStateAdapter {
    public MyAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 1 :
                return new AboutFrag();
        }
        return new BaselineSurvey();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}