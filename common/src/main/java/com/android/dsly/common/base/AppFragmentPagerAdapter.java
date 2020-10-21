/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.dsly.common.base;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * ================================================
 * 基类 {@link FragmentStatePagerAdapter}
 * <p>
 * Created by JessYan on 22/03/2016
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 * @author chenzhipeng
 */
public class AppFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<Class> mList;
    private List<String> mTitles;

    public AppFragmentPagerAdapter(FragmentManager fragmentManager, List<Class> list) {
        super(fragmentManager);
        this.mList = list;
    }

    public AppFragmentPagerAdapter(FragmentManager fragmentManager, List<Class> list, List<String> titles) {
        super(fragmentManager);
        this.mList = list;
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        try {
            return (Fragment) mList.get(position).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null) {
            return mTitles.get(position);
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
