package com.ifsrecruitment.util;

/**
 * Created by BVK on 09-07-2015.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class Util {


    private static final String TAG = Util.class.getSimpleName();

    public static boolean isOnline(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static String postData(List<NameValuePair> nameValuePairs, String url) {
        // TODO Auto-generated method stub
        String responseStr = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        Log.e("reqURL", "" + url);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                responseStr = EntityUtils.toString(resEntity).trim();
                // you can add an if statement here and do other actions based
                // on the response
            }

            Log.e("Response-->", responseStr);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return responseStr;
    }

    public static String makeServiceCall(String url, int method,
                                         List<NameValuePair> params) {

        String response = null;
        int GET = 1;
        int POST = 2;
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader("Content-Type", "application/json");
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }

                Log.i("Url", url);
                HttpGet httpGet = new HttpGet(url);

                httpResponse = httpClient.execute(httpGet);

            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;

    }

    public static String callAPI(String URL, int method, JSONObject jsonObject, List<NameValuePair> params, Context context) {
        String result = null;

        int GET = 1;
        int POST = 2;

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpEntity httpEntity = null;
        HttpResponse httpResponse = null;

        if (params == null) {
            params = new ArrayList<NameValuePair>();
        }

        params.add(new BasicNameValuePair("Season", "" + ReadSharePrefrence(context, Constant.SHRED_PR.KEY_SEASON)));
        params.add(new BasicNameValuePair("AUTH_TOKEN", Util.ReadSharePrefrence(context, Constant.SHRED_PR.KEY_TOKEN)));

        if (method == GET) {
            try {

                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    URL += "?" + paramString;
                }

                Log.i("Url", URL);
                HttpGet httpGet = new HttpGet(URL);
                httpGet.setHeader(HTTP.CONTENT_TYPE, "application/json");

                httpResponse = httpClient.execute(httpGet);
                httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);
                Log.e("response:", "" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (method == POST) {
            try {

                HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 15000); // Timeout
                // Limit
                HttpPost post = new HttpPost(URL);
                StringEntity se = new StringEntity(jsonObject.toString());

                post.setHeader(HTTP.CONTENT_TYPE, "application/json");
                post.setHeader("Season", "" + ReadSharePrefrence(context, Constant.SHRED_PR.KEY_SEASON));
                post.setHeader("AUTH_TOKEN", "" + Util.ReadSharePrefrence(context, Constant.SHRED_PR.KEY_TOKEN));
                post.setEntity(new UrlEncodedFormEntity(params));
                post.setEntity(se);
                httpResponse = httpClient.execute(post);
                httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);
                Log.e("response:", "" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static void writeToFile(String data, String file_name,
                                   Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    context.openFileOutput(file_name, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String readFromFile(String file_name, Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(file_name);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(
                        inputStream);
                BufferedReader bufferedReader = new BufferedReader(
                        inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public static void WriteSharePrefrence(Context context, String key,
                                           String values) {
        @SuppressWarnings("static-access")
        SharedPreferences write_Data = context.getSharedPreferences(
                Constant.SHRED_PR.SHARE_PREF, context.MODE_PRIVATE);
        Editor editor = write_Data.edit();
        editor.putString(key, values);
        editor.apply();
    }

    public static String ReadSharePrefrence(Context context, String key) {
        @SuppressWarnings("static-access")
        SharedPreferences read_data = context.getSharedPreferences(
                Constant.SHRED_PR.SHARE_PREF, context.MODE_PRIVATE);

        return read_data.getString(key, "");
    }

    public static void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof Button /*etc.*/)
                ((TextView) v).setTypeface(font);
            else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font);
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        return Uri.parse(path);
    }

    public static void hideKeyboard(Activity context) {
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}