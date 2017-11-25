package by.overpass.hunger;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by MSI GE62 2QE Apache on 14.11.2017.
 */

public class CustomerCreator extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {
        String link = urls[0];
        StringBuilder sb = null;

        try {
            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            String customerName = urls[1], customerPhone = urls[2];

            String data = URLEncoder.encode("customerName", "UTF-8") + "=" +
                    URLEncoder.encode(customerName, "UTF-8");
            data += "&" + URLEncoder.encode("customerPhone", "UTF-8") + "=" +
                    URLEncoder.encode(customerPhone, "UTF-8");

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
