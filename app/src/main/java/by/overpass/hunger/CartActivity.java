package by.overpass.hunger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CartActivity extends AppCompatActivity {

    ListView listView;
    ImageView actionBarGoToMainMenu;
    TextView actionBarTotalPrice;
    Button btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if (CartController.currentOrderID == -1) {
            Intent intent = new Intent(this, StartMenuActivity.class);
            intent.putExtra("cartIsEmpty", true);
            startActivity(intent);
        }

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.cart_action_bar);

        listView = (ListView) findViewById(R.id.cartContents);
        actionBarGoToMainMenu = (ImageView) findViewById(R.id.actionBarGoToMainMenu);
        actionBarTotalPrice = (TextView) findViewById(R.id.actionBarTotalPrice);
        btnOrder = (Button) findViewById(R.id.btnOrder);

        //debug
        /*if (CartControl.cartList == null)
            Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, CartControl.cartList.toString(), Toast.LENGTH_SHORT).show();*/

        DishInCartAdapter dishInCartAdapter = new DishInCartAdapter(this, CartController.cartList);
        listView.setAdapter(dishInCartAdapter);

        actionBarTotalPrice.setText(getResources().getString(R.string.total) + String.format("%.1f",
                CartController.getTotalPrice(this, CartController.currentOrderID)) +
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
}
