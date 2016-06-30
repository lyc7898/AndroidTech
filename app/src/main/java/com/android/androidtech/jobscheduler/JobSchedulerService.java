package com.android.androidtech.jobscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;

/**
 * Created by yuchengluo on 2016/6/30.
 */

public class JobSchedulerService extends JobService{
    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
