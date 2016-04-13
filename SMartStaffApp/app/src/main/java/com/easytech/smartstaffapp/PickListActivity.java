package com.easytech.smartstaffapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.easytech.smartstaffapp.pojo.Employee;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * A login screen that offers login via email/password.
 */
public class PickListActivity extends AppCompatActivity implements View.OnClickListener {

    private PickListTask mAuthTask = null;

    // UI references.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private EditText qrCodeInput;

    private View mProgressView;
    private View mSearchFormView;

    private Employee employee;

    private static final String TAG = "PickListMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_list);

        qrCodeInput = (EditText) findViewById(R.id.picklist_qrcode);
        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        findViewById(R.id.read_qrcode).setOnClickListener(this);
        findViewById(R.id.submit_qrcode).setOnClickListener(this);

        mSearchFormView = findViewById(R.id.search_form);
        mProgressView = findViewById(R.id.search_progress);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_qrcode) {
            // launch barcode activity.
            Intent intent = new Intent(this, BarcodeScannerActivity.class);
            intent.putExtra(BarcodeScannerActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(BarcodeScannerActivity.UseFlash, useFlash.isChecked());

            startActivityForResult(intent, Constants.RC_BARCODE_CAPTURE);
        }

        if(v.getId() == R.id.submit_qrcode) {
            String code = qrCodeInput.getText().toString();

            View focusView = null;
            boolean cancel = false;

            // Check for a valid code.
            if (TextUtils.isEmpty(code)) {
                qrCodeInput.setError(getString(R.string.error_field_required));
                focusView = qrCodeInput;
                cancel = true;
            }

            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.

                SharedPreferences sharedpreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
                String customerJson = sharedpreferences.getString(Constants.Customer, null);
                if(customerJson == null) {
                    goToLoginActivity();
                } else {
                    Gson gson = new Gson();
                    employee = gson.fromJson(customerJson, Employee.class);
                    if(employee.getWarehouseId() != null) {
                        showProgress(true);
                        mAuthTask = new PickListTask(this, code, employee.getEmpId(), employee.getWarehouseId());
                        mAuthTask.execute((Void) null);
                    } else
                        Toast.makeText(this, "You do not have the required access.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeScannerActivity.BarcodeObject);
                    Toast.makeText(PickListActivity.this, R.string.barcode_success, Toast.LENGTH_SHORT).show();
                    qrCodeInput.setText(barcode.displayValue);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    Toast.makeText(PickListActivity.this, R.string.barcode_failure, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                Toast.makeText(PickListActivity.this, String.format(getString(R.string.barcode_error), CommonStatusCodes.getStatusCodeString(resultCode)), Toast.LENGTH_SHORT).show();
                Log.d(TAG, String.format(getString(R.string.barcode_error), CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
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

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class PickListTask extends AsyncTask<Void, Void, Boolean> {

        private final Context context;
        private final String mCode;
        private Long empId;
        private Long warehouseId;
        private String output;

        PickListTask(Context context, String code, Long empId, Long warehouseId) {
            this.context = context;
            this.empId = empId;
            mCode = code;
            this.warehouseId = warehouseId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(Constants.urlString + "ListPickList?empId=" + empId + "&warehouseId=" + warehouseId);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);

                //System.out.println(output);

                if (output == null) return false;
                else  return true;
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
                goToListPickListActivity();
            } else {
                Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }

        private void goToListPickListActivity() {
            Intent intent = new Intent(context, ListPickListActivity.class);
            intent.putExtra(Constants.pickList, output);
            startActivity(intent);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

