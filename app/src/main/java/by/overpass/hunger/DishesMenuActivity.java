package by.overpass.hunger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

public class DishesMenuActivity extends AppCompatActivity {

    private GridView gridView;
    private ImageView actionBarCartImage;

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

        //int categoryID = -1;
        if (extras != null) {
            boolean shouldShowMessages = true;
            if (savedInstanceState != null)
                shouldShowMessages = savedInstanceState.getBoolean("shouldShowMessages");
            //categoryID = extras.getInt("categoryID") + 1;
            String fetchingError = extras.getString("fetchingError");

            if (fetchingError != null && shouldShowMessages)
                Toast.makeText(this, fetchingError, Toast.LENGTH_SHORT).show();
        }

        gridView = (GridView) findViewById(R.id.dishesGridView);

        //selectedDishesList = CartController.fetchDishes(this, categoryID);

        //Log.d("NESTED ASYNCTASK", selectedDishesList.toString());

        DishPreviewAdapter dishPreviewAdapter = null;
        if (TransitionHelper.selectedDishesList != null) {
            dishPreviewAdapter = new DishPreviewAdapter(this,
                    TransitionHelper.selectedDishesList);
        } else
            retreat();

        gridView.setAdapter(dishPreviewAdapter);

        final DishPreviewAdapter finalDishPreviewAdapter = dishPreviewAdapter;
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(gridView.getContext(), LoadingDishDescriptionActivity.class);
                intent.putExtra("chosenDishID", (int) finalDishPreviewAdapter.getItemId(position));

                //debug
                /*Log.d("DISHESMENUACTIVITY", "position = " + position + ", id[position] = " +
                        dishPreviewAdapter.getItemId(position));*/

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("shouldShowMessages", false);
        super.onSaveInstanceState(outState);
    }

    private void retreat() {
        Intent intent = new Intent(this, StartMenuActivity.class);
        intent.putExtra("fetchingError", getResources()
                .getString(R.string.couldnt_load_data_message));
        startActivity(intent);
    }
}