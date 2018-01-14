package by.overpass.hunger;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGetHC4;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import by.overpass.hunger.datamanipulation.CartController;
import by.overpass.hunger.datamanipulation.TransitionHelper;

public class LoadingDishDescriptionActivity extends AppCompatActivity {

    private int chosenDishID = -1;
    private AsyncTask<String, Void, String> fetchDishInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ImageView hungerProgressBar = (ImageView) findViewById(R.id.hunger_progress_bar);
        int currentRotation = 0;
        RotateAnimation anim = new RotateAnimation(currentRotation, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(600);
        anim.setRepeatMode(Animation.RESTART);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setFillEnabled(true);
        anim.setFillAfter(true);
        hungerProgressBar.startAnimation(anim);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            chosenDishID = extras.getInt("chosenDishID");

        fetchDishInfo = new DishInfoFetcher().execute(getResources()
                .getString(R.string.fetch_dish_info_http_link) + chosenDishID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fetchDishInfo.cancel(true);
    }

    private void goBack() {
        Intent intent = new Intent(this, DishesMenuActivity.class);
        intent.putExtra("fetchingError", getString(R.string.couldnt_load_data_message));
        startActivity(intent);
    }

    public class DishInfoFetcher extends AsyncTask<String, Void, String> {

        private String result;

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
                goBack();
            }

            result  = sb != null ? sb.toString() : null;
            return sb != null ? sb.toString() : null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            try {
                TransitionHelper.chosenDish = CartController.fetchDishInfoParser(result);
            } catch (JSONException e) {
                goBack();
            }
            Intent intent = new Intent(getApplicationContext(), DishDescriptionActivity.class);
            intent.putExtra("chosenDishID", chosenDishID);
            startActivity(intent);
        }
    }
}
