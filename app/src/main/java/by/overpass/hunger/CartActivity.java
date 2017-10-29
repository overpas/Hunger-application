package by.overpass.hunger;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.Arrays;

public class CartActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        listView = (ListView) findViewById(R.id.cartContents);
        DishInCartAdapter dishInCartAdapter = new DishInCartAdapter(this, Arrays.asList(1, 2, 3, 4));
        listView.setAdapter(dishInCartAdapter);
    }
}
