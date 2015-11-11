package com.epicsquad.datakeeper.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.widget.TabHost;
import com.epicsquad.datakeeper.R;
import com.epicsquad.datakeeper.common.Utils;
import com.epicsquad.datakeeper.service.CallService;
import com.epicsquad.datakeeper.service.impl.CallServiceImpl;

import java.io.File;

public class LauncherActivity extends Activity{

    private static final String TAB_SMSES_FRAGMENT_TAG = "TAB_SMSES_FRAGMENT_TAG";
    private static final String TAB_CALLS_FRAGMENT_TAG = "TAB_CALLS_FRAGMENT_TAG";

    private Location oldLocation;
    private TabHost tabHost;

    private CallService callService = new CallServiceImpl();


    public static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Utils.setupApplication(getApplicationContext());
        context = getApplicationContext();
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
       /* createTab("Звонки", R.id.tab_calls);
        createTab("Смс", R.id.tab_smses);*/

    }

    private void createTab(final String name, final int id) {
        final TabHost.TabSpec tabSpec = tabHost
                .newTabSpec(Integer.toString(id));
        tabSpec.setIndicator(name);
        tabSpec.setContent(id);
        tabHost.addTab(tabSpec);

        final FragmentTransaction ft = getFragmentManager().beginTransaction();

        switch (id) {
            case R.id.tab_smses:
                SmsesFragment smsesFragment = new SmsesFragment();
                ft.replace(id, smsesFragment, TAB_SMSES_FRAGMENT_TAG).commit();
                break;
            case R.id.tab_calls:
                CallsFragment callsFragment = new CallsFragment();
                ft.replace(id, callsFragment, TAB_CALLS_FRAGMENT_TAG).commit();
                break;
        }
    }

}
