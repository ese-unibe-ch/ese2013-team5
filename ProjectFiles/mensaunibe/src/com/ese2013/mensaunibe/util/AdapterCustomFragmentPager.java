package com.ese2013.mensaunibe.util;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;




public class AdapterCustomFragmentPager extends FragmentPagerAdapter {
	
	Context ctx=null;
	
    public AdapterCustomFragmentPager(Context ctx, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
        case 0:
//            return new ImageFragment(R.drawable.caesar_salad,menu_name[0], menu_description[0]);
        case 1:
//            return new ImageFragment(R.drawable.albondigas_pasta,menu_name[1], menu_description[1]);
        case 2:
//            return new ImageFragment(R.drawable.salmon_entrada,menu_name[2], menu_description[2]);
        default:
            return null;
        }
    }
}