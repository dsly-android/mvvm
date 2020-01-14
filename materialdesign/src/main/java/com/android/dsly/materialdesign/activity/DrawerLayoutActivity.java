package com.android.dsly.materialdesign.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.materialdesign.R;
import com.android.dsly.materialdesign.databinding.DesignActivityDrawerLayoutBinding;
import com.blankj.utilcode.util.LogUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class DrawerLayoutActivity extends BaseFitsWindowActivity<DesignActivityDrawerLayoutBinding, BaseViewModel>
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    public int getLayoutId() {
        return R.layout.design_activity_drawer_layout;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setSupportActionBar(mBinding.designDrawerLayout.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mBinding.drawerLayout, mBinding.designDrawerLayout.toolbar, R.string.design_navigation_drawer_open, R.string.design_navigation_drawer_close);
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void initEvent() {
        mBinding.designDrawerLayout.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mBinding.navView.setNavigationItemSelectedListener(this);
        mBinding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                LogUtils.e("onDrawerSlide:"+slideOffset);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                LogUtils.e("onDrawerOpened");
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                LogUtils.e("onDrawerClosed");
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                LogUtils.e("onDrawerStateChanged:"+newState);
            }
        });
    }

    @Override
    public void initData() {
        mBinding.setData("aaa");
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.design_menu_drawer_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
