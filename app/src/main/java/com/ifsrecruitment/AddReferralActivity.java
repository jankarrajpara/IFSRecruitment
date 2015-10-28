package com.ifsrecruitment;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ifsrecruitment.R;
import com.ifsrecruitment.util.Constant;
import com.ifsrecruitment.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.InjectView;

public class AddReferralActivity extends BaseActivity {

    @InjectView(R.id.etFirstName)
    EditText etFirstName;
    @InjectView(R.id.etLastName)
    EditText etLastName;
    @InjectView(R.id.etEmail)
    EditText etEmail;
    @InjectView(R.id.etPhone)
    EditText etPhone;
    @InjectView(R.id.etCity)
    EditText etCity;
    @InjectView(R.id.etCampus)
    EditText etCampus;
    @InjectView(R.id.etNote)
    EditText etNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_referral);

        actionBarAndButtonActions();
    }

    private void actionBarAndButtonActions() {

        ActionBar actionBar = getActionBar();
        // add the custom view to the action bar
        actionBar.setCustomView(R.layout.custom_addreferral_actionbar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayHomeAsUpEnabled(false);

        RelativeLayout rlSave = (RelativeLayout) actionBar.getCustomView().findViewById(R.id.rlSave);
        rlSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Util.hideKeyboard(AddReferralActivity.this);

                if (isValidate()) {
                    if (Util.isOnline(getApplicationContext()))
                        new AddReferral(getText(etFirstName), getText(etLastName), getText(etEmail), getText(etPhone), getText(etCity), getText(etCampus), getText(etNote)).execute();
                        //new FileUploadTask().execute();
                    else toast(Constant.network_error);
                }

            }
        });

    }

    class AddReferral extends AsyncTask<Void, String, String> {

        String firstname, lastname, email, phone, city, campus, notes;
        ProgressDialog progressDialog;
        String response;

        public AddReferral(String firstname, String lastname, String email, String phone, String city, String campus, String notes) {
            this.firstname = firstname;
            this.lastname = lastname;
            this.email = email;
            this.phone = phone;
            this.city = city;
            this.campus = campus;
            this.notes = notes;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddReferralActivity.this, R.style.MyTheme);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(Void... params) {

            try {
                JSONObject json = new JSONObject();
                json.put("FirstName", firstname);
                json.put("LastName", lastname);
                json.put("Email", email);
                json.put("Phone", phone);
                json.put("City", city);
                json.put("Campus", campus);
                json.put("RecruiterID", "1");
                json.put("RecruiterName", "xyz");

                json.put("ReferralCode", firstname);
                json.put("Notes", notes);
                json.put("ReferrerFirstName", email);
                json.put("ReferrerLastName", phone);
                json.put("ReferrerEmail", email);
                json.put("Source", campus);
                json.put("URL", "1");
                json.put("ZipCode", "xyz");
                json.put("SeasonID", "16");
                json.put("SeasonRecordID", "1");
                json.put("ResultID", "17");
                json.put("TimeStamp", "2015-09-03T02:50:17.3664799+00:00");
                json.put("LoggedinUserID", city);
                json.put("LoggedinName", campus);
                json.put("SessionID", "1");

                response = Util.callAPI(Constant.URL + "referral", 2, json, null, getApplicationContext());
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
                String PrincipalID = jObj.optString("PrincipalID");

                if (PrincipalID.length() > 0) {
                    Util.WriteSharePrefrence(getApplicationContext(), Constant.SHRED_PR.KEY_RELOAD, "1");
                    finish();
                } else toast("Failed");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean isValidate() {

        if (isEmpty(getText(etFirstName))) {
            toast("please enter firstname");
            return false;
        }
        if (isEmpty(getText(etLastName))) {
            toast("please enter lastname");
            return false;
        }
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(getText(etEmail)).matches() == false) {
            toast("please enter valid email");
            return false;
        }
        if (isEmpty(getText(etPhone))) {
            toast("please enter phone");
            return false;
        }
        if (isEmpty(getText(etCity))) {
            toast("please enter city");
            return false;
        }
        if (isEmpty(getText(etCampus))) {
            toast("please enter campus");
            return false;
        }
        if (isEmpty(getText(etNote))) {
            toast("please enter note");
            return false;
        }
        return true;
    }

}
