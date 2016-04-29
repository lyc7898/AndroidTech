package com.android.androidtech.activity.base;

import android.content.Intent;
import android.os.Bundle;


import com.android.androidtech.fragment.base.BaseFragment;

import java.util.HashMap;

/**
 * Created by yuchengluo on 2015/6/26.
 */
public interface IFragmentStackManager {
    void push(Class<? extends BaseFragment> cls, Bundle args, HashMap<String, Object> hashMap);

    boolean pop(int requestCode, int resultCode, Intent data);

    boolean empty();

    boolean full();

    void clear();

    int size();

    BaseFragment top();

    void destroy();

}
