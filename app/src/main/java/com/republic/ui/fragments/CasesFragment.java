package com.republic.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.republic.domain.Domain;
import com.republic.entities.Corruption;
import com.republic.support.OperationCallback;
import com.republic.support.RepublicFactory;
import com.republic.ui.R;
import com.republic.ui.adapters.CaseListAdapter;
import com.republic.ui.support.Utils;

import java.util.ArrayList;
import java.util.List;


public class CasesFragment extends Fragment {

    private List<Corruption> reportedCorruption;
    private Domain domain;
    private CaseListAdapter listAdapter;


    public static CasesFragment newInstance() {
        CasesFragment fragment = new CasesFragment();

        return fragment;
    }

    public CasesFragment() {
        super();
        reportedCorruption = new ArrayList<>();
        domain = RepublicFactory.getDomain();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cases, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListView();
        loadReportedCorruption();
    }

    private void setupListView(){
        listAdapter = new CaseListAdapter(getActivity(), reportedCorruption);
        View view = getView();
        if(view != null) {
            ListView listView = (ListView)view.findViewById(R.id.casesList);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(onItemClickListener);
        }
    }
    private void loadReportedCorruption(){
        String userId = Utils.readFromPref(getActivity(), Utils.Constants.USER_TOKEN);
       domain.loadAllUserCorruptions(userId, new OperationCallback<Corruption>() {
           @Override
           public void performOperation(List<Corruption> results) {
               reportedCorruption.addAll(results);
               listAdapter.notifyDataSetChanged();
           }
       });
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            String postId = reportedCorruption.get(i).getPostId();
            Utils.launchFacebookPage(getActivity(), Utils.Constants.FB_APP_POST+postId, Utils.Constants.FB_DOT_COM + postId);
        }
    };
}
