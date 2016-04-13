package com.easytech.smartstaffapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easytech.smartstaffapp.pojo.DeliveryOrder;
import com.easytech.smartstaffapp.pojo.Employee;
import com.easytech.smartstaffapp.pojo.PickingList;
import com.google.gson.Gson;

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

public class DeliveryOrderDetailsActivity extends AppCompatActivity {

    private DeliveryOrder deliveryOrder;
    private Employee employee;

    private View mProgressView;
    private View mSearchFormView;

    private ListView customerDO;
    private ListView storeDO;
    private ListView warehouseDO;

    private TextView amtView;
    private TextView idView;
    private TextView dateTimeView;
    private TextView pathView;

    private UpdateDeliveryOrderTask mAuthTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        customerDO = (ListView) findViewById(R.id.do_customerinfo);
        warehouseDO = (ListView) findViewById(R.id.do_warehouseinfo);
        storeDO = (ListView) findViewById(R.id.do_storeinfo);

        amtView = (TextView) findViewById(R.id.do_amt);
        idView = (TextView) findViewById(R.id.do_id);
        dateTimeView = (TextView) findViewById(R.id.do_datetime);
        pathView = (TextView) findViewById(R.id.do_path);

        deliveryOrder = (DeliveryOrder) getIntent().getSerializableExtra(Constants.delivery);
        SharedPreferences sharedpreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        String customerJson = sharedpreferences.getString(Constants.Customer, null);
        if(customerJson == null) {
            goToLoginActivity();
        } else {
            Gson gson = new Gson();
            employee = gson.fromJson(customerJson, Employee.class);

            idView.setText(deliveryOrder.getId().toString());
            pathView.setText(deliveryOrder.getOrigin() + " - " +deliveryOrder.getDestination());

            Date date = new Date(deliveryOrder.getDeliveryDate());
            SimpleDateFormat df2 = new SimpleDateFormat(Constants.dateFormat);

            if (deliveryOrder.getDeliveryTimeSlot() != null) dateTimeView.setText(df2.format(date) + ", " + deliveryOrder.getDeliveryTimeSlot().getTimeSlotNum());
            else dateTimeView.setText(df2.format(date));

            if (deliveryOrder.getType().equalsIgnoreCase("warehouseTostore")) {
                amtView.setText("Cages - " + deliveryOrder.getTotalCages() + "; Pallets - "+deliveryOrder.getTotalPallets());
                customerDO.setVisibility(View.GONE);
                storeDO.setVisibility(View.GONE);
                warehouseDO.setAdapter(new RequestAdapter(deliveryOrder.getRequests(), getLayoutInflater()));
            }
            else if (deliveryOrder.getType().equalsIgnoreCase("storeTostore")) {
                amtView.setText("Cages - " + deliveryOrder.getTotalCages() + "; Pallets - "+deliveryOrder.getTotalPallets());
                customerDO.setVisibility(View.GONE);
                warehouseDO.setVisibility(View.GONE);
                storeDO.setAdapter(new RequestAdapter(deliveryOrder.getRequests(), getLayoutInflater()));

            }
            else if (deliveryOrder.getType().equalsIgnoreCase("customer")) {
                storeDO.setVisibility(View.GONE);
                warehouseDO.setVisibility(View.GONE);
                customerDO.setAdapter(new RequestAdapter(deliveryOrder.getItems(), getLayoutInflater()));
            }

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProgress(true);
                    mAuthTask = new UpdateDeliveryOrderTask(getBaseContext(), employee.getEmpId(), deliveryOrder.getId());
                    mAuthTask.execute((Void) null);
                }
            });
        }
        mSearchFormView = findViewById(R.id.do_form);
        mProgressView = findViewById(R.id.do_progress);
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public class UpdateDeliveryOrderTask extends AsyncTask<Void, Void, Boolean> {

        private final Context context;
        private Long empId;
        private Long deliveryOrderId;
        private String output;

        UpdateDeliveryOrderTask(Context context, Long empId, Long deliveryOrderId) {
            this.context = context;
            this.empId = empId;
            this.deliveryOrderId = deliveryOrderId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(Constants.urlString + "MobileUpdateDeliveryOrderStatus?empId="
                        + empId + "&=deliveryOrderId"
                        + deliveryOrderId + "&status=Delivered");

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);

                //System.out.println(output);

                if (output == null) {
                    output = "Something went wrong, please try again!";
                    return false;
                }
                else if(output.equalsIgnoreCase("OK")) return true;
                else return false;
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
            showProgress(false);
            if (success) {
                finish();
                goToMainActivity();
            } else {
                Toast.makeText(context, output, Toast.LENGTH_SHORT).show();
            }
        }

        private void goToMainActivity() {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(Constants.success, "true");
            startActivity(intent);
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
