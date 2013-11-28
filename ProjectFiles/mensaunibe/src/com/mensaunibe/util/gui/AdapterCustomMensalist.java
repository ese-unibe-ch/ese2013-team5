package com.mensaunibe.util.gui;

import java.util.List;
import com.mensaunibe.R;
import com.mensaunibe.app.model.Mensa;
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
import android.widget.Toast;

public class AdapterCustomMensalist extends BaseAdapter {
	
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = AdapterCustomMensalist.class.getSimpleName();
	
	private Context mContext;
	private FragmentMensaList mFragment;
	private List<Mensa> mMensaList;
	private int mResource;

	public AdapterCustomMensalist(Context context, FragmentMensaList fragment, List<Mensa> mensalist, int resource) {
		super();
		this.mContext = context;
		this.mFragment = fragment;
		this.mMensaList = mensalist;
		this.mResource = resource; // the xml layout file, like this it gets dynamic
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = mInflater.inflate(mResource, parent, false);
        
        Mensa mensa = mMensaList.get(position);
        
        LinearLayout grid = (LinearLayout) rowView.findViewById(R.id.list_grid);
        ImageButton mapbutton = (ImageButton) rowView.findViewById(R.id.button_map);
        CheckBox starcheckbox = (CheckBox) rowView.findViewById(R.id.checkbox_star);
        starcheckbox.setChecked(mensa.isFavorite());       
        // the actual fields that contain text
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView address = (TextView) rowView.findViewById(R.id.address);
        TextView city = (TextView) rowView.findViewById(R.id.city);
        
        name.setText(mensa.getName());
        address.setText(mensa.getAddress());
        city.setText(mensa.getCity());

        // set the click listener for the list item
        // should open mensa details
        final OnClickListener rowListener = new OnClickListener() {
            @Override
            public void onClick(View rowView) {
            	// show the mensadetails for the clicked mensa
            	mFragment.selectItem(position);
            	// delete after developement, just to show that it works
            	Toast.makeText(rowView.getContext(), "Mensa clicked, show details", Toast.LENGTH_SHORT).show();
            }
        };
        grid.setOnClickListener(rowListener);
        
        // set the click listener for the navigation button
        // redirect to map fragment and show route
        final OnClickListener mapListener = new OnClickListener() {
            @Override
            public void onClick(View mapbutton) {
            	Toast.makeText(mapbutton.getContext(), "Navigating to Mensa", Toast.LENGTH_SHORT).show();
            }
        };
        mapbutton.setOnClickListener(mapListener);
        
        // set the click listener for the favorite button
        final OnCheckedChangeListener starListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Mensa mensa = mMensaList.get(position);
				mensa.setIsFavorite(isChecked);
				mFragment.updateFavorite(mensa);
			}
        };
        starcheckbox.setOnCheckedChangeListener(starListener);
            
		return rowView;
	}
	@Override
	public int getCount() {
		return mMensaList.size();
	}
	@Override
	public Mensa getItem(int position) {
		return mMensaList.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
}