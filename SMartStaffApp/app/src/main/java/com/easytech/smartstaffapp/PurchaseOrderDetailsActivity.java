package com.easytech.smartstaffapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easytech.smartstaffapp.pojo.Employee;
import com.easytech.smartstaffapp.pojo.PurchaseOrder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PurchaseOrderDetailsActivity extends AppCompatActivity {

    private PurchaseOrder purchaseOrder;
    private Employee employee;

    private PurchaseOrderDetailsTask mAuthTask = null;

    private TextView idView;
    private TextView titleView;
    private TextView approvalView;
    private TextView approverView;
    private TextView descView;
    private TextView amtView;
    private TextView nextView;
    private TextView deliveryView;
    private ListView itemsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        purchaseOrder = (PurchaseOrder) getIntent().getSerializableExtra(Constants.purchase);

        idView = (TextView) findViewById(R.id.po_id);
        titleView = (TextView) findViewById(R.id.po_title);
        approvalView = (TextView) findViewById(R.id.po_approvaldate);
        approverView = (TextView) findViewById(R.id.po_approver);
        descView = (TextView) findViewById(R.id.po_desc);
        amtView = (TextView) findViewById(R.id.po_amt);
        nextView = (TextView) findViewById(R.id.po_nextrundate);
        deliveryView = (TextView) findViewById(R.id.po_deliverydate);
        itemsView = (ListView) findViewById(R.id.po_items);

        idView.setText(purchaseOrder.getId().toString());
        titleView.setText(purchaseOrder.getTitle());
        Date date = new Date(purchaseOrder.getApprovalDate());
        SimpleDateFormat df2 = new SimpleDateFormat(Constants.dateFormat);
        approvalView.setText(df2.format(date));


        SharedPreferences sharedpreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        String customerJson = sharedpreferences.getString(Constants.Customer, null);
        if (customerJson == null) {
            goToLoginActivity();
        } else {
            Gson gson = new Gson();
            employee = gson.fromJson(customerJson, Employee.class);

            //System.out.println("Employee : " + employee.toString());

            mAuthTask = new PurchaseOrderDetailsTask(this, employee.getEmpId(), purchaseOrder.getId());
            mAuthTask.execute((Void) null);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO update PO

                //TODO create goods received
            }
        });
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public class PurchaseOrderDetailsTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private Long poId;
        private Long empId;
        private String output;
        private PurchaseOrder po;

        PurchaseOrderDetailsTask(Context context, Long empId, Long poId) {
            this.empId = empId;
            this.poId = poId;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(Constants.urlString + "MobilePuchaseOrder?empId=" + empId + "&poId=" + poId);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);

                //System.out.println(output);

                if (output == null) return false;
                else {
                    Gson gson = new Gson();
                    po = gson.fromJson(output, new TypeToken<List<PurchaseOrder>>() {
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
            if (success) {
                approverView.setText(po.getApprover());
                descView.setText(po.getDesc());
                amtView.setText(String.format("$%.2f", po.getAmount()));
                Date date = new Date(po.getNextRunDate());
                SimpleDateFormat df2 = new SimpleDateFormat(Constants.dateFormat);
                nextView.setText(df2.format(date));
                Date date2 = new Date(po.getDeliveryDate());
                deliveryView.setText(df2.format(date2));

                itemsView.setAdapter(new poItemsAdapter(po.getItems(), getLayoutInflater()));

            } else {
                Toast.makeText(context, "Purchase Order Details unavailable.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}

