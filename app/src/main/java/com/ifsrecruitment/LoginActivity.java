package com.ifsrecruitment;


import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ifsrecruitment.adapter.ReferralAdapter;
import com.ifsrecruitment.util.Constant;
import com.ifsrecruitment.util.Util;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @InjectView(R.id.etUsername)
    EditText etUsername;
    @InjectView(R.id.etPassword)
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        actionBarAndButtonActions();

        etUsername.setText("leah@israelfreespirit.com");
        etPassword.setText("leah613613");
        // Crittercism.initialize(getApplicationContext(), "55ac180a63235a0f00ad8f5e");

        if (Util.isOnline(getApplicationContext()))
            new getSeason().execute();
        else toast(Constant.network_error);

    }

    @OnClick(R.id.rlSignIn)
    @SuppressWarnings("unused")
    public void SignIn(View view) {

        hideKeyboard();

        //startActivity(HomeActivity.class);

        if (isValidate()) {
            if (Util.isOnline(getApplicationContext()))
                new LogIn(getText(etUsername), getText(etPassword)).execute();
                //new FileUploadTask().execute();
            else toast(Constant.network_error);
        }
    }

    class LogIn extends AsyncTask<Void, String, String> {

        String email, password;
        ProgressDialog progressDialog;
        String response;

        public LogIn(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this, R.style.MyTheme);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(Void... params) {

            try {
                JSONObject json = new JSONObject();
                json.put("username", email);
                json.put("password", password);
                response = Util.callAPI(Constant.URL + "authentication", 2, json, null, getApplicationContext());
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

            try {
                JSONObject jObj = new JSONObject(result);
                String Token = jObj.optString("Token");

                if (Token.length() > 0) {
                    write(Constant.SHRED_PR.KEY_TOKEN, "" + Token);
                    write(Constant.SHRED_PR.KEY_IS_LOGGEDIN, "1");
                    startActivity(HomeActivity.class);
                    finish();
                } else toast("Login Failed");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getSeason extends AsyncTask<Void, String, String> {

        String response;

        @Override
        protected String doInBackground(Void... params) {

            try {
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                response = Util.callAPI(Constant.URL + "season", 1, null, params1, getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }

        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date currentDate = sdf.parse(sdf.format(new Date()));
                Log.e("Date", ">" + currentDate);

                JSONArray jsonArray = new JSONArray(result);
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        Date startDate = sdf.parse(jsonObject1.optString("startDate").substring(0, 10));
                        Date endDate = sdf.parse(jsonObject1.optString("endDate").substring(0, 10));

                        if (((currentDate.compareTo(startDate) > 0) || (currentDate.compareTo(startDate) == 0)) && (currentDate.compareTo(endDate) < 0) || (currentDate.compareTo(endDate) == 0)) {
                            write(Constant.SHRED_PR.KEY_SEASON, "" + jsonObject1.optString("TaglitSeasonID"));
                        }


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.e("TaglitSeasonID", ">" + read(Constant.SHRED_PR.KEY_SEASON));
        }
    }


    private void actionBarAndButtonActions() {

        ActionBar actionBar = getActionBar();
        // add the custom view to the action bar
        actionBar.setCustomView(R.layout.custom_login_action_bar);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        actionBar.setDisplayHomeAsUpEnabled(false);

//        ImageButton action_button = (ImageButton) actionBar.getCustomView()
//                .findViewById(R.id.btn_action_bar);


    }


    private boolean isValidate() {
//        if (android.util.Patterns.EMAIL_ADDRESS.matcher(getText(etEmail)).matches() == false) {
//            toast("please enter valid email");
//            return false;
//        }
        if (isEmpty(getText(etUsername))) {
            toast("please enter username");
            return false;
        }
        if (isEmpty(getText(etPassword))) {
            toast("please enter password");
            return false;
        }
        return true;
    }

}
