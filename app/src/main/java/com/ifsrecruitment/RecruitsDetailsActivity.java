package com.ifsrecruitment;

import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ifsrecruitment.R;

import java.util.HashMap;

import butterknife.InjectView;

public class RecruitsDetailsActivity extends BaseActivity {

    @InjectView(R.id.tvTitle)
    TextView tvTitle;
    @InjectView(R.id.tvDesc)
    TextView tvDesc;
    @InjectView(R.id.tvIFSNotes1)
    TextView tvIFSNotes1;
    @InjectView(R.id.tvRecruiterNotes1)
    TextView tvRecruiterNotes1;

    HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruits_details);

        actionBarAndButtonActions();

        hashMap = (HashMap<String, String>) getIntent().getSerializableExtra("map");
        if (hashMap != null) {
            setData();
        }

    }

    private void setData() {
        tvTitle.setText("" + hashMap.get("FirstName") + " " + hashMap.get("LastName"));
        tvDesc.setText("" + hashMap.get("eligibility") + "\n\n" + hashMap.get("Deposit"));
        tvIFSNotes1.setText("" + hashMap.get("IFSNotes"));
        tvRecruiterNotes1.setText("" + hashMap.get("RecruiterNotes"));
    }

    private void actionBarAndButtonActions() {

        ActionBar actionBar = getActionBar();
        // add the custom view to the action bar
        actionBar.setCustomView(R.layout.custom_recruits_actionbar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayHomeAsUpEnabled(false);


    }

}
