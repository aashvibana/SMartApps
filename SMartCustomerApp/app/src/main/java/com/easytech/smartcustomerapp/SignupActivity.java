package com.easytech.smartcustomerapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A Signup screen that offers Signup via email/password.
 */
public class SignupActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Keep track of the Signup task to ensure we can cancel it if requested.
     */
    private UserSignupTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mFnameView;
    private EditText mLnameView;
    private EditText mPostalCode;
    private Spinner mGender;
    private Spinner mRace;
    private EditText mHomeAddress;
    private EditText mContact;
    private EditText mDateView;
    private EditText mMonthView;
    private EditText mYearView;

    private View mProgressView;
    private View mSignupFormView;

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Set up the Signup form.
        email = getIntent().getStringExtra("email");
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        if( email != null || !email.isEmpty() ) mEmailView.setText(email);

        password = getIntent().getStringExtra("password");
        mPasswordView = (EditText) findViewById(R.id.password);
        if( password != null || !password.isEmpty() ) mPasswordView.setText(password);

        mFnameView = (EditText) findViewById(R.id.fname);
        mLnameView = (EditText) findViewById(R.id.lname);
        mPostalCode = (EditText) findViewById(R.id.postalcode);
        mHomeAddress = (EditText) findViewById(R.id.address);
        mContact = (EditText) findViewById(R.id.contact);
        mDateView = (EditText) findViewById(R.id.day);
        mMonthView = (EditText) findViewById(R.id.month);
        mYearView = (EditText) findViewById(R.id.year);

        mGender = (Spinner) findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adapterGender =
                ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGender.setAdapter(adapterGender);

        mRace = (Spinner) findViewById(R.id.race);
        ArrayAdapter<CharSequence> adapterRace =
                ArrayAdapter.createFromResource(this, R.array.race_array, android.R.layout.simple_spinner_item);
        adapterRace.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRace.setAdapter(adapterRace);

        Button mEmailSignUpButton = (Button) findViewById(R.id.email_sign_up_button);
        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
            }
        });

        mSignupFormView = findViewById(R.id.signup_form);
        mProgressView = findViewById(R.id.signup_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, Constants.REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, Constants.REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the Signup form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual Signup attempt is made.
     */
    private void attemptSignup() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mFnameView.setError(null);
        mLnameView.setError(null);
        mPostalCode.setError(null);
        mHomeAddress.setError(null);
        mContact.setError(null);
        mDateView.setError(null);
        mMonthView.setError(null);
        mYearView.setError(null);

        // Store values at the time of the Signup attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String fname = mFnameView.getText().toString();
        String lname = mLnameView.getText().toString();
        String postalCode = mPostalCode.getText().toString();
        String homeAddress = mHomeAddress.getText().toString();
        String contact = mContact.getText().toString();
        String gender = mGender.getSelectedItem().toString();
        String race = mRace.getSelectedItem().toString();
        String day = mDateView.getText().toString();
        String month = mMonthView.getText().toString();
        String year = mYearView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        else if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        else if(TextUtils.isEmpty(fname)) {
            mFnameView.setError(getString(R.string.error_field_required));
            focusView = mFnameView;
            cancel = true;
        }
        else if(TextUtils.isEmpty(lname)) {
            mLnameView.setError(getString(R.string.error_field_required));
            focusView = mLnameView;
            cancel = true;
        }
        else if(TextUtils.isEmpty(postalCode)) {
            mPostalCode.setError(getString(R.string.error_field_required));
            focusView = mPostalCode;
            cancel = true;
        }
        else if(TextUtils.isEmpty(homeAddress)) {
            mHomeAddress.setError(getString(R.string.error_field_required));
            focusView = mHomeAddress;
            cancel = true;
        }
        else if(TextUtils.isEmpty(contact)) {
            mContact.setError(getString(R.string.error_field_required));
            focusView = mContact;
            cancel = true;
        }
        else if (TextUtils.isEmpty(day)) {
            mDateView.setError("");
            focusView = mDateView;
            cancel = true;
        } else if (TextUtils.isEmpty(month)) {
            mMonthView.setError("");
            focusView = mMonthView;
            cancel = true;
        } else if (TextUtils.isEmpty(year)) {
            mYearView.setError("");
            focusView = mYearView;
            cancel = true;
        } else if (!isDateValid(day, month, year)) {
            Toast.makeText(this, "Invalid Date", Toast.LENGTH_SHORT).show();
            mDateView.setError("");
            focusView = mDateView;
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

            String dob = day + "" + month + "" + year;
            System.out.println("DOB :" + dob);
            //System.out.println("SignupActivty 1" + email + password+ fname +lname+ postalCode+ homeAddress + contact+ gender+ race+ dob);
            mAuthTask = new UserSignupTask(this, email, password, fname ,lname, postalCode, homeAddress , contact, gender, race, dob);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isDateValid(String day, String month, String year) {
        //System.out.println("DMY :" + day + "" + month + "" + year);
        final String DATE_FORMAT = "ddMMyyyy";
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(day+month+year);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the Signup form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignupFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

        addEmailsToAutoComplete(emails);
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


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(SignupActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous Signup/registration task used to authenticate
     * the user.
     */
    public class UserSignupTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private String email;
        private String password;
        private String fname;
        private String lname;
        private String postalCode;
        private String homeAddress;
        private String contact;
        private String gender;
        private String race;
        private String dob;

        UserSignupTask(Context context, String email, String password, String fname, String lname, String postalCode, String homeAddress, String contact, String gender, String race, String dob) {
            this.context = context;
            this.email = email;
            this.password = password;
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
                HttpPost httpPost = new HttpPost(Constants.urlString + "CreateCustomer");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("password", password));
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

                //System.out.println("SignupActivity3" + output);

                if (output != null && output.equalsIgnoreCase("OK")) {
                    return true;
                }
                else
                    return false;

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

        public void goToLoginActivity() {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra("New Customer", true);
            startActivity(intent);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
                goToLoginActivity();
            } else {
                Toast.makeText(context, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

