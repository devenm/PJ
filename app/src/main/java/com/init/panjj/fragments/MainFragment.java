package com.init.panjj.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.init.panjj.R;
import com.init.panjj.otherclasses.CustomViewPager;
import com.init.panjj.otherclasses.SwipeDirection;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by INIT on 1/16/2017.
 */

public class MainFragment extends Fragment {
    View view;
    public  List<String> mFragmentTitleList=new ArrayList<>();
    CustomViewPager viewPager;
    TabLayout tabLayout;
    public static  String tag;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view!=null) {
            viewPager.setCurrentItem(0,false);
            return view;
        }
         view=inflater.inflate(R.layout.mainfragment,container,false);
        viewPager = (CustomViewPager) view.findViewById(R.id.pager);
        viewPager.setAllowedSwipeDirection(SwipeDirection.all);
        tabLayout = (TabLayout) view.findViewById(R.id.tabanim_tabs);
        setUpadapter(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAllowedSwipeDirection(SwipeDirection.none);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                tag=mFragmentTitleList.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0,false);
        return view;
    }
    private void setUpadapter(ViewPager viewPager) {
       PageAdap pageAdap = new PageAdap(getFragmentManager());
        pageAdap.addFrag(new HomeFragment(), "    Home  ");
        pageAdap.addFrag(new Movies_mainPage(), "    Movies  ");
        pageAdap.addFrag(new NewTrailer_Tab(), "   Trailer  ");
        pageAdap.addFrag(new Video_tab(), "  Video Songs ");
        pageAdap.addFrag(new LiveTvFragment(), "  Live Tv  ");
        pageAdap.addFrag(new Gurbani(), "  Gurbani   ");
        pageAdap.addFrag(new RadioFragment(), "   Radio  ");
        viewPager.setAdapter(pageAdap);
    }

    public void openlive(int i) {
        if (viewPager!=null)
            viewPager.setCurrentItem(i);
    }

    private class PageAdap extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public PageAdap(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    public void replaceFragment(Fragment fragment, String url, String cat, String tag, String info, String id, int adapterPosition) {
//        Fragment frag = getFragmentManager().findFragmentByTag(tag);
        if (true) {
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            bundle.putString("cat", cat);
            bundle.putString("info", info);
            bundle.putString("tag", tag);
            bundle.putString("id", id);
            bundle.putInt("pos", adapterPosition);
            fragment.setArguments(bundle);
//            getFragmentManager().beginTransaction().replace(R.id.onlyhome, fragment, tag).addToBackStack(null).commit();
        }
    }
}
