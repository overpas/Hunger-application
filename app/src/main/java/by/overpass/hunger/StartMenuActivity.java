package by.overpass.hunger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class StartMenuActivity extends AppCompatActivity {
    
    GridView gridView;
    ImageView actionBarCartImage;

    private DishCategory[] categories = {
            new DishCategory(R.string.complex_dinner_caption, R.drawable.generic_complex_dinner),
            new DishCategory(R.string.soup_caption, R.drawable.generic_soup),
            new DishCategory(R.string.meat_caption, R.drawable.generic_meat),
            new DishCategory(R.string.garnish_caption, R.drawable.generic_garnish),
            new DishCategory(R.string.salad_caption, R.drawable.generic_salad),
            new DishCategory(R.string.pizza_caption, R.drawable.generic_pizza),
            new DishCategory(R.string.burgers_caption, R.drawable.generic_burger),
            new DishCategory(R.string.sushi_caption, R.drawable.generic_sushi),
            new DishCategory(R.string.dessert_caption, R.drawable.generic_dessert),
            new DishCategory(R.string.beverages_caption, R.drawable.generic_beverages)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        
        gridView = (GridView) findViewById(R.id.categoriesGridView);
        DishCategoryAdapter dishCategoryAdapter = new DishCategoryAdapter(this, categories);
        gridView.setAdapter(dishCategoryAdapter);

        /*gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(gridView.getContext(), DishesMenuActivity.class);
            intent.putExtra("categoryID", i);
            startActivity(intent);
        });*/

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(gridView.getContext(), DishesMenuActivity.class);
                intent.putExtra("categoryID", i);
                startActivity(intent);
            }
        });

        actionBarCartImage = (ImageView) findViewById(R.id.actionBarCartImage);
        actionBarCartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });
    }
}
