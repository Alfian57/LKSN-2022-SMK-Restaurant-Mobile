package com.postingan.esemka_restaurant.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.postingan.esemka_restaurant.Fragment.AyamFragment;
import com.postingan.esemka_restaurant.Fragment.CemilanFragment;
import com.postingan.esemka_restaurant.Fragment.DagingSapiFragment;
import com.postingan.esemka_restaurant.Fragment.HappyMealFragment;
import com.postingan.esemka_restaurant.Fragment.IkanFragment;
import com.postingan.esemka_restaurant.Fragment.MakananPenutupFragment;
import com.postingan.esemka_restaurant.Fragment.PaketFamilyFragment;
import com.postingan.esemka_restaurant.Fragment.SarapanPagiFragment;

public class VPAdapter extends FragmentStateAdapter {


    public VPAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AyamFragment();
            case 1:
                return new CemilanFragment();
            case 2:
                return new DagingSapiFragment();
            case 3:
                return new HappyMealFragment();
            case 4:
                return new IkanFragment();
            case 5:
                return new MakananPenutupFragment();
            case 6:
                return new PaketFamilyFragment();
            case 7:
                return new SarapanPagiFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 9;
    }
}
