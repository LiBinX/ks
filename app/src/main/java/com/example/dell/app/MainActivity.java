package com.example.dell.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bank.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

        private ViewPager mViewPager;
        private FragmentPagerAdapter mAdapter;
        private List<Fragment> mFragments = new ArrayList<Fragment>();
        /*底部三个按钮*/
        private LinearLayout mTabBtnWeixin;
        private LinearLayout mTabBtnFrd;
        private LinearLayout mTabBtnAddress;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

                initView();
                findViewById(R.id.btn_tab_bottom_weixin).setOnClickListener(this);
                findViewById(R.id.btn_tab_bottom_contact).setOnClickListener(this);
                findViewById(R.id.btn_tab_bottom_friend).setOnClickListener(this);

                //FragmentPagerAdapter处理页面的横向滑动，getSupportFragmentManager加载fragment
                mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

                        @Override
                        public int getCount() {
                                return mFragments.size();
                        }

                        @Override
                        public Fragment getItem(int arg0) {
                                return mFragments.get(arg0);
                        }
                };

                mViewPager.setAdapter(mAdapter);

                mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
                        private int currentIndex;

                        @Override
                        //onPageSelected页面跳转完后得到调用，position是你当前选中的页面的Position
                        public void onPageSelected(int position) {
                                resetTabBtn();
                                switch (position) {
                                        case 0:
                                                ((ImageButton) mTabBtnWeixin.findViewById(R.id.btn_tab_bottom_weixin))
                                                        .setImageResource(R.drawable.tab_weixin_pressed);
                                                break;
                                        case 1:
                                                ((ImageButton) mTabBtnFrd.findViewById(R.id.btn_tab_bottom_friend))
                                                        .setImageResource(R.drawable.tab_find_frd_pressed);
                                                break;
                                        case 2:
                                                ((ImageButton) mTabBtnAddress.findViewById(R.id.btn_tab_bottom_contact))
                                                        .setImageResource(R.drawable.tab_address_pressed);
                                                break;
                                }

                                currentIndex = position;
                        }

                        @Override
                        //当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直得到调用。
                        public void onPageScrolled(int arg0, float arg1, int arg2) {

                        }

                        @Override
                        //在状态改变的时候调用
                        public void onPageScrollStateChanged(int arg0) {
                        }
                });
        }

        protected void resetTabBtn() {
                ((ImageButton) mTabBtnWeixin.findViewById(R.id.btn_tab_bottom_weixin))
                        .setImageResource(R.drawable.tab_weixin_normal);
                ((ImageButton) mTabBtnFrd.findViewById(R.id.btn_tab_bottom_friend))
                        .setImageResource(R.drawable.tab_find_frd_normal);
                ((ImageButton) mTabBtnAddress.findViewById(R.id.btn_tab_bottom_contact))
                        .setImageResource(R.drawable.tab_address_normal);
        }

        private void initView() {

                mTabBtnWeixin = (LinearLayout) findViewById(R.id.id_tab_bottom_weixin);
                mTabBtnFrd = (LinearLayout) findViewById(R.id.id_tab_bottom_friend);
                mTabBtnAddress = (LinearLayout) findViewById(R.id.id_tab_bottom_contact);

                FragmentAccount fragmentaccount = new FragmentAccount();
                FragmentFind fragmentfind = new FragmentFind();
                FragmentPlan fragmentplan = new FragmentPlan();
                mFragments.add(fragmentaccount);
                mFragments.add(fragmentplan);
                mFragments.add(fragmentfind);
        }

        public void onClick(View v) {
                switch (v.getId()) {
                        case R.id.btn_tab_bottom_weixin:
                                mViewPager.setCurrentItem(0, true);
                                break;
                        case R.id.btn_tab_bottom_friend:
                                mViewPager.setCurrentItem(1, true);
                                break;
                        case R.id.btn_tab_bottom_contact:
                                mViewPager.setCurrentItem(2, true);
                                break;
                }
        }
}
