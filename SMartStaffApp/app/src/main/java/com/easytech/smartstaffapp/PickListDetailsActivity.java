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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easytech.smartstaffapp.pojo.Employee;
import com.easytech.smartstaffapp.pojo.Item;
import com.easytech.smartstaffapp.pojo.PickingList;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PickListDetailsActivity extends AppCompatActivity {

    private PickingList pickingList;

    private TextView idView;
    private TextView doView;
    private TextView loadingbayView;
    private TextView pickingTimeView;
    private ListView itemView;
    private Button button;

    private View mProgressView;
    private View mSearchFormView;

    private Employee employee;

    private UpdatePickListTask mAuthTask = null;

    private static final String TAG = "PickListDetailsMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pickingList = (PickingList) getIntent().getSerializableExtra(Constants.pick);
        SharedPreferences sharedpreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        String customerJson = sharedpreferences.getString(Constants.Customer, null);
        if(customerJson == null) {
            goToLoginActivity();
        } else {
            Gson gson = new Gson();
            employee = gson.fromJson(customerJson, Employee.class);
        }

        idView = (TextView) findViewById(R.id.picklist_id);
        doView = (TextView) findViewById(R.id.delivery_order_id);
        loadingbayView = (TextView) findViewById(R.id.loading_bay);
        pickingTimeView = (TextView) findViewById(R.id.picking_time);
        button = (Button) findViewById(R.id.finish_picking);

        idView.setText(String.valueOf(pickingList.getId()));
        doView.setText(String.valueOf(pickingList.getDeliveryOrderId()));
        loadingbayView.setText(pickingList.getLoadingBay());
        pickingTimeView.setText(String.valueOf(pickingList.getPickingTime()));

        itemView = (ListView) findViewById(R.id.list_pick_items);

        if(pickingList.getItems().isEmpty()) Toast.makeText(this, "All Items have been picked!", Toast.LENGTH_LONG).show();

        else itemView.setAdapter(new ItemAdapter(pickingList.getItems(), getLayoutInflater()));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), BarcodeScannerActivity.class);
                intent.putExtra(BarcodeScannerActivity.AutoFocus, false);
                intent.putExtra(BarcodeScannerActivity.UseFlash, false);

                startActivityForResult(intent, Constants.RC_BARCODE_CAPTURE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress(true);
                mAuthTask = new UpdatePickListTask(getBaseContext(), employee.getEmpId(), pickingList.getId());
                mAuthTask.execute((Void) null);
            }
        });

        mSearchFormView = findViewById(R.id.pick_form);
        mProgressView = findViewById(R.id.pick_progress);
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeScannerActivity.BarcodeObject);
                    Toast.makeText(PickListDetailsActivity.this, R.string.barcode_success, Toast.LENGTH_SHORT).show();
                    //TODO remove picked item from Picking list
                    for(Item item : pickingList.getItems()) {
                        if(item.getItemUpc().equalsIgnoreCase(barcode.displayValue))
                            pickingList.getItems().remove(item);
                    }
                    if(pickingList.getItems().isEmpty()) Toast.makeText(this, "All Items have been picked!", Toast.LENGTH_LONG).show();

                    else itemView.setAdapter(new ItemAdapter(pickingList.getItems(), getLayoutInflater()));
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    Toast.makeText(PickListDetailsActivity.this, R.string.barcode_failure, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                Toast.makeText(PickListDetailsActivity.this, String.format(getString(R.string.barcode_error), CommonStatusCodes.getStatusCodeString(resultCode)), Toast.LENGTH_SHORT).show();
                Log.d(TAG, String.format(getString(R.string.barcode_error), CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public class UpdatePickListTask extends AsyncTask<Void, Void, Boolean> {

        private final Context context;
        private Long empId;
        private Long pickListId;
        private String output;

        UpdatePickListTask(Context context, Long empId, Long pickListId) {
            this.context = context;
            this.empId = empId;
            this.pickListId = pickListId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(Constants.urlString + "MobileUpdatePickListStatus?empId=" + empId + "&pickList=" + pickListId);

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
            mAuthTask = null;
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
