package by.overpass.hunger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DishDescriptionActivity extends AppCompatActivity {

    TextView dishDescriptionTextView;
    TextView dishNameTextView;
    TextView dishWeightTextView;
    TextView dishPriceTextView;
    TextView dishCalorificValueTextView;
    ImageView dishImageView;
    int chosenDishID;
    ImageView actionBarCartImage;
    Button addToCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_description);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        dishDescriptionTextView = (TextView) findViewById(R.id.dishDescriptionTextView);
        dishCalorificValueTextView = (TextView) findViewById(R.id.dishCalorificValueTextView);
        dishPriceTextView = (TextView) findViewById(R.id.dishPriceTextView);
        dishWeightTextView = (TextView) findViewById(R.id.dishWeightTextView);
        dishNameTextView = (TextView) findViewById(R.id.dishNameTextView);
        dishImageView = (ImageView) findViewById(R.id.dishImageView);

        actionBarCartImage = (ImageView) findViewById(R.id.actionBarCartImage);
        actionBarCartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CartController.currentOrderID == -1)
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.cart_is_empty),
                            Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                    startActivity(intent);
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            chosenDishID = extras.getInt("chosenDishID");

        Dish chosenDish = CartController.fetchDishInfo(this, chosenDishID);

        if (chosenDish != null) {
            dishNameTextView.setText(chosenDish.getName());
            dishWeightTextView.setText(chosenDish.getWeight() + " кг");
            dishCalorificValueTextView.setText(chosenDish.getCalorificValue() + " ккал");
            dishPriceTextView.setText(chosenDish.getPrice() + getResources().getString(R.string.currency));
            dishDescriptionTextView.setText(chosenDish.getDescription());
            Picasso.with(this).load(chosenDish.getUrl())
                    .placeholder(R.drawable.dish_placeholder).into(dishImageView);
        }
    }

    public void toCart(View v) {
        if (CartController.currentOrderID == -1)
            CartController.createNewOrder(this);

        CartController.addToCart(this, chosenDishID);

        CartController.updateCartList(this);

        if (CartController.currentOrderID != -1)
            Toast.makeText(this, getResources().getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show();
    }
}