package by.overpass.hunger;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.google.android.gms.wallet.Cart;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGetHC4;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class LoadingDishesMenuActivity extends AppCompatActivity {

    private int categoryID = -1;
    private AsyncTask<String, Void, String> fetchDishes;

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
            categoryID = extras.getInt("categoryID") + 1;

        //Log.d("LOADING", "categoryID = " + String.valueOf(categoryID));

        fetchDishes = new DishesFetcher().execute(getResources().getString(R.string.fetch_dishes_http_link) +
                categoryID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fetchDishes.cancel(true);
    }

    private void goBack() {
        Intent intent = new Intent(this, StartMenuActivity.class);
        intent.putExtra("fetchingError", getString(R.string.couldnt_load_data_message));
        startActivity(intent);
    }

    public class DishesFetcher extends AsyncTask<String, Void, String> {

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
                e.printStackTrace();
                goBack();
            }

            result = sb != null ? sb.toString() : null;
            //Log.d("LOADING", result);
            return sb != null ? sb.toString() : null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            try {
                TransitionHelper.selectedDishesList = CartController.fetchDishesParser(result);
            } catch (JSONException e) {
                //e.printStackTrace();
                goBack();
            } catch (NullPointerException e) {
                goBack();
            }
            Intent intent = new Intent(getApplicationContext(), DishesMenuActivity.class);
            startActivity(intent);
        }
    }
}
