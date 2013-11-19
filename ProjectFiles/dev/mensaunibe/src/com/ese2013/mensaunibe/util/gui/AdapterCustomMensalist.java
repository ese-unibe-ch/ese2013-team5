package com.ese2013.mensaunibe.util.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ese2013.mensaunibe.ActivityMain;
import com.ese2013.mensaunibe.FragmentMensaList;
import com.ese2013.mensaunibe.R;
import com.ese2013.mensaunibe.model.ModelMensa;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterCustomMensalist extends BaseAdapter {
	private Context context;
	private List<ModelMensa> mensas;
	private LayoutInflater inflater;
	private FragmentMensaList fragment;
	private int resource;

	public AdapterCustomMensalist(Context context, FragmentMensaList fragment, ArrayList<ModelMensa> list, int resource) {
		super();
		this.context = context; // ActivityMain
		this.mensas = list;
		this.fragment = fragment; // FragmentMensaList
		this.resource = resource; // the xml layout file, like this it gets dynamic
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource, parent, false);
        
        ModelMensa mensa = mensas.get(position);
        
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
            	fragment.selectItem(position);
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
        final OnClickListener starListener = new OnClickListener() {
            @Override
            public void onClick(View starbutton) {
            	ModelMensa mensa = mensas.get(position);
//            	mensa.setFavorite(!m.isFavorite());
            	mensa.setFavorite(mensa.getId());
            	//Toast.makeText(starbutton.getContext(), "Mensa set to Favorite", Toast.LENGTH_SHORT).show();
            }
        };
        starcheckbox.setOnClickListener(starListener);
            
		return rowView;
	}
	@Override
	public int getCount() {
		return mensas.size();
	}
	@Override
	public ModelMensa getItem(int position) {
		return mensas.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
}