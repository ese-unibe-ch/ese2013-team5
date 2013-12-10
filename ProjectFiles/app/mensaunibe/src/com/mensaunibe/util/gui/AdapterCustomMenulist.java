package com.mensaunibe.util.gui;

import java.util.List;

import com.mensaunibe.R;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.app.model.Menu;
import com.mensaunibe.app.views.FragmentMenuListDayFull;
import com.mensaunibe.app.views.FragmentMenuListPager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterCustomMenulist extends BaseAdapter {
	
	// for logging and debugging purposes
	private static final String TAG = AdapterCustomMenulist.class.getSimpleName();
	
	private Controller mController;
	private FragmentMenuListDayFull mFragment;

	private MensaList mMensaList;
	private List<Menu> mMenus;
	private int mResource;

	public AdapterCustomMenulist(Controller controller, FragmentMenuListDayFull fragment, MensaList mensalist, List<Menu> menulist, int resource) {
		super();
		this.mController = controller;
		this.mFragment = fragment;
		this.mMensaList = mensalist;
		this.mMenus = menulist;
		this.mResource = resource; // the xml layout file, like this it gets dynamic
	}
	
	public AdapterCustomMenulist(Controller controller, List<Menu> menulist, int resource) {
		super();
		this.mController = controller;
		this.mMenus = menulist;
		this.mResource = resource; // the xml layout file, like this it gets dynamic
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		LayoutInflater mInflater = (LayoutInflater) mController.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = mInflater.inflate(mResource, parent, false);
        LinearLayout row = (LinearLayout) rowView.findViewById(R.id.list_grid);
        
        // the actual fields that contain text
        final Menu menu = mMenus.get(position);
        
        // handle the full menu list displaying of the mensa
        if ( (TextView) rowView.findViewById(R.id.mensa) != null ) {
        	TextView mensa = (TextView) rowView.findViewById(R.id.mensa);
			Mensa mMensa = mMensaList.getMensaById(menu.getMensaID());
			if (mMensa != null) {
				mensa.setText(mMensa.getName());
			} else {
				Log.e(TAG, "mMensa was null!");
			}
        }
        
        TextView title = (TextView) rowView.findViewById(R.id.title);
        TextView desc = (TextView) rowView.findViewById(R.id.desc);
        TextView price = (TextView) rowView.findViewById(R.id.price);
        TextView date = (TextView) rowView.findViewById(R.id.date);
        TextView rating = (TextView) rowView.findViewById(R.id.rating);
        TextView count = (TextView) rowView.findViewById(R.id.count);
        ImageView icon = (ImageView) rowView.findViewById(R.id.icon);
        
        title.setText(menu.getTitle());
        desc.setText(menu.getDesc());
        price.setText(menu.getPrice());
        date.setText(menu.getDate());
        rating.setText(String.valueOf(menu.getRating()));
        count.setText(String.valueOf(menu.getVotes()));
        
        // determine the type of menu and set the according icon
        if (menu.getType().equals("meat")) {
        	icon.setImageResource(R.drawable.ic_menu_meat);
        } else if (menu.getType().equals("seafood")) {
        	icon.setImageResource(R.drawable.ic_menu_seafood);
        } else if (menu.getType().equals("vegetarian")) {
        	icon.setImageResource(R.drawable.ic_menu_vegetarian);
        }

        // set the click listener for the menu item
        OnClickListener rowListener = new OnClickListener() {
            @Override
            public void onClick(View rowView) {
            	if (mMensaList != null) {
            		((FragmentMenuListPager) mFragment.getParentFragment()).selectItem( mMensaList.getMensaById(mMenus.get(position).getMensaID()));
            	} else {
            		showRating(menu.getMenuID());
            	}
            }
        };
        
        // prevent menus that are not served today from getting rated
        if (mMensaList != null) {
        	row.setOnClickListener(rowListener);
        } else {
	        if (Controller.getDataHandler().getCurrentDayName().equals(menu.getDay())) {
	        	row.setOnClickListener(rowListener);
	        }
        }
            
		return rowView;
	}
	
	@Override
	public int getCount() {
		return mMenus.size();
	}
	
	@Override
	public Menu getItem(int position) {
		return mMenus.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void showRating(final int menuid) {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mController);
		
		final RatingBar rating = new RatingBar(mController);
		rating.setMax(5);
		rating.setStepSize(1.0f);
		rating.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		LinearLayout parentLayout = new LinearLayout(mController);
        parentLayout.setGravity(Gravity.CENTER);
        parentLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        parentLayout.addView(rating);

		dialogBuilder.setIcon(R.drawable.ic_star);
		dialogBuilder.setTitle("Menu bewerten");
		dialogBuilder.setView(parentLayout);

		// Buttons OK
		dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// send rating to server
				Controller.getDataHandler().APIRegisterRating(menuid, rating.getProgress());
				Toast.makeText(mController, "Send Vote for menu with ID = " + menuid + " to server..." + String.valueOf(rating.getProgress()), Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		});
		
		// Button cancel
		dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		dialogBuilder.create();
		dialogBuilder.show();
	}
}