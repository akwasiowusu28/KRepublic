package com.republic.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.republic.ui.R;

/**
 * Created by aowusu on 7/24/2015.
 */
public class NavDrawerAdapter extends ArrayAdapter<Integer> {

    private Context context;
    private int resource;
    private Integer[] navigationIcons;
    private Integer[] navigationItems;

    public NavDrawerAdapter(Context context, Integer[] navigationItems, Integer[] navigationIcons) {
        super(context, R.layout.drawer_list_layout, navigationItems);
        this.context = context;
        resource = R.layout.drawer_list_layout;
        this.navigationItems = navigationItems;
        this.navigationIcons = navigationIcons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = convertView;

        if (rowView == null) {
            ViewHolder viewHolder = new ViewHolder();
            rowView = inflater.inflate(resource, parent, false);

            viewHolder.icon = (ImageView) rowView
                    .findViewById(R.id.item_icon);
            viewHolder.itemName = (TextView) rowView
                    .findViewById(R.id.item_name);

            rowView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) rowView.getTag();

        viewHolder.icon
                .setImageResource(navigationIcons[position]);

        viewHolder.itemName.setText(context
                .getString(navigationItems[position]));

        return rowView;
    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView itemName;
    }

}
