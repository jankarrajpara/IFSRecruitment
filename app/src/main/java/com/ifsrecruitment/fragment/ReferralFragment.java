package com.ifsrecruitment.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ifsrecruitment.R;
import com.ifsrecruitment.adapter.RecruitsAdapter;
import com.ifsrecruitment.adapter.ReferralAdapter;
import com.ifsrecruitment.util.Constant;
import com.ifsrecruitment.util.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Sadiwala on 8/28/2015.
 */
public class ReferralFragment extends Fragment {

    @InjectView(R.id.tvError)
    TextView tvError;
    @InjectView(R.id.lvReferral)
    ListView lvReferral;
    ArrayList<HashMap<String, String>> listReferrals = new ArrayList<>();

    public static ReferralFragment newInstance() {
        ReferralFragment fragment = new ReferralFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_referral, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (Util.isOnline(getActivity()))
            new getReferrals().execute();
        else Toast.makeText(getActivity(), Constant.network_error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();

        if (Util.ReadSharePrefrence(getActivity(), Constant.SHRED_PR.KEY_RELOAD).equals("1")) {
            Util.WriteSharePrefrence(getActivity(), Constant.SHRED_PR.KEY_RELOAD, "0");
            if (Util.isOnline(getActivity()))
                new getReferrals().execute();
            else Toast.makeText(getActivity(), Constant.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    class getReferrals extends AsyncTask<Void, String, String> {

        ProgressDialog progressDialog;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(Void... params) {

            try {
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("page", "1"));
                params1.add(new BasicNameValuePair("pagesize", "10"));
                params1.add(new BasicNameValuePair("sort", "0"));
                params1.add(new BasicNameValuePair("where", ""));
                params1.add(new BasicNameValuePair("searchterm", ""));
                params1.add(new BasicNameValuePair("prefilter", ""));
                params1.add(new BasicNameValuePair("orderby", ""));

                response = Util.callAPI(Constant.URL + "referralview/myreferrals", 1, null, params1, getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }

        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (progressDialog != null) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
            }

            listReferrals.clear();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.optJSONArray("Result");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("ReferralID", "" + jsonObject1.optString("ReferralID"));
                        hashMap.put("FirstName", "" + jsonObject1.optString("FirstName"));
                        hashMap.put("LastName", "" + jsonObject1.optString("LastName"));
                        hashMap.put("Email", "" + jsonObject1.optString("Email"));

                        listReferrals.add(hashMap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            lvReferral.setAdapter(new ReferralAdapter(getActivity(),listReferrals));

            if (listReferrals.size() == 0) tvError.setVisibility(View.VISIBLE);
            else tvError.setVisibility(View.GONE);

        }
    }

}
