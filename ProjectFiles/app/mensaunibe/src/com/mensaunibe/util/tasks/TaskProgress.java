package com.mensaunibe.util.tasks;

import android.os.Handler;
import android.widget.ProgressBar;

public class TaskProgress implements TaskListener {
	 
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = TaskProgress.class.getSimpleName();
	
	private Handler mHandler;
	private boolean mCheck;
	private ProgressBar mProgressBar;

	public TaskProgress(ProgressBar progressbar, int speed) {
		this.mHandler = new Handler();;
		this.mProgressBar = progressbar;
		this.mCheck = false;
		runProgress(speed);
	}
	
    private void runProgress(final int speed) {
        new Thread(
            new Runnable() {
                public void run() {
                    while (!mCheck) {

                        // show fake progress
                        for (int i = 0; i <= 100; i++) {
                            final int progress = i;
                            
                            if (mCheck) {
	                            try {
	                                Thread.sleep(speed/10);
	                                // Update the progress bar fast as it should be finished
	                                mHandler.post(new Runnable() {
	                                    public void run() {
	                                        mProgressBar.setProgress(progress);
	                                    }
	                                });
	                            } catch (InterruptedException e) {
	                                e.printStackTrace();
	                            }
                            } else {
	                            try {
	                                Thread.sleep(speed);
	                                // Update the progress bar
	                                mHandler.post(new Runnable() {
	                                    public void run() {
	                                        mProgressBar.setProgress(progress);
	                                    }
	                                });
	                            } catch (InterruptedException e) {
	                                e.printStackTrace();
	                            }
                            }
                        }
                    }
                }
            }
        ).start();
    }

	@Override
	public void onRendered() {
		this.mCheck = true;
	}

	@Override
	public void onTaskComplete(Object result) {
		// unused in this task as it is not async
	}

	@Override
	public void onProgressUpdate(int percent) {
		// unused in this task as it is not async
	}
}
