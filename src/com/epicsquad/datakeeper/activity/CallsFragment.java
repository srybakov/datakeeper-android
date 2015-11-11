package com.epicsquad.datakeeper.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.epicsquad.datakeeper.R;
import com.epicsquad.datakeeper.common.StableArrayAdapter;
import com.epicsquad.datakeeper.model.domain.Call;
import com.epicsquad.datakeeper.service.CallService;
import com.epicsquad.datakeeper.service.impl.CallServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CallsFragment extends Fragment {

    private CallService callService = new CallServiceImpl();

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState){
        return inflater.inflate(R.layout.calls, container,
                false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView listview = (ListView) view.findViewById(R.id.calls_list_view);
        Set<Call> calls = callService.findAll();
        List<String> listToDisplay = new ArrayList<String>();
        for (Call call : calls){
            listToDisplay.add(call.toString());
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, listToDisplay);
        listview.setAdapter(adapter);

    }

}
