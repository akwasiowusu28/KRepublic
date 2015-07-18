package com.republic.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.republic.entities.CorruptionType;
import com.republic.ui.R;
import com.republic.ui.adapters.MainPageListviewAdapter;
import com.republic.ui.support.NavigationHelper;
import com.republic.ui.support.Utils;

import static android.widget.AdapterView.OnItemClickListener;

/** *
 * Created by Akwasi Owusu on 7/9/15.
 */
public class ReportFragment extends Fragment {

    public static ReportFragment newInstance() {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView mainListView = (ListView) view.findViewById(R.id.mainpagelistview);
        mainListView.setAdapter(new MainPageListviewAdapter(getActivity(), NavigationHelper.getMainPageIcons(),
                NavigationHelper.getMainPageNavigationItems(), NavigationHelper.getMainPageDescriptions()));
        mainListView.setOnItemClickListener(listener);
    }

    private OnItemClickListener listener = new OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            FragmentManager fragmentTransaction = getActivity().getSupportFragmentManager();

            Fragment incidentDetail = IncidentDetail.newInstance();

            //Reason for accessing enum value with position is, the order of the list items matches
            // the order of the constants in the CorruptionType enum
            incidentDetail.getArguments().putSerializable(Utils.Constants.SELECTED_CORRUPTION_TYPE,
                    CorruptionType.values()[position]);

            fragmentTransaction.beginTransaction().
                    replace(R.id.container, incidentDetail).commit();
        }
    };
}
