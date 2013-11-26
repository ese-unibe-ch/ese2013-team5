package com.mensaunibe.app.views;

import com.mensaunibe.R;

import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class FragmentSplashScreen extends Fragment {
	
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = FragmentSplashScreen.class.getSimpleName();

    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_splashscreen, container, false);
        // set up the progress bar
        mProgressBar = (ProgressBar) rootView.findViewById (R.id.progressbar);
        // add nice color to the progress bar
        mProgressBar.getProgressDrawable().setColorFilter(0xffE3003D, Mode.SRC_IN);
        return rootView;
    }

    /**
     * Sets the progress of the ProgressBar
     *
     * @param percent int the new progress between 0 and 100
     */
    public void setProgress(int percent) {
        mProgressBar.setProgress(percent);
    }
}