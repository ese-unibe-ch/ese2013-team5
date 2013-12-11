package com.mensaunibe.app.views;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mensaunibe.R;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;

/**
 * The "Home"-Fragment shows one or multiple mensas, depending on the homescreen settings
 */
public class FragmentStart extends Fragment {
	
	// for logging and debugging purposes
	private static final String TAG = FragmentStart.class.getSimpleName();
	
	@SuppressWarnings("unused")
	private Controller mController;
	private DataHandler mDataHandler;
	private MensaList mMensaList;
	private Mensa mMensa;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		this.mController = Controller.getController();
		this.mDataHandler = Controller.getDataHandler();
		this.mMensaList = mDataHandler.getMensaList();
		
		if (mMensaList != null) {
			String homeconfig = (String) Controller.getSettings().getString("setting_homescreen", "closestmensa");
			
			if (homeconfig.equals("favmensalist") && mDataHandler.getMensaList().getFavMensas() != null) {
				List<Mensa> mFavMensas = mMensaList.getFavMensas();
				loadView(FragmentMensaList.newInstance(mFavMensas));
			} else if (homeconfig.equals("favmensadetails") && mDataHandler.getMensaList().getFavMensas() != null) {
				mMensa = mMensaList.getMensaById(1);
				loadView(new FragmentMensaListPager());
			} else if (homeconfig.equals("closestfavmensa") && mDataHandler.getClosestFavMensa() != null) {
				mMensa = mDataHandler.getClosestFavMensa();
				loadView(FragmentMensaDetails.newInstance(mMensa, true));
			} else {
				// default is the closest mensa, no need to specifically catch this, even if manually set
				if (mDataHandler.getClosestMensa() != null) {
					mMensa = mDataHandler.getClosestMensa();
				} else {
					mMensa = mMensaList.getMensaById(1);
				}
				loadView(FragmentMensaDetails.newInstance(mMensa, true));
			}
		} else {
			Log.e(TAG, "onCreateView(): mMensaList was null");
		}

		View rootView = inflater.inflate(R.layout.fragment_start, container, false);
	
		return rootView;
	}
	
	public void loadView(Fragment frag) {
		Fragment fragment = frag;
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.replace(R.id.frame_content, fragment).commit();
	}
}