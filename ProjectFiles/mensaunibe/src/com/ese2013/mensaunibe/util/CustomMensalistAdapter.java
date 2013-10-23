package com.ese2013.mensaunibe.util;

import java.util.ArrayList;
import java.util.List;

import com.ese2013.mensaunibe.FragmentMensaDetails;
import com.ese2013.mensaunibe.FragmentMensaList;
import com.ese2013.mensaunibe.R;
import com.ese2013.mensaunibe.model.Mensa;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CustomMensalistAdapter extends BaseAdapter {
	private Context context;
	private List<Mensa> mensas;
	private LayoutInflater inflater;
	private FragmentMensaList fragment;

	public CustomMensalistAdapter(Context context, FragmentMensaList fragment, ArrayList<Mensa> list, int resource) {
		super();
		this.context = context;
		this.mensas = list;
		this.fragment = fragment;
		//this.fragment = context.getApplicationContext().
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
//		View rowView = convertView;
//		if (rowView == null) {
        	//View rowView = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_mensalist_item, parent, false);
        
        //view = inflater.inflate(R.layout.list_mensalist_item, null);
        //ImageButton star = (ImageButton) view.findViewById(R.id.button_star);
//        star.setOnClickListener(new OnClickListener() {
//
//        @Override
//        public void onClick(View viewIn) {
//        	//TODO: implement Button functionality
//        }});
//        
        LinearLayout grid = (LinearLayout) rowView.findViewById(R.id.list_grid);
        ImageButton mapbutton = (ImageButton) rowView.findViewById(R.id.button_map);
        ImageButton starbutton = (ImageButton) rowView.findViewById(R.id.button_star);
        
        // the actual fields that contain text
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView address = (TextView) rowView.findViewById(R.id.address);
        TextView city = (TextView) rowView.findViewById(R.id.city);
        Mensa mensa = mensas.get(position);
        name.setText(mensa.getName());
        address.setText(mensa.getStreet());
        city.setText(mensa.getPlz());

        // set the click listener for the list item
        // should open mensa details
        final OnClickListener rowListener = new OnClickListener() {
            @Override
            public void onClick(View rowView) {
            	// show the mensadetails for the clicked mensa
            	fragment.selectItem(position);
            	// delete after developement, just to show that it works
            	Toast toast = Toast.makeText(rowView.getContext(),
        				"Mensa clicked, show details",
        				Toast.LENGTH_SHORT);
        		toast.show();
            }
        };
        grid.setOnClickListener(rowListener);
        
        // set the click listener for the navigation button
        // redirect to map fragment and show route
        final OnClickListener mapListener = new OnClickListener() {
            @Override
            public void onClick(View mapbutton) {
            	Toast toast = Toast.makeText(mapbutton.getContext(),
        				"Navigating to Mensa",
        				Toast.LENGTH_SHORT);
        		toast.show();
            }
        };
        mapbutton.setOnClickListener(mapListener);
        
        // set the click listener for the favorite button
        // is it possible to have multiple favorite mensas or just one?
        final OnClickListener starListener = new OnClickListener() {
            @Override
            public void onClick(View starbutton) {
            	Toast toast = Toast.makeText(starbutton.getContext(),
        				"Mensa set to Favorite",
        				Toast.LENGTH_SHORT);
        		toast.show();
            }
        };
        starbutton.setOnClickListener(starListener);
            
		return rowView;
	}
	@Override
	public int getCount() {
		return mensas.size();
	}
	@Override
	public Mensa getItem(int position) {
		return mensas.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
}