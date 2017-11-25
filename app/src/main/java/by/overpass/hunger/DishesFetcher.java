package by.overpass.hunger;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGetHC4;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by MSI GE62 2QE Apache on 30.10.2017.
 */

public class DishesFetcher extends AsyncTask<String, Void, String> {

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
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String aVoid) {

    }
}
