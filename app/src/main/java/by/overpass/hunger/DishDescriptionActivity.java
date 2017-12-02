package by.overpass.hunger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DishDescriptionActivity extends AppCompatActivity {

    private TextView dishDescriptionTextView;
    private TextView dishNameTextView;
    private TextView dishWeightTextView;
    private TextView dishPriceTextView;
    private TextView dishCalorificValueTextView;
    private ImageView dishImageView;
    private int chosenDishID;
    private ImageView actionBarCartImage;
    private Button addToCartButton;
    private ProgressBar progressBar;
    private LinearLayout infoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_description);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        progressBar = (ProgressBar) findViewById(R.id.dishDescriptionActivityProgressBar);
        progressBar.bringToFront();
        infoLayout = (LinearLayout) findViewById(R.id.infoLayout);
        addToCartButton = (Button) findViewById(R.id.btnAddToCart);
        dishDescriptionTextView = (TextView) findViewById(R.id.dishDescriptionTextView);
        dishCalorificValueTextView = (TextView) findViewById(R.id.dishCalorificValueTextView);
        dishPriceTextView = (TextView) findViewById(R.id.dishPriceTextView);
        dishWeightTextView = (TextView) findViewById(R.id.dishWeightTextView);
        dishNameTextView = (TextView) findViewById(R.id.dishNameTextView);
        dishImageView = (ImageView) findViewById(R.id.dishImageView);

        dishImageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_below));
        addToCartButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_above));
        if (infoLayout != null)
            infoLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.tick_animation));

        actionBarCartImage = (ImageView) findViewById(R.id.actionBarCartImage);
        actionBarCartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CartController.currentOrderID == -1)
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.cart_is_empty),
                            Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getApplicationContext(), LoadingCartActivity.class);
                    startActivity(intent);
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            chosenDishID = extras.getInt("chosenDishID");

        if (TransitionHelper.chosenDish != null) {
            dishNameTextView.setText(TransitionHelper.chosenDish.getName());
            dishWeightTextView.setText(TransitionHelper.chosenDish.getWeight() + " " +
                    getResources().getString(R.string.weight_unit));
            dishCalorificValueTextView.setText(TransitionHelper.chosenDish.getCalorificValue() + " " +
                    getResources().getString(R.string.calorific_value_unit));
            dishPriceTextView.setText(TransitionHelper.chosenDish.getPrice() + " " +
                    getResources().getString(R.string.currency));
            dishDescriptionTextView.setText(TransitionHelper.chosenDish.getDescription());
            Picasso.with(this).load(TransitionHelper.chosenDish.getUrl())
                    .placeholder(R.drawable.dish_placeholder).into(dishImageView);
        } else
            retreat();
    }

    private void retreat() {
        Intent intent = new Intent(this, DishesMenuActivity.class);
        intent.putExtra("fetchingError",
                getResources().getString(R.string.couldnt_load_data_message));
        startActivity(intent);
    }

    public void toCart(View v) {
        progressBar.setVisibility(View.VISIBLE);
        new ToCart().execute();
    }

    class ToCart extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (CartController.currentOrderID == -1)
                CartController.createNewOrder(getApplicationContext());

            CartController.addToCart(getApplicationContext(), chosenDishID);

            CartController.updateCartList(getApplicationContext());

            progressBar.setVisibility(View.INVISIBLE);
            if (CartController.currentOrderID != -1)
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), getResources()
                        .getString(R.string.couldnt_submit_data_message), Toast.LENGTH_SHORT).show();
        }
    }
}