package com.example.energyefficience;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class MyFragmentStateAdapter extends FragmentStateAdapter{
    int NumofTabs;
    public MyFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity, int NumofTabs) {
        super(fragmentActivity);
        this.NumofTabs = NumofTabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position){
            case 0:
                fragment = new Base64Fragment();
                return fragment;
            case 1:
                fragment = new SecondAlgFramgment();
                return fragment;
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return NumofTabs;
    }
}
