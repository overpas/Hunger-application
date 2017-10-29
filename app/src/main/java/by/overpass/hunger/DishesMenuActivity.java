package by.overpass.hunger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGetHC4;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DishesMenuActivity extends AppCompatActivity {

    GridView gridView;
    int categoryID = -1;
    ImageView actionBarCartImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_menu);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        actionBarCartImage = (ImageView) findViewById(R.id.actionBarCartImage);
        actionBarCartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            categoryID = extras.getInt("categoryID") + 1;

        String link = getResources().getString(R.string.fetch_dishes_http_link) + categoryID;
        AsyncTask<String, Void, String> dishesFetcher = new DishesFetcher().execute(link);
        String stringJSON;
        JSONArray arrayOfDishes;
        JSONObject jsonObject;
        List<Dish> selectedDishesList = new ArrayList<>();

        try {

            stringJSON = dishesFetcher.get();
            arrayOfDishes = new JSONArray(stringJSON);

            for (int i = 0; i < arrayOfDishes.length(); i++) {
                jsonObject = arrayOfDishes.getJSONObject(i);
                selectedDishesList.add(new Dish(jsonObject.getInt("id"), categoryID,
                        jsonObject.getString("name"), jsonObject.getString("url").trim(),
                        jsonObject.getDouble("price")));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        gridView = (GridView) findViewById(R.id.dishesGridView);
        final DishPreviewAdapter dishPreviewAdapter = new DishPreviewAdapter(this, selectedDishesList);
        gridView.setAdapter(dishPreviewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(gridView.getContext(), DishDescriptionActivity.class);
                intent.putExtra("chosenDishID", (int) dishPreviewAdapter.getItemId(position));

                /*Log.d("DISHESMENUACTIVITY", "position = " + position + ", id[position] = " +
                        dishPreviewAdapter.getItemId(position));*/

                startActivity(intent);
            }
        });

    }

    private class DishesFetcher extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String link = urls[0];
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGetHC4 request = new HttpGetHC4(link);
            CloseableHttpResponse response = null;
            StringBuffer sb = null;

            try {

                response = client.execute(request);
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                sb = new StringBuffer("");
                String line;

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                in.close();
                client.close();
                response.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return sb != null ? sb.toString() : null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
