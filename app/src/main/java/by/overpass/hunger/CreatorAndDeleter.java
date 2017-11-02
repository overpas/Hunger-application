package by.overpass.hunger;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by MSI GE62 2QE Apache on 02.11.2017.
 */

public class CreatorAndDeleter extends AsyncTask<String, Void, String> {

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
            } else if (urls.length == 2) {
                String orderID = urls[1];

                String data = URLEncoder.encode("orderID", "UTF-8") + "=" +
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
