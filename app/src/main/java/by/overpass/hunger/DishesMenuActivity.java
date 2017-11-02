package by.overpass.hunger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

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
                if (CartController.currentOrderID == -1)
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
            categoryID = extras.getInt("categoryID") + 1;

        List<Dish> selectedDishesList = CartController.fetchDishes(this, categoryID);

        gridView = (GridView) findViewById(R.id.dishesGridView);
        final DishPreviewAdapter dishPreviewAdapter = new DishPreviewAdapter(this, selectedDishesList);
        gridView.setAdapter(dishPreviewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(gridView.getContext(), DishDescriptionActivity.class);
                intent.putExtra("chosenDishID", (int) dishPreviewAdapter.getItemId(position));

                //debug
                /*Log.d("DISHESMENUACTIVITY", "position = " + position + ", id[position] = " +
                        dishPreviewAdapter.getItemId(position));*/

                startActivity(intent);
            }
        });
    }
}