package com.android.androidtech.activity.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.android.androidtech.fragment.base.BaseFragment;

import java.util.HashMap;

/**
 * 
 * @author yuchengluo 2015-6-25
 */
@SuppressLint("Recycle")
public abstract class BaseFragmentActivity extends BaseActivity {

	private static final String TAG = "BaseFragmentActivity";

	private FragmentStackManager mFragmentStackManager;

	protected FragmentManager mManager;

	private int mContainerId;

	private String mContentTag;

	private StackLayout mMainFragmentContainer;

	protected static HashMap<String, Object> fragmentFields;
	protected int mSelectedIndex;
	protected Bundle mArgs;
	protected Class<? extends BaseFragment> mFragmentCls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mManager = getSupportFragmentManager();
	}

	private void setContainerId(int id) {
		this.mContainerId = id;
	}

	private void setContentTag(String tag) {
		this.mContentTag = TAG;
	}

	private void setStackLayout(StackLayout layout) {
		this.mMainFragmentContainer = layout;
	}

	protected void makeNewContentFragmentStackManager(int id, String tag, StackLayout layout) {
		setContainerId(id);
		setContentTag(tag);
		setStackLayout(layout);
		mFragmentStackManager = new FragmentStackManager(this, mManager, mContainerId, mContentTag,
				mMainFragmentContainer);
	}

	public void addSecondFragment(Class<? extends BaseFragment> cls, Bundle args, HashMap<String, Object> fields) {
		if (args == null) {
			args = new Bundle();
		}
		BaseFragment topFragment = mFragmentStackManager.top();
		mFragmentStackManager.push(cls, args, fields);
	}

	public void popBackStack(int requestCode, int resultCode, Intent data) {
		mFragmentStackManager.pop(requestCode, resultCode, data);
	}

	public void popBackStack() {
		mFragmentStackManager.pop(-100, -100, null);
	}

	public void clearBackStack() {
		mFragmentStackManager.clear();
	}


	public void addContent() {
		addContent(-1);
	}

	public BaseFragment top() {
		return mFragmentStackManager.top();
	}

	protected int size() {
		return mFragmentStackManager.size();
	}

	public void addContent(int isDefaultTabs) {
		mFragmentStackManager.push(mFragmentCls, mArgs, fragmentFields);
	}

	public BaseFragment getCurrentFragment() {
		return top();
	}

}
