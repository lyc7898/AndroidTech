package com.ycl.androidtech.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;


import com.ycl.androidtech.R;
import com.ycl.androidtech.activity.base.BaseFragmentActivity;
import com.ycl.androidtech.activity.base.StackLayout;
import com.ycl.androidtech.fragment.base.BaseFragment;
import com.ycl.androidtech.fragment.homepage.HomePageFragment;
import com.ycl.androidtech.utils.GLog;

import java.util.HashMap;

/**
 * Created by yuchengluo on 2015/6/26.
 */
public class HomePageActivity extends BaseFragmentActivity {

    private final static String TAG = "MusicMainWithMiniBarActivity";
    public final static String IS_SHOW_PUSH_DIALOG = "is_show_dialog";
    public final static String PUSH_DIALOG_TITLE = "dialog_title";
    public final static String PUSH_DIALOG_MSG = "dialog_message";
    private int mFocusedTextColor, mTextColor;

    private boolean isShowPushDialog = false;
    private String pushDialogTitle;
    private String pushDialogMessage;
  //  private View mMiniBar;
   // private View mMinibarLoadingAnim;
   // private View mMinibarLoadingBtn;
   // private View mMinibarPlayBtn;
   // private ImageView mMinibarPlayListBtn;
  //  private View mMinibarPauseBtn;
  //  private View mMinibarCircleBtn;
  //  private RelativeLayout mPopWindowViewContainer;
    //private AsyncEffectImageView mFloatAdImageView;
 //   private ImageView mViewSwitchNight;

    private StackLayout mMainFragmentContainer;

    private static final String MAIN_FRAGMENT_CONTENT = "main_content";

    private boolean misStartActivityWithAnim;
    private boolean misStartActivityForResult;
    public static final int CONTAINER_ID = R.id.homepage_fragment_detail;
    public static final int DEFAULT_REQUEST_CODE = -1;
    public static final String IS_FINISH_WITH_ANIM = "is_finish_activity_with_anim";
    private int mViewIndex = TAB_VIEW_00;

    /**
     * 是否非Activity界面跳转过来
     * */
    public static final String GOTO_FRAGMENT_FROM_TIMESCAPE = "is_com_from_timescape";
    /**
     * 需要跳转到的fragment
     * */
    public static final String ARG_MAIN_ACTIVITYWITHMINI_KEY_SHOW_FRAGMENT = "the_show_fragment";
    /**
     * TAB 定位的POS
     * */
    public static final String ARG_MAIN_ACTIVITYWITHMINI_KEY_SELECTED_TAB = "the_selected_tab";
    /**
     * 需要跳转到Fragment中的ARG
     * */
    public static final String ARG_MAIN_ACTIVITYWITHMINI_KEY_FRAGMENT_ARGS = "the_fragment_args";
    /**
     * 是否需要动画
     * */
    public static final String ARG_MAIN_ACTIVITYWITHMINI_KEY_ISSTARTVITHANIM = "is_start_activity_with_anim";
    /**
     * 是否需要Result For back
     * */
    public static final String ARG_MAIN_ACTIVITYWITHMINI_KEY_IS_ACTIVITY_FORRESULT = "is_start_activity_forresult";

    /**
     * 有参数，需要给Fragment添加Argment，有tab版的fragment显示需要调用这个show方法
     *
     * @param context
     *            用于启动新的activity
     * @param cls
     *            你想要创建的Fragment的类型，用于反射。
     * @param fields
     *            fragment构造函数有参数的话放在这里面，对Fragment的字段进行直接设置。
     * @param args
     *            Fragment要得到的Argments。
     * @param selectedIndex
     *            goto the tab of selectedIndex
     * @param isStartActivityWithAnim
     *            启动Activity是否用从下到上的动画：true为用，false为不用
     * @param isStartActivityForResult
     *            启动Activity是否用startActivityForResult的方法：true为用，false为不用
     * @param requestCode
     *            isStartActivityForResult为true时各自定义后传入的唯一标识，
     *            如果isStartActivityForResult
     *            为false，统一传TerminalIndependenceActivity.DEFAULT_REQUEST_CODE
     */

    public static void show(Context context, final Class<? extends BaseFragment> cls, HashMap<String, Object> fields,
                            Bundle args, int selectedIndex, boolean isStartActivityWithAnim, boolean isStartActivityForResult,
                            int requestCode) {
        fragmentFields = fields;

        // 如果这个MusicMainWithMiniBarActivity已经存在，那么就不在startActivity
        if (context != null && context instanceof HomePageActivity) {
            HomePageActivity activity = (HomePageActivity) context;
            activity.addSecondFragment(cls, args, fields);
            return;
        }
        Intent intent = new Intent(context, HomePageActivity.class);
        String[] temp = cls.toString().split(" ");
        String theShowFragmentCls = temp[1];
        intent.putExtra(ARG_MAIN_ACTIVITYWITHMINI_KEY_SHOW_FRAGMENT, theShowFragmentCls);
        intent.putExtra(ARG_MAIN_ACTIVITYWITHMINI_KEY_SELECTED_TAB, selectedIndex);
        if (null == args) {
            args = new Bundle();
        }
        if (isStartActivityWithAnim) {
            args.putBoolean(IS_FINISH_WITH_ANIM, isStartActivityWithAnim);
        }
        intent.putExtra(ARG_MAIN_ACTIVITYWITHMINI_KEY_FRAGMENT_ARGS, args);
        intent.putExtra(ARG_MAIN_ACTIVITYWITHMINI_KEY_ISSTARTVITHANIM, isStartActivityWithAnim);
        intent.putExtra(ARG_MAIN_ACTIVITYWITHMINI_KEY_IS_ACTIVITY_FORRESULT, isStartActivityForResult);
        boolean isComFromActivity = args.getBoolean(GOTO_FRAGMENT_FROM_TIMESCAPE, false);
        if (isComFromActivity) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        boolean isFromFriendStream = args.getBoolean("is_from_friendStream", false);
        if (isFromFriendStream) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        if (context != null) {
            if (isStartActivityForResult) {
                ((Activity) context).startActivityForResult(intent, requestCode);
            } else {
                context.startActivity(intent);
            }
        }
        if (isStartActivityWithAnim) {
            // ((Activity) context).overridePendingTransition(R.anim.roll_up,
            // R.anim.still_when_up);
        }
    }

    /**
     * 有参数，需要给Fragment添加Argment，无tab版的fragment显示需要调用这个show方法
     *
     * @param context
     *            用于启动新的activity
     * @param cls
     *            你想要创建的Fragment的类型，用于反射。
     * @param fields
     *            fragment构造函数有参数的话放在这里面，对Fragment的字段进行直接设置。
     * @param args
     *            Fragment要得到的Argments。
     * @param isStartActivityWithAnim
     *            启动Activity是否用从下到上的动画：true为用，false为不用
     * @param isStartActivityForResult
     *            启动Activity是否用startActivityForResult的方法：true为用，false为不用
     * @param requestCode
     *            isStartActivityForResult为true时各自定义后传入的唯一标识，
     *            如果isStartActivityForResult
     *            为false，统一传TerminalIndependenceActivity.DEFAULT_REQUEST_CODE
     */
    public static void show(Context context, final Class<? extends BaseFragment> cls, HashMap<String, Object> fields,
                            Bundle args, boolean isStartActivityWithAnim, boolean isStartActivityForResult, int requestCode) {
        show(context, cls, fields, args, -1, isStartActivityWithAnim, isStartActivityForResult, requestCode);
    }

    /**
     * 有参数，不需要给Fragment添加Argment，有tab版的fragment显示需要调用这个show方法
     *
     * @param context
     *            用于启动新的activity
     * @param cls
     *            你想要创建的Fragment的类型，用于反射。
     * @param fields
     *            fragment构造函数有参数的话放在这里面，对Fragment的字段进行直接设置。
     * @param selectedIndex
     *            goto the tab of selectedIndex
     * @param isStartActivityWithAnim
     *            启动Activity是否用从下到上的动画：true为用，false为不用
     * @param isStartActivityForResult
     *            启动Activity是否用startActivityForResult的方法：true为用，false为不用
     * @param requestCode
     *            isStartActivityForResult为true时各自定义后传入的唯一标识，
     *            如果isStartActivityForResult
     *            为false，统一传TerminalIndependenceActivity.DEFAULT_REQUEST_CODE
     */
    public static void show(Context context, final Class<? extends BaseFragment> cls, HashMap<String, Object> fields,
                            int selectedIndex, boolean isStartActivityWithAnim, boolean isStartActivityForResult, int requestCode) {
        show(context, cls, fields, null, selectedIndex, isStartActivityWithAnim, isStartActivityForResult, requestCode);
    }

    /**
     * 无参数，不需要给Fragment添加Argment，无tab版的fragment显示需要调用这个show方法
     *
     * @param context
     *            用于启动新的activity
     * @param cls
     *            你想要创建的Fragment的类型，用于反射。
     * @param isStartActivityWithAnim
     *            启动Activity是否用从下到上的动画：true为用，false为不用
     * @param isStartActivityForResult
     *            启动Activity是否用startActivityForResult的方法：true为用，false为不用
     * @param requestCode
     *            isStartActivityForResult为true时各自定义后传入的唯一标识，
     *            如果isStartActivityForResult
     *            为false，统一传TerminalIndependenceActivity.DEFAULT_REQUEST_CODE
     */
    public static void show(Context context, final Class<? extends BaseFragment> cls, boolean isStartActivityWithAnim,
                            boolean isStartActivityForResult, int requestCode) {
        show(context, cls, null, null, -1, isStartActivityWithAnim, isStartActivityForResult, requestCode);
    }

    /**
     * 有参数，不需要给Fragment添加Argment，无tab版的fragment显示需要调用这个show方法
     *
     * @param context
     *            用于启动新的activity
     * @param cls
     *            你想要创建的Fragment的类型，用于反射。
     * @param fields
     *            fragment构造函数有参数的话放在这里面，对Fragment的字段进行直接设置。
     * @param isStartActivityWithAnim
     *            启动Activity是否用从下到上的动画：true为用，false为不用
     * @param isStartActivityForResult
     *            启动Activity是否用startActivityForResult的方法：true为用，false为不用
     * @param requestCode
     *            isStartActivityForResult为true时各自定义后传入的唯一标识，
     *            如果isStartActivityForResult
     *            为false，统一传TerminalIndependenceActivity.DEFAULT_REQUEST_CODE
     */
    public static void show(Context context, final Class<? extends BaseFragment> cls, HashMap<String, Object> fields,
                            boolean isStartActivityWithAnim, boolean isStartActivityForResult, int requestCode) {
        show(context, cls, fields, null, -1, isStartActivityWithAnim, isStartActivityForResult, requestCode);
    }

    /**
     * 无参数，需要给Fragment添加Argment，有tab版的fragment显示需要调用这个show方法
     *
     * @param context
     *            用于启动新的activity
     * @param cls
     *            你想要创建的Fragment的类型，用于反射。
     * @param args
     *            Fragment要得到的Argments。
     * @param selectedIndex
     *            goto the tab of selectedIndex
     * @param isStartActivityWithAnim
     *            启动Activity是否用从下到上的动画：true为用，false为不用
     * @param isStartActivityForResult
     *            启动Activity是否用startActivityForResult的方法：true为用，false为不用
     * @param requestCode
     *            isStartActivityForResult为true时各自定义后传入的唯一标识，
     *            如果isStartActivityForResult
     *            为false，统一传TerminalIndependenceActivity.DEFAULT_REQUEST_CODE
     */
    public static void show(Context context, final Class<? extends BaseFragment> cls, Bundle args, int selectedIndex,
                            boolean isStartActivityWithAnim, boolean isStartActivityForResult, int requestCode) {
        show(context, cls, null, args, selectedIndex, isStartActivityWithAnim, isStartActivityForResult, requestCode);
    }

    /**
     * 无参数，需要给Fragment添加Argment，无tab版的fragment显示需要调用这个show方法
     *
     * @param context
     *            用于启动新的activity
     * @param cls
     *            你想要创建的Fragment的类型，用于反射。
     * @param args
     *            Fragment要得到的Argments。
     * @param isStartActivityWithAnim
     *            启动Activity是否用从下到上的动画：true为用，false为不用
     * @param isStartActivityForResult
     *            启动Activity是否用startActivityForResult的方法：true为用，false为不用
     * @param requestCode
     *            isStartActivityForResult为true时各自定义后传入的唯一标识，
     *            如果isStartActivityForResult
     *            为false，统一传TerminalIndependenceActivity.DEFAULT_REQUEST_CODE
     */
    public static void show(Context context, final Class<? extends BaseFragment> cls, Bundle args,
                            boolean isStartActivityWithAnim, boolean isStartActivityForResult, int requestCode) {
        show(context, cls, null, args, -1, isStartActivityWithAnim, isStartActivityForResult, requestCode);
    }

    /**
     * 无参数，不需要给Fragment添加Argment，有tab版的fragment显示需要调用这个show方法
     *
     * @param context
     *            用于启动新的activity
     * @param cls
     *            你想要创建的Fragment的类型，用于反射。
     * @param selectedIndex
     *            goto the tab of selectedIndex
     * @param isStartActivityWithAnim
     *            启动Activity是否用从下到上的动画：true为用，false为不用
     * @param isStartActivityForResult
     *            启动Activity是否用startActivityForResult的方法：true为用，false为不用
     * @param requestCode
     *            isStartActivityForResult为true时各自定义后传入的唯一标识，
     *            如果isStartActivityForResult
     *            为false，统一传TerminalIndependenceActivity.DEFAULT_REQUEST_CODE
     */
    public static void show(Context context, final Class<? extends BaseFragment> cls, int selectedIndex,
                            boolean isStartActivityWithAnim, boolean isStartActivityForResult, int requestCode) {
        show(context, cls, null, null, selectedIndex, isStartActivityWithAnim, isStartActivityForResult, requestCode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        mMainFragmentContainer = (StackLayout) findViewById(R.id.homepage_fragment_detail);
        makeNewContentFragmentStackManager(CONTAINER_ID, MAIN_FRAGMENT_CONTENT, mMainFragmentContainer);
//        mFocusedTextColor = mContext.getResources().getColor(R.color.white);
//        mTextColor = mContext.getResources().getColor(R.color.main_top_bar_text_color);
        initView();

//
//        IntentFilter f = new IntentFilter();
//        f.addAction(BroadcastAction.ACTION_2G3G_STATE_CHANGED);
//        registerReceiver(m2G3GChangedReceiver, new IntentFilter(f), BroadcastAction.SEND_BROADCAST_PROMISSION, null);

        Intent intent = getIntent();
        misStartActivityWithAnim = intent.getBooleanExtra(ARG_MAIN_ACTIVITYWITHMINI_KEY_ISSTARTVITHANIM, false);
        misStartActivityForResult = intent.getBooleanExtra(ARG_MAIN_ACTIVITYWITHMINI_KEY_IS_ACTIVITY_FORRESULT, false);
        if (misStartActivityForResult) {
            setResult(Activity.RESULT_CANCELED);
        }
        Bundle data = new Bundle();
        data.putInt(APP_INDEX_KEY, mViewIndex);
        addSecondFragment(HomePageFragment.class, data, null);

        String theShowFragmentCls = intent.getStringExtra(ARG_MAIN_ACTIVITYWITHMINI_KEY_SHOW_FRAGMENT);

        if (!TextUtils.isEmpty(theShowFragmentCls)) {

            mSelectedIndex = intent.getIntExtra(ARG_MAIN_ACTIVITYWITHMINI_KEY_SELECTED_TAB, -1);

            mArgs = intent.getBundleExtra(ARG_MAIN_ACTIVITYWITHMINI_KEY_FRAGMENT_ARGS);
            if (mSelectedIndex != -1 && mArgs != null) {
                mArgs.putInt(ARG_MAIN_ACTIVITYWITHMINI_KEY_SELECTED_TAB, mSelectedIndex);
            }
            try {
                mFragmentCls = (Class<? extends BaseFragment>) Class.forName(theShowFragmentCls);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (mFragmentCls != null) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (mSelectedIndex != -1) {
                            addContent(mSelectedIndex);
                        } else {
                            addContent();
                        }
                    }
                }, 500);
            }
        }

        // 不在这里先初始化 h5支付透穿会有问题....
        // APMidasPayAPI.init(this);
        // APMidasPayAPI.setEnv(APMidasPayAPI.ENV_TEST);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String theShowFragmentCls = intent.getStringExtra(ARG_MAIN_ACTIVITYWITHMINI_KEY_SHOW_FRAGMENT);
        BaseFragment bf = top();
        String nowTopShowFragmentCls = null;
        if (bf != null) {
            nowTopShowFragmentCls = bf.getClass().toString();
            String[] temp = nowTopShowFragmentCls.split(" ");
            nowTopShowFragmentCls = temp[1];
        }

        mSelectedIndex = intent.getIntExtra(ARG_MAIN_ACTIVITYWITHMINI_KEY_SELECTED_TAB, -1);

        mArgs = intent.getBundleExtra(ARG_MAIN_ACTIVITYWITHMINI_KEY_FRAGMENT_ARGS);
        if (mArgs == null) {
            mArgs = new Bundle();
        }


        if (!TextUtils.isEmpty(theShowFragmentCls)) {
            if (mSelectedIndex != -1 && mArgs != null) {
                mArgs.putInt(ARG_MAIN_ACTIVITYWITHMINI_KEY_SELECTED_TAB, mSelectedIndex);
            }
            try {
                mFragmentCls = (Class<? extends BaseFragment>) Class.forName(theShowFragmentCls);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (mFragmentCls != null) {
                if (mSelectedIndex != -1) {
                    addContent(mSelectedIndex);
                } else {
                    addContent();
                }
            }
        }
    }

    @Override
    public int getSaveUIID() {
        return APP_HOMEPAGE_FRAGMENT;
    }

    @Override
    public boolean finishWhenJump() {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        GLog.d("StackLayout", "a onKeyDown");

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
//                if (mPopWindowViewContainer.getVisibility() == View.VISIBLE && dissmissPopWindowContainer()) {
//                    return true;
//                }
//                if (mPlayerComponent.isShow()) {
//                    DefaultEventBus.post(event);
//                    return true;
//                }
                BaseFragment top = top();
                if (top != null && top.onKeyDown(keyCode, event)) {
                    return true;
                }
                GLog.d(TAG, "zxg@@@@@ onKeyDown size() is:" + size());
                if (size() > 1) {
                    popBackStack();
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_MENU:
                return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    protected boolean isAutoStaticsForm() {
        return false;
    }


    private void initView() {

    }


    // public FormeManager getFormeMannager() {
    // return ((FormeManager)
    // InstanceManager.getInstance(InstanceManager.INSTANCE_FORME));
    // }

    private static final int MSG_REFRESH_MUSIC_CIRCLE = 10000;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GLog.d(TAG, "On Resume" + getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        GLog.d(TAG, "onPause " + getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void finish() {
        super.finish();
    }

    protected void networkStateChanged() {
        BaseFragment curFragment = getCurrentFragment();
        if (curFragment != null) {
            curFragment.networkStateChanged();
        }
    }

    public void cancelSetLoading() {
    }

    public int getTopbarHeight() {
        return 48;//(int) getResources().getDimension(R.dimen.topbar_height);
    }

    protected void refreshMoreView() {
        if (TAB_VIEW_03 == mViewIndex) {
            // mMoreView.initContentView();
            // mMoreView.rebuildListView();
            // mMoreView.refreshUI();
        }
    }

    private Animation mLoadingRotateAnim;

    // @Override
    // public void onServiceConnected(ComponentName name, IBinder service) {
    // super.onServiceConnected(name, service);
    // onServiceConnectedMiniBar();
    // }





    public View getMainFragmentContainer() {
        return mMainFragmentContainer;
    }



    public boolean isMainActivity() {
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        GLog.d(TAG, "onSaveInstanceState do nothing");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        GLog.d(TAG, "onRestoreInstanceState do nothing too");
    }
}
