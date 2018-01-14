package by.overpass.hunger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import by.overpass.hunger.datamanipulation.CartController;

public class CustomerDetailActivity extends AppCompatActivity {

    private final String customerNamePrefKey = "userPreferredName";
    private final String customerPhonePrefKey = "userPreferredPhone";
    private SharedPreferences sharedPrefs;
    private String destination = "";
    private String customerName;
    private String customerPhone;

    private TextView enterDataTV;
    private TextView enterAddressTV;
    private TextView enterNameTV;
    private TextView enterPhoneTV;
    private EditText addressEditText;
    private EditText nameEditText;
    private EditText phoneEditText;
    private Button btnCompleteOrder;
    private TextView totalPriceTextView;
    private ImageView goToMainMenuImageView;
    private ImageView goToCartImageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            destination = extras.getString("confirmedAddress");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.order_completion_action_bar);

        progressBar = (ProgressBar) findViewById(R.id.finalInfoActivityProgressBar);
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        btnCompleteOrder = (Button) findViewById(R.id.btnCompleteOrder);
        totalPriceTextView = (TextView) findViewById(R.id.actionBarTotalCost);
        goToMainMenuImageView = (ImageView) findViewById(R.id.actionBarGoToStartMenu);
        goToCartImageView = (ImageView) findViewById(R.id.actionBarGoToCart);
        enterDataTV = (TextView) findViewById(R.id.enterDataTextView);
        enterAddressTV = (TextView) findViewById(R.id.enterAddressTextView);
        enterNameTV = (TextView) findViewById(R.id.enterNameTextView);
        enterPhoneTV = (TextView) findViewById(R.id.enterPhoneTextView);

        btnCompleteOrder.startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_above));
        enterDataTV.startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_below));
        enterAddressTV.startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_below));
        enterNameTV.startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_below));
        enterPhoneTV.startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_below));
        nameEditText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_below));
        addressEditText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_below));
        phoneEditText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_below));

        goToMainMenuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(goToMainMenuImageView.getContext(), StartMenuActivity.class);
                startActivity(intent);
            }
        });

        goToCartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(goToCartImageView.getContext(), CartActivity.class);
                startActivity(intent);
            }
        });

        sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        customerName = sharedPrefs.getString(customerNamePrefKey, "");
        customerPhone = sharedPrefs.getString(customerPhonePrefKey, "");

        totalPriceTextView.setText(getResources().getString(R.string.total) + " " +
                String.format("%.1f", CartController.totalCost) + " " +
                getResources().getString(R.string.currency));
        addressEditText.setText(destination);
        nameEditText.setText(customerName);
        phoneEditText.setText(customerPhone);
    }

    public void confirmOrder(View view) {
        progressBar.setVisibility(View.VISIBLE);
        customerName = nameEditText.getText().toString();
        customerPhone = phoneEditText.getText().toString();
        destination = addressEditText.getText().toString();

        if (customerName.trim().equals(""))
            Toast.makeText(this, getResources().getString(R.string.enterNameMessage),
                    Toast.LENGTH_SHORT);
        else if (customerPhone.trim().equals(""))
            Toast.makeText(this, getResources().getString(R.string.enterPhoneMessage),
                    Toast.LENGTH_SHORT);
        else if (destination.trim().equals(""))
            Toast.makeText(this, getResources().getString(R.string.enterAddressMessage),
                    Toast.LENGTH_SHORT);
        else {
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString(customerNamePrefKey, customerName);
            editor.putString(customerPhonePrefKey, customerPhone);
            editor.commit();

            if (CartController.currentCustomerID == -1)
                CartController.createNewCustomer(this, customerName, customerPhone);

            CartController.completeOrder(this, destination);
        }

        if (CartController.orderSuccessful) {
            Intent intent = new Intent(this, FinalInfoActivity.class);
            startActivity(intent);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, getResources().getString(R.string.cannot_process_order_excuse),
                    Toast.LENGTH_SHORT).show();
        }

    }
}