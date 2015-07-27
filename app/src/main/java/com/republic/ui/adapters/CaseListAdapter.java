package com.republic.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.republic.entities.Corruption;
import com.republic.ui.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Akwasi Owusu on 7/26/15.
 */
public class CaseListAdapter extends ArrayAdapter<Corruption> {

    private List<Corruption> cases;
    private Context context;
    private int resource;

    public CaseListAdapter(Context context, List<Corruption> cases) {
        super(context, R.layout.case_item_layout, cases);
        resource = R.layout.case_item_layout;
        this.context = context;
        this.cases = cases;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = convertView;

        if (rowView == null) {
            ViewHolder viewHolder = new ViewHolder();
            rowView = inflater.inflate(resource, parent, false);

            viewHolder.dateTextView = (TextView) rowView
                    .findViewById(R.id.reportDate);
            viewHolder.descriptionTextView = (TextView) rowView
                    .findViewById(R.id.description);

            rowView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) rowView.getTag();

        Corruption corruption = cases.get(position);

        viewHolder.dateTextView
                .setText(getDateString(corruption.getPostDate()));

        viewHolder.descriptionTextView.setText(getDescription(corruption));

        return rowView;
    }

    private String getDescription(Corruption corruption) {
        return corruption.getCorruptionType().toString() + " " +
                context.getString(R.string.at) + " " + corruption.getLocation();
    }

    private String getDateString(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(ViewHolder.MMM_DD_YYYY,
                Locale.getDefault());

        return dateFormatter.format(date);
    }

    private static class ViewHolder {
        public TextView dateTextView;
        public TextView descriptionTextView;
        public static final String MMM_DD_YYYY = "MMM dd, yyyy";
    }
}
