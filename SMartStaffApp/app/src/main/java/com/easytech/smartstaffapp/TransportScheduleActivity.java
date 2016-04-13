package com.easytech.smartstaffapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easytech.smartstaffapp.pojo.DeliveryOrder;
import com.easytech.smartstaffapp.pojo.DeliverySchedule;
import com.easytech.smartstaffapp.pojo.TimeSlot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransportScheduleActivity extends AppCompatActivity {

    private View mProgressView;
    private View mSearchFormView;
    private TextView scheduleString;

    private ScheduleTask mAuthTask = null;

    private List<DeliverySchedule> deliveryScheduleList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_schedule);

        scheduleString = (TextView) findViewById(R.id.schedule_string);

        mSearchFormView = findViewById(R.id.search_form);
        mProgressView = findViewById(R.id.search_progress);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ScheduleTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private String output;
        private final Long empId;
        private final String vehiclePlateNum;

        ScheduleTask(Context context, Long empId, String vehiclePlateNum) {
            this.context = context;
            this.empId = empId;
            this.vehiclePlateNum = vehiclePlateNum;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(Constants.urlString + "MobileVehicleSchedule?empId=" + empId + "&vehiclePlateNum=" + vehiclePlateNum);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);

                //System.out.println(output);

                if (output == null) return false;
                else {
                    Gson gson = new Gson();
                    deliveryScheduleList = gson.fromJson(output, new TypeToken<List<DeliverySchedule>>() {
                    }.getType());

                    return true;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                String format = "";
                int count_ds = 0;
                for(DeliverySchedule deliverySchedule : deliveryScheduleList) {
                    Date date = new Date(deliverySchedule.getDeliveryDate());
                    SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat);
                    format += ++count_ds + " " + deliverySchedule.getId() + " : " + sdf.format(date) + "\n";
                    int count_ts = 0;
                    for(TimeSlot timeSlot : deliverySchedule.getTimeSlots()) {
                        Date date1 = new Date(timeSlot.getTimeSlotNum());
                        SimpleDateFormat sdf1 = new SimpleDateFormat(Constants.dateFormat);
                        format += count_ds + "." + ++count_ts + " " + timeSlot.getId() + " : " + sdf1.format(date1) + "\n";
                        int count_do = 0;
                        for(DeliveryOrder deliveryOrder : timeSlot.getDeliveryOrders()) {
                            format += count_ds + "." + count_ts + "." + ++count_do + " " + deliveryOrder.getId() + " : " + deliveryOrder.getOrigin() + " - " + deliveryOrder.getDestination() + "\n";
                        }
                    }
                }

                scheduleString.setText(format);
            } else {
                Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSearchFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSearchFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSearchFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mSearchFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
