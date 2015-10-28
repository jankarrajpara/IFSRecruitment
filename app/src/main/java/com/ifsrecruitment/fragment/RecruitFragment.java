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
public class RecruitFragment extends Fragment {

    @InjectView(R.id.tvError)
    TextView tvError;
    @InjectView(R.id.lvRecruits)
    ListView lvRecruits;

    ArrayList<HashMap<String, String>> listRecruiters = new ArrayList<>();

    public static RecruitFragment newInstance() {
        RecruitFragment fragment = new RecruitFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recruits, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (Util.isOnline(getActivity()))
            new getRecruiters().execute();
        else Toast.makeText(getActivity(), Constant.network_error, Toast.LENGTH_SHORT).show();

    }

    class getRecruiters extends AsyncTask<Void, String, String> {

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
//                params1.add(new BasicNameValuePair("where", ""));
//                params1.add(new BasicNameValuePair("searchterm", ""));
//                params1.add(new BasicNameValuePair("prefilter", ""));
//                params1.add(new BasicNameValuePair("orderby", ""));
                params1.add(new BasicNameValuePair("RecruiterID", "1"));

                response = Util.callAPI(Constant.URL + "recruiter", 1, null, params1, getActivity());
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

            listRecruiters.clear();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.optJSONArray("Result");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("ApplicantID", "" + jsonObject1.optString("ApplicantID"));
                        hashMap.put("FirstName", "" + jsonObject1.optString("FirstName"));
                        hashMap.put("LastName", "" + jsonObject1.optString("LastName"));
                        hashMap.put("Email", "" + jsonObject1.optString("Email"));
                        hashMap.put("Institution", "" + jsonObject1.optString("Institution"));
                        hashMap.put("State", "" + jsonObject1.optString("State"));
                        hashMap.put("City", "" + jsonObject1.optString("City"));
                        hashMap.put("Deposit", "" + jsonObject1.optString("Deposit"));
                        hashMap.put("eligibility", "" + jsonObject1.optString("eligibility"));
                        hashMap.put("IFSNotes", "" + jsonObject1.optString("IFSNotes"));
                        hashMap.put("RecruiterNotes", "" + jsonObject1.optString("RecruiterNotes"));

                        listRecruiters.add(hashMap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            lvRecruits.setAdapter(new RecruitsAdapter(getActivity(), listRecruiters));

            if (listRecruiters.size() == 0) tvError.setVisibility(View.VISIBLE);
            else tvError.setVisibility(View.GONE);

        }
    }

}
