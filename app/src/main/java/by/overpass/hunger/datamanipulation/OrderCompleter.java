package by.overpass.hunger.datamanipulation;

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
 * Created by MSI GE62 2QE Apache on 15.11.2017.
 */

public class OrderCompleter extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {
        String link = urls[0];
        StringBuilder sb = null;

        try {
            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            String orderID = urls[1], customerID = urls[2], destination = urls[3];

            String data = URLEncoder.encode("orderID", "UTF-8") + "=" +
                    URLEncoder.encode(orderID, "UTF-8");
            data += "&" + URLEncoder.encode("customerID", "UTF-8") + "=" +
                    URLEncoder.encode(customerID, "UTF-8");
            data += "&" + URLEncoder.encode("destination", "UTF-8") + "=" +
                    URLEncoder.encode(destination, "UTF-8");

            wr.write(data);
            wr.flush();

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
