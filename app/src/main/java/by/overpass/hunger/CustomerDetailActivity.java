package by.overpass.hunger;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CustomerDetailActivity extends AppCompatActivity {

    String destination;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            destination = extras.getString("confirmedAddress");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.order_completion_action_bar);
    }
}
