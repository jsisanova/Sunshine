package com.example.android.sunshine.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
// TODO (2) Make sure you've imported the jobdispatcher.JobService, not job.JobService !!!!
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;

// TODO (3) Add a class called SunshineFirebaseJobService that extends jobdispatcher.JobService
public class SunshineFirebaseJobService extends JobService {

    //  TODO  (4) Declare an ASyncTask field called mFetchWeatherTask
    private AsyncTask<Void, Void, Void> mFetchWeatherTask;

//   TODO (5) Override onStartJob and within it, spawn off a separate ASyncTask to sync weather data
    /**
     * The entry point to your Job. Implementations should offload work to another thread of
     * execution as soon as possible.
     *
     * This is called by the Job Dispatcher to tell us we should start our job. Keep in mind this
     * method is run on the application's main thread, so we need to offload work to a background
     * thread.
     *
     * @return whether there is more work remaining.
     */
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        mFetchWeatherTask = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                SunshineSyncTask.syncWeather(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //  TODO (6) Once the weather data is sync'd, call jobFinished with the appropriate arguements
                // false value to signify that we don’t have any more work to do.
                jobFinished(jobParameters, false);
            }
        };

        mFetchWeatherTask.execute();
        return true;
    }

//   TODO  (7) Override onStopJob, cancel the ASyncTask if it's not null and return true
    /**
     * Called when the scheduling engine has decided to interrupt the execution of a running job,
     * most likely because the runtime constraints associated with the job are no longer satisfied.
     *
     * @return whether the job should be retried - to be rescheduled to finish that work that we were doing when it was interrupted
     * @see Job.Builder#setRetryStrategy(RetryStrategy)
     * @see RetryStrategy
     */
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mFetchWeatherTask != null) {
            mFetchWeatherTask.cancel(true);
        }
        return true;
    }
}