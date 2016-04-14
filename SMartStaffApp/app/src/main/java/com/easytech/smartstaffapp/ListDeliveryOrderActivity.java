package com.easytech.smartstaffapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.easytech.smartstaffapp.pojo.DeliveryOrder;
import com.easytech.smartstaffapp.pojo.Employee;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
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
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class ListDeliveryOrderActivity extends AppCompatActivity {

    private ListView deliveryOrderList;
    private View mProgressView;
    private View mSearchFormView;

    private Employee employee;
    private List<DeliveryOrder> deliveryOrder;

    private DeliveryOrderTask mAuthTask = null;

    private static final String TAG = "DeliveryOrderMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_delivery_order);

        deliveryOrderList = (ListView) findViewById(R.id.delivery_order_list);
        mSearchFormView = findViewById(R.id.do_search_form);
        mProgressView = findViewById(R.id.do_search_progress);

        SharedPreferences sharedpreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        String customerJson = sharedpreferences.getString(Constants.Customer, null);
        if(customerJson == null) {
            goToLoginActivity();
        } else {
            Gson gson = new Gson();
            employee = gson.fromJson(customerJson, Employee.class);

            if(employee.getWarehouseId() != null) {
                showProgress(true);
                mAuthTask = new DeliveryOrderTask(this, employee.getEmpId(), employee.getWarehouseId());
                mAuthTask.execute((Void) null);
            } else Toast.makeText(this, "You do not have the required access.", Toast.LENGTH_LONG).show();

        }
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class DeliveryOrderTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private String output;
        private final Long empId;
        private final Long warehouseId;

        DeliveryOrderTask(Context context, Long empId, Long warehouseId) {
            this.context = context;
            this.empId = empId;
            this.warehouseId = warehouseId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(Constants.urlString + "MobileListDeliveryOrder?empId=" + empId + "&warehouseId=" + warehouseId + "&canclled=" + false + "&delivered" + false);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);

                System.out.println("DO o/p :" + output);

                if (output == null || output.isEmpty()) return false;
                else {
                    System.out.println("DO : " + output);

                    Gson gson = new Gson();
                    deliveryOrder = gson.fromJson(output, new TypeToken<List<DeliveryOrder>>() {
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
            mAuthTask = null;
            showProgress(false);
            if (success) {
                deliveryOrderList.setAdapter(new DeliveryOrderAdapter(deliveryOrder, getLayoutInflater()));
                deliveryOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        Intent productDetailsIntent = new Intent(getBaseContext(), DeliveryOrderDetailsActivity.class);
                        productDetailsIntent.putExtra(Constants.delivery, deliveryOrder.get(position));
                        startActivity(productDetailsIntent);
                    }
                });
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

