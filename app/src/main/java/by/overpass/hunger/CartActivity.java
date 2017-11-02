package by.overpass.hunger;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class CartActivity extends AppCompatActivity /*implements AdapterView.OnItemClickListener*/ {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        listView = (ListView) findViewById(R.id.cartContents);

        //debug
        /*if (CartControl.cartList == null)
            Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, CartControl.cartList.toString(), Toast.LENGTH_SHORT).show();*/

        DishInCartAdapter dishInCartAdapter = new DishInCartAdapter(this, CartControl.cartList);
        listView.setAdapter(dishInCartAdapter);

        //listView.setOnItemClickListener(this);
    }

    /*@Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

    }*/
}
