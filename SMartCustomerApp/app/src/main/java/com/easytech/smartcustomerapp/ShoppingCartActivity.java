package com.easytech.smartcustomerapp;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ShoppingCartActivity extends AppCompatActivity {

    private ListView listview;
    private ShoppingCart shoppingCart;
    private TextView shippingView;
    private TextView totalView;
    private Button checkoutButton;

    private TextView shippingFee;
    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedItem = parent.getItemAtPosition(position).toString();

            if (selectedItem.contains("Saturday") || selectedItem.contains("Sunday")) shoppingCart.setShippingFee(12.00);
            else if (selectedItem.contains("5pm - 9pm")) shoppingCart.setShippingFee(12.00);
            else shoppingCart.setShippingFee(7.00);

            shippingView.setText(String.format("$%.2f", shoppingCart.getShippingFee()));
            totalView.setText(String.format("$%.2f", shoppingCart.calculateTotal()));
            shippingFee.setText(String.format("$%.2f", shoppingCart.getShippingFee()));
        }

        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        shoppingCart = new ShoppingCart();
        listview = (ListView) findViewById(R.id.list_item_cart);
        totalView = (TextView) findViewById(R.id.total_amount);
        shippingView = (TextView) findViewById(R.id.shipping_amount);
        checkoutButton = (Button) findViewById(R.id.checkout);

        if (shoppingCart.getItems() == null || shoppingCart.getItems().isEmpty())
            Toast.makeText(this, "There are no Items in your cart.", Toast.LENGTH_LONG).show();

        else {
            shippingView.setText(String.format("$%.2f", shoppingCart.getShippingFee()));
            totalView.setText(String.format("$%.2f", shoppingCart.calculateTotal()));
            listview.setAdapter(new CartAdapter(shoppingCart.getItems(), getLayoutInflater()));
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent productDetailsIntent = new Intent(getBaseContext(), ItemDetailsActivity.class);
                    productDetailsIntent.putExtra(Constants.item, shoppingCart.getItems().get(position));
                    startActivity(productDetailsIntent);
                }
            });
            checkoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // get prompt.xml view
                    LayoutInflater li = LayoutInflater.from(ShoppingCartActivity.this);
                    View promptsView = li.inflate(R.layout.prompt, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShoppingCartActivity.this);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final Spinner userInput = (Spinner) promptsView.findViewById(R.id.delivery_spinner);
                    ArrayAdapter<CharSequence> adapter =
                            ArrayAdapter.createFromResource(ShoppingCartActivity.this, R.array.delivery_array, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    userInput.setAdapter(adapter);

                    shippingFee = (TextView) promptsView.findViewById(R.id.shipping_details);

                    userInput.setOnItemSelectedListener(onItemSelectedListener);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            //TODO complete payment
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
            });
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSearchItemActivity();
            }
        });
    }

    public void goToSearchItemActivity() {
        Intent intent = new Intent(this, SearchItemActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_cart) {
            goToShoppingCartActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        goToMainActivity();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void goToShoppingCartActivity() {
        Intent intent = new Intent(this, ShoppingCartActivity.class);
        startActivity(intent);
    }
}
