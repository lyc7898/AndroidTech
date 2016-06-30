package com.android.androidtech.jobscheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

/**
 * Created by yuchengluo on 2016/6/30.
 */

public class JobScheduleManager {
    private static JobScheduleManager mInstance = null;
    private Context mContext = null;
    private JobScheduler mJS = null;

    public static JobScheduleManager getmInstance(Context ctx) {
        if (null == mInstance) {
            mInstance = new JobScheduleManager(ctx);
        }
        return mInstance;
    }

    public JobScheduleManager(Context ctx) {
        mContext = ctx;
        mJS = (JobScheduler) mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    public boolean addJobScheduleTask(int task_id) {
        JobInfo.Builder builder = new JobInfo.Builder(task_id,
                new ComponentName("PackgeName",
                        JobSchedulerService.class.getName()));
        switch (task_id){
            case 1:
                builder.setPeriodic(1000);
                break;
            case 2:
                builder.setPersisted(false);
                break;
            default:

        }
        if(mJS != null) {
            return mJS.schedule(builder.build()) > 0;
        }else{
            return false;
        }
    }
    //create JobInfo

}
