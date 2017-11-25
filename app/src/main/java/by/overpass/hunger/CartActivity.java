package by.overpass.hunger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CartActivity extends AppCompatActivity {

    private ListView listView;
    private ImageView actionBarGoToMainMenu;
    private TextView actionBarTotalPrice;
    private Button btnOrder;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        boolean smthWentWrong;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            smthWentWrong = extras.getBoolean("didSmthGoWrong");
            if (smthWentWrong)
                Toast.makeText(this, "Sorry, something went wrong.", Toast.LENGTH_SHORT).show();
        }

        if (CartController.currentOrderID == -1) {
            Intent intent = new Intent(this, StartMenuActivity.class);
            intent.putExtra("cartIsEmpty", true);
            startActivity(intent);
        }

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.cart_action_bar);

        progressBar = (ProgressBar) findViewById(R.id.cartActivityProgressBar);
        listView = (ListView) findViewById(R.id.cartContents);
        actionBarGoToMainMenu = (ImageView) findViewById(R.id.actionBarGoToMainMenu);
        actionBarTotalPrice = (TextView) findViewById(R.id.actionBarTotalPrice);
        btnOrder = (Button) findViewById(R.id.btnOrder);

        btnOrder.startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_above));

        //debug
        /*if (CartController.cartList == null)
            Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, CartController.cartList.toString(), Toast.LENGTH_SHORT).show();*/

        DishInCartAdapter dishInCartAdapter = new DishInCartAdapter(this,
                CartController.cartList, this);
        listView.setAdapter(dishInCartAdapter);

        actionBarTotalPrice.setText(getResources().getString(R.string.total) + " " +
                String.format("%.1f", CartController.totalCost) + " " +
                getResources().getString(R.string.currency));

        actionBarGoToMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(actionBarGoToMainMenu.getContext(), StartMenuActivity.class);
                startActivity(intent);
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(btnOrder.getContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void showProgressBar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }
}
