package com.easytech.smartcustomerapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.easytech.smartcustomerapp.pojo.Customer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class EditUserActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserEditTask mAuthTask = null;

    // UI references.
    private EditText mFnameView;
    private EditText mLnameView;
    private EditText mPostalCodeView;
    private Spinner mGenderView;
    private Spinner mRaceView;
    private EditText mHomeAddressView;
    private EditText mContactView;
    private EditText mDobView;

    private Customer customer;
    SharedPreferences sharedpreferences;

    private View mProgressView;
    private View mEditFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        mFnameView = (EditText) findViewById(R.id.fname);
        mLnameView = (EditText) findViewById(R.id.lname);
        mPostalCodeView = (EditText) findViewById(R.id.postalcode);
        mHomeAddressView = (EditText) findViewById(R.id.address);
        mContactView = (EditText) findViewById(R.id.contact);
        mDobView = (EditText) findViewById(R.id.dob);

        sharedpreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);

        mGenderView = (Spinner) findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adapterGender =
                ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderView.setAdapter(adapterGender);

        mRaceView = (Spinner) findViewById(R.id.race);
        ArrayAdapter<CharSequence> adapterRace =
                ArrayAdapter.createFromResource(this, R.array.race_array, android.R.layout.simple_spinner_item);
        adapterRace.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRaceView.setAdapter(adapterRace);

        SharedPreferences sharedpreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        String customerJson = sharedpreferences.getString(Constants.Customer, null);
        if (customerJson == null) {
            goToLoginActivity();
        } else {
            Gson gson = new Gson();
            customer = gson.fromJson(customerJson, Customer.class);

            System.out.print(customer.toString());

            if (customer.getFname() != null)
                mFnameView.setText(customer.getFname());
            if (customer.getLname() != null)
                mLnameView.setText(customer.getLname());
            if (customer.getAddress() != null)
                mHomeAddressView.setText(customer.getAddress());
            if (customer.getPostalCode() != null)
                mPostalCodeView.setText(customer.getPostalCode());
            if (customer.getContact() != null)
                mContactView.setText(customer.getContact());
            if (customer.getDob() != null)
                mDobView.setText(customer.getDob().toString());
        }

        Button mEmailSignUpButton = (Button) findViewById(R.id.email_sign_up_button);
        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptEditCustomer();
            }
        });

        mEditFormView = findViewById(R.id.edit_form);
        mProgressView = findViewById(R.id.edit_progress);
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptEditCustomer() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mFnameView.setError(null);
        mLnameView.setError(null);
        mPostalCodeView.setError(null);
        mHomeAddressView.setError(null);
        mContactView.setError(null);

        // Store values at the time of the Edit attempt.
        String fname = mFnameView.getText().toString();
        String lname = mLnameView.getText().toString();
        String postalCode = mPostalCodeView.getText().toString();
        String homeAddress = mHomeAddressView.getText().toString();
        String contact = mContactView.getText().toString();
        String gender = mGenderView.getSelectedItem().toString();
        String race = mRaceView.getSelectedItem().toString();
        String dob = mDobView.getText().toString();


        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(fname)) {
            mFnameView.setError(getString(R.string.error_field_required));
            focusView = mFnameView;
            cancel = true;
        } else if (TextUtils.isEmpty(lname)) {
            mLnameView.setError(getString(R.string.error_field_required));
            focusView = mLnameView;
            cancel = true;
        } else if (TextUtils.isEmpty(postalCode)) {
            mPostalCodeView.setError(getString(R.string.error_field_required));
            focusView = mPostalCodeView;
            cancel = true;
        } else if (TextUtils.isEmpty(homeAddress)) {
            mHomeAddressView.setError(getString(R.string.error_field_required));
            focusView = mHomeAddressView;
            cancel = true;
        } else if (TextUtils.isEmpty(contact)) {
            mContactView.setError(getString(R.string.error_field_required));
            focusView = mContactView;
            cancel = true;
        } else if (TextUtils.isEmpty(dob)) {
            mDobView.setError(getString(R.string.error_field_required));
            focusView = mDobView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt Signup and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user Signup attempt.
            showProgress(true);

            //System.out.println("SignupActivty 1" + email + password+ fname +lname+ postalCode+ homeAddress + contact+ gender+ race+ dob);

            customer.setDob(Long.parseLong(dob.replace("/", "")));
            customer.setContact(contact);
            customer.setAddress(homeAddress);
            customer.setGender(gender);
            customer.setRace(race);
            customer.setPostalCode(postalCode);
            customer.setFname(fname);
            customer.setLname(lname);

            Gson gson = new GsonBuilder().serializeNulls().create();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            String json = gson.toJson(customer);
            System.out.println("UserEditActivity : " + json);
            editor.putString(Constants.Customer, json);
            editor.putBoolean(Constants.loggedIn, true);
            editor.commit();

            mAuthTask = new UserEditTask(this, fname, lname, postalCode, homeAddress, contact, gender, race, dob);
            mAuthTask.execute((Void) null);
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

            mEditFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mEditFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mEditFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mEditFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserEditTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private String fname;
        private String lname;
        private String postalCode;
        private String homeAddress;
        private String contact;
        private String gender;
        private String race;
        private String dob;

        UserEditTask(Context context, String fname, String lname, String postalCode, String homeAddress, String contact, String gender, String race, String dob) {
            this.context = context;
            this.fname = fname;
            this.lname = lname;
            this.postalCode = postalCode;
            this.homeAddress = homeAddress;
            this.contact = contact;
            this.gender = gender;
            this.race = race;
            this.dob = dob;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String output;
            try {

                HttpClient httpClient = new DefaultHttpClient();
                /*HttpPost httpPost = new HttpPost(URLEncoder.encode(urlString +
                        "?email=" + email +
                        "&password=" + password +
                        "&race=" + race +
                        "&dob=" + dob +
                        "&gender=" + gender +
                        "&address=" + homeAddress +
                        "&postalCode=" + postalCode +
                        "&contact=" + contact +
                        "&fname=" + fname +
                        "&lname=" + lname, "UTF-8"));
*/
                HttpPost httpPost = new HttpPost(Constants.urlString + "EditCustomerProfile");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email", customer.getEmail()));
                nameValuePairs.add(new BasicNameValuePair("race", race));
                nameValuePairs.add(new BasicNameValuePair("dob", dob));
                nameValuePairs.add(new BasicNameValuePair("gender", gender));
                nameValuePairs.add(new BasicNameValuePair("address", homeAddress));
                nameValuePairs.add(new BasicNameValuePair("postalCode", postalCode));
                nameValuePairs.add(new BasicNameValuePair("contact", contact));
                nameValuePairs.add(new BasicNameValuePair("fname", fname));
                nameValuePairs.add(new BasicNameValuePair("lname", lname));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                // Execute HTTP Post Request
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);

                System.out.println(output);

                if (output != null) {
                    return true;
                } else return false;

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
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
                goToUserProfileActivity();
            } else {
                Toast.makeText(context, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
            }
        }

        private void goToUserProfileActivity() {
            Intent intent = new Intent(context, UserProfileActivity.class);
            startActivity(intent);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

