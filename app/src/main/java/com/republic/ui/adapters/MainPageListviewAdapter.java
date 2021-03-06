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
 * Created by Akwasi Owusu on 7/6/15.
 */
public class MainPageListviewAdapter extends ArrayAdapter<Integer> {

    private Context context;
    private int resource;
    private Integer[] navigationIcons;
    private Integer[] navigationItems;
    private Integer[] navigationItemsDesc;

    public MainPageListviewAdapter(Context context,
                                   Integer[] navigationIcons, Integer[] navigationItems,
                                   Integer[] navigationItemsDesc) {
        super(context, R.layout.layout_navigation_listview, navigationItems);
        this.context = context;
        resource = R.layout.layout_navigation_listview;
        this.navigationItems = navigationItems;
        this.navigationIcons = navigationIcons;
        this.navigationItemsDesc = navigationItemsDesc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = convertView;

        if (rowView == null) {
            ViewHolder viewHolder = new ViewHolder();
            rowView = inflater.inflate(resource, parent, false);

            viewHolder.firstLinetextView = (TextView) rowView
                    .findViewById(R.id.firstLine);
            viewHolder.secondLinetextView = (TextView) rowView
                    .findViewById(R.id.secondLine);

            viewHolder.mainIconImageView = (ImageView) rowView
                    .findViewById(R.id.icon);
            rowView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) rowView.getTag();

        viewHolder.mainIconImageView
                .setImageResource(navigationIcons[position]);

        viewHolder.firstLinetextView.setText(context
                .getString(navigationItems[position]));
        viewHolder.secondLinetextView.setText(context
                .getString(navigationItemsDesc[position]));

        return rowView;
    }

    private static class ViewHolder {
        public TextView firstLinetextView;
        public TextView secondLinetextView;
        public ImageView mainIconImageView;
    }
}
