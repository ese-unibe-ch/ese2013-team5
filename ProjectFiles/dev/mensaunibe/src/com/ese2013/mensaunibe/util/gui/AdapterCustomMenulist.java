package com.ese2013.mensaunibe.util.gui;

import java.util.ArrayList;
import java.util.List;

import com.ese2013.mensaunibe.R;
import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterCustomMenulist extends BaseAdapter {
	private Context context;
	private List<Menu> menus;
	private LayoutInflater inflater;
	private int resource;

	public AdapterCustomMenulist(Context context, ArrayList<Menu> list, int resource) {
		super();
		this.context = context; // ActivityMain
		this.menus = list;
		this.resource = resource; // the xml layout file, like this it gets dynamic
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource, parent, false);
        
        LinearLayout grid = (LinearLayout) rowView.findViewById(R.id.list_grid);
        
        // the actual fields that contain text
        Menu menu = menus.get(position);
        if ( (TextView) rowView.findViewById(R.id.mensa) != null ) {
        	TextView mensa = (TextView) rowView.findViewById(R.id.mensa);
        	// get the mensa name for the menu overview (FragmentMenuList)
			Mensa mensaobj = menu.getMensa();
        	mensa.setText(mensaobj.getName());
        }
        TextView title = (TextView) rowView.findViewById(R.id.title);
        TextView desc = (TextView) rowView.findViewById(R.id.desc);
        TextView price = (TextView) rowView.findViewById(R.id.price);
        TextView date = (TextView) rowView.findViewById(R.id.date);
        
        title.setText(menu.getTitle());
        desc.setText(menu.getDesc());
        price.setText(menu.getPrice());
        date.setText(menu.getDate());

        // set the click listener for the list item
        // should open mensa details
        final OnClickListener rowListener = new OnClickListener() {
            @Override
            public void onClick(View rowView) {
            	// show the mensadetails for the clicked mensa
            	//fragment.selectItem(position);
            	// delete after developement, just to show that it works
            	Toast toast = Toast.makeText(rowView.getContext(),
        				"Menu clicked, show rating...",
        				Toast.LENGTH_SHORT);
        		toast.show();
            }
        };
        grid.setOnClickListener(rowListener);
            
		return rowView;
	}
	@Override
	public int getCount() {
		return menus.size();
	}
	@Override
	public Menu getItem(int position) {
		return menus.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
}