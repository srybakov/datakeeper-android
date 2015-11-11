package com.epicsquad.datakeeper.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.epicsquad.datakeeper.R;
import com.epicsquad.datakeeper.common.StableArrayAdapter;
import com.epicsquad.datakeeper.model.domain.SMS;
import com.epicsquad.datakeeper.service.SMSService;
import com.epicsquad.datakeeper.service.impl.SMSServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SmsesFragment extends Fragment {

    private SMSService smsService = new SMSServiceImpl();

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState){
        return inflater.inflate(R.layout.smses, container,
                false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView listview = (ListView) view.findViewById(R.id.sms_list_view);
        Set<SMS> smses = smsService.findAll();
        List<String> listToDisplay = new ArrayList<String>();
        for (SMS sms : smses){
            listToDisplay.add(sms.toString());
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, listToDisplay);
        listview.setAdapter(adapter);

    }
}
