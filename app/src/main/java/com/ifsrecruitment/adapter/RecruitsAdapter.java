package com.ifsrecruitment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ifsrecruitment.R;
import com.ifsrecruitment.RecruitsDetailsActivity;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Sadiwala on 8/28/2015.
 */
public class RecruitsAdapter extends BaseAdapter {

    private static final String TAG = RecruitsAdapter.class.getSimpleName();
    private static LayoutInflater inflater = null;
    private Context mContext;
    ArrayList<HashMap<String, String>> locallist;

    public RecruitsAdapter(Context ctx, ArrayList<HashMap<String, String>> locallist) {
        mContext = ctx;
        this.locallist = locallist;
        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return locallist.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.listraw_recruits, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final HashMap<String, String> hashMap = locallist.get(i);
        holder.tvTitle.setText("" + hashMap.get("FirstName") + " " + hashMap.get("LastName"));
        holder.tvDesc.setText("" + hashMap.get("eligibility") + " | " + hashMap.get("Deposit"));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, RecruitsDetailsActivity.class);
                intent.putExtra("map", hashMap);
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    public class ViewHolder {
        @InjectView(R.id.tvTitle)
        TextView tvTitle;
        @InjectView(R.id.tvDesc)
        TextView tvDesc;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}

