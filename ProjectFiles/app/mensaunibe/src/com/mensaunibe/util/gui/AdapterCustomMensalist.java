package com.mensaunibe.util.gui;

import java.util.List;
import com.mensaunibe.R;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.app.views.FragmentMensaList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterCustomMensalist extends BaseAdapter {
	
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = AdapterCustomMensalist.class.getSimpleName();
	
	private Controller mController;
	private DataHandler mDataHandler;
	private FragmentMensaList mFragment;
	private MensaList mMensaList;
	private List<Mensa> mMensas;
	private int mResource;

	public AdapterCustomMensalist(Controller controller, FragmentMensaList fragment, List<Mensa> mensalist, int resource) {
		super();
		this.mController = controller;
		this.mDataHandler = Controller.getDataHandler();
		this.mFragment = fragment;
		this.mMensaList = mDataHandler.getMensaList();
		this.mMensas = mensalist;
		this.mResource = resource; // the xml layout file, like this it gets dynamic
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater mInflater = (LayoutInflater) mController.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = mInflater.inflate(mResource, parent, false);
        
        final Mensa mensa = mMensas.get(position);
        
        LinearLayout grid = (LinearLayout) rowView.findViewById(R.id.list_grid);
        ImageButton mapbutton = (ImageButton) rowView.findViewById(R.id.button_map);
        CheckBox starCheckbox = (CheckBox) rowView.findViewById(R.id.checkbox_star);
        starCheckbox.setChecked(mensa.isFavorite());       
        // the actual fields that contain text
        TextView distance = (TextView) rowView.findViewById(R.id.distance);
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView address = (TextView) rowView.findViewById(R.id.address);
        TextView city = (TextView) rowView.findViewById(R.id.city);
        
        distance.setText(String.valueOf(Math.round(mensa.getDistance())) + "m");
        name.setText(mensa.getName());
        address.setText(mensa.getAddress());
        city.setText(mensa.getCity());

        // set the click listener for the list item
        // opens mensa details
        final OnClickListener rowListener = new OnClickListener() {
            @Override
            public void onClick(View rowView) {
            	mFragment.selectItem(position);
            	// save the current selected item to the DataHandler for eventual config changes
        		Controller.getDataHandler().setDrawerPosition(Controller.getNavigationDrawer().getDrawerListCount() + 1);
            }
        };
        grid.setOnClickListener(rowListener);
        
        // set the click listener for the navigation button
        // redirect to map fragment and show route
        final OnClickListener mapListener = new OnClickListener() {
            @Override
            public void onClick(View mapbutton) {
            	Controller.getDataHandler().setLocationTarget(mensa.getLocation());
            	Controller.getNavigationDrawer().selectItem(2);
            	// TODO: add status update
            }
        };
        mapbutton.setOnClickListener(mapListener);
        
        // set the click listener for the favorite button
        final OnCheckedChangeListener starListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Mensa mensa = mMensas.get(position);
				mensa.setFavorite(isChecked);
				mFragment.updateFavorite(mensa);
				// update the closest fav mensa / closest mensa
				mDataHandler.loadLocation(true);
			}
        };
        starCheckbox.setOnCheckedChangeListener(starListener);
            
		return rowView;
	}
	@Override
	public int getCount() {
		return mMensas.size();
	}
	@Override
	public Mensa getItem(int position) {
		return mMensas.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public void notifyDataSetChanged() {
		mMensaList.sortList();
	    super.notifyDataSetChanged();
	}
}