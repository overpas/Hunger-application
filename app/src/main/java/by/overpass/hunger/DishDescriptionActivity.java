package by.overpass.hunger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

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
                if (CartControl.currentOrderID == -1)
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

        String link = getResources().getString(R.string.fetch_dish_info_http_link) + chosenDishID;
        AsyncTask<String, Void, String> dishInfoFetcher = new DishesFetcher().execute(link);
        String stringJSON;
        JSONArray arrayOfDishes;
        JSONObject jsonObject;
        Dish chosenDish;

        try {
            stringJSON = dishInfoFetcher.get();
            arrayOfDishes = new JSONArray(stringJSON);
            jsonObject = arrayOfDishes.getJSONObject(0);

            chosenDish = new Dish(jsonObject.getInt("id"), jsonObject.getInt("category_id"),
                    jsonObject.getString("name"), jsonObject.getString("url").trim(),
                    jsonObject.getDouble("price"));
            chosenDish.setCalorificValue(jsonObject.getDouble("calorific_value"));
            chosenDish.setDescription(jsonObject.getString("description").trim());
            chosenDish.setWeight(jsonObject.getDouble("weight"));

            dishNameTextView.setText(chosenDish.getName());
            dishWeightTextView.setText(chosenDish.getWeight() + " кг");
            dishCalorificValueTextView.setText(chosenDish.getCalorificValue() + " ккал");
            dishPriceTextView.setText(chosenDish.getPrice() + " BYN");
            dishDescriptionTextView.setText(chosenDish.getDescription());
            Picasso.with(this).load(chosenDish.getUrl())
                    .placeholder(R.drawable.dish_placeholder).into(dishImageView);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void toCart(View v) {
        if (CartControl.currentOrderID == -1)
            createNewOrder();

        addToCart();

        CartControl.updateCartList(this);

        if (CartControl.currentOrderID != -1)
            Toast.makeText(this, getResources().getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show();
    }

    public void createNewOrder() {
        String newOrderLink = getResources().getString(R.string.create_new_order_http_link);
        AsyncTask<String, Void, String> creator = new OrderOrCartCreator().execute(newOrderLink);
        try {
            int newlyInsertedID = Integer.parseInt(creator.get());
            CartControl.currentOrderID = newlyInsertedID;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void addToCart() {
        String cartLink = getResources().getString(R.string.add_to_cart_http_link);
        String orderID = String.valueOf(CartControl.currentOrderID);
        String dishID = String.valueOf(chosenDishID);

        AsyncTask<String, Void, String> adder = new OrderOrCartCreator().execute(cartLink, dishID,
                orderID);
    }

    private class OrderOrCartCreator extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String link = urls[0];
            StringBuilder sb = null;

            try {
                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                if (urls.length == 3) {
                    String dishID = urls[1], orderID = urls[2];

                    String data = URLEncoder.encode("dishID", "UTF-8") + "=" +
                            URLEncoder.encode(dishID, "UTF-8");
                    data += "&" + URLEncoder.encode("orderID", "UTF-8") + "=" +
                            URLEncoder.encode(orderID, "UTF-8");

                    wr.write(data);
                    wr.flush();
                }

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                sb = new StringBuilder();
                String line;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sb == null ? null : sb.toString();
        }
    }
}
