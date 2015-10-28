package com.ifsrecruitment;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ifsrecruitment.R;
import com.ifsrecruitment.adapter.FragmentAdapter;
import com.ifsrecruitment.adapter.ReferralAdapter;
import com.ifsrecruitment.fragment.RecruitFragment;
import com.ifsrecruitment.fragment.ReferralFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class HomeActivity extends FragmentActivity {

    @InjectView(R.id.view1)
    View view1;
    @InjectView(R.id.view2)
    View view2;
    @InjectView(R.id.rlAdd)
    RelativeLayout rlAdd;

    TextView tvTitle;
    FragmentAdapter pageAdapter;
    View v;
    public static ViewPager pager;
    public static int currentPosition = 0;
    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.inject(this);

        actionBarAndButtonActions();
        init();

    }

    @OnClick(R.id.rlAdd)
    @SuppressWarnings("unused")
    public void Add(View view) {
        startActivity(AddReferralActivity.class);
    }

    @OnClick(R.id.rlReferral)
    @SuppressWarnings("unused")
    public void Invited(View view) {
        pager.setCurrentItem(1);
    }

    @OnClick(R.id.rlrecruits)
    @SuppressWarnings("unused")
    public void Existing(View view) {
        pager.setCurrentItem(0);
    }

    private void init() {

        fragmentManager = getFragmentManager();
        pager = (ViewPager) findViewById(R.id.viewpagerDemo);
        final List<Fragment> fragmentList = getFragments();
        pageAdapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList);
        pager.setAdapter(pageAdapter);
        pager.setCurrentItem(0);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int pageNo) {
                currentPosition = pageNo;
                switch (currentPosition) {
                    case 0:
                        tvTitle.setText("Recruits");
                        view1.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.GONE);
                        rlAdd.setVisibility(View.GONE);
                        break;
                    case 1:
                        tvTitle.setText("My Referral");
                        view1.setVisibility(View.GONE);
                        view2.setVisibility(View.VISIBLE);
                        rlAdd.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(RecruitFragment.newInstance());
        fragmentList.add(ReferralFragment.newInstance());

        return fragmentList;
    }

    protected void startActivity(Class klass) {
        startActivity(new Intent(this, klass));
    }

    private void actionBarAndButtonActions() {

        ActionBar actionBar = getActionBar();
        // add the custom view to the action bar
        actionBar.setCustomView(R.layout.custome_home_actionbar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayHomeAsUpEnabled(false);

        tvTitle = (TextView) actionBar.getCustomView()
                .findViewById(R.id.tvTitle);

        RelativeLayout rlStats = (RelativeLayout) actionBar.getCustomView().findViewById(R.id.rlStats);
        rlStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(StatsActivity.class);
            }
        });


    }

}
