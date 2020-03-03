package com.hezhihu89.adapter;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

/**
 * @author hezhihu89
 * 创建时间 2020 年 02 月 25 日 10:32
 * 功能描述:
 */
class Bean extends ViewModel {

    public MutableLiveData<List<Bean>> mLiveData = new MutableLiveData<List<Bean>>();

    public void setData(AppCompatActivity activity, List<Bean> liveData) {
        mLiveData.setValue(liveData);

    }

    public LiveData<List<Bean>> getLiveData() {
        return mLiveData;
    }
}
