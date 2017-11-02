package by.overpass.hunger;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MSI GE62 2QE Apache on 27.10.2017.
 */

public class DishInCartAdapter extends BaseAdapter implements View.OnClickListener {

    private final Context mContext;
    private final List<Dish> dishes;
    private final List<Dish> alreadyDisplayedDishes = new ArrayList<>();
    private final List<Integer> alreadyDisplayedDishesIDs = new ArrayList<>();

    public DishInCartAdapter(Context mContext, List<Dish> dishes) {
        this.mContext = mContext;
        this.dishes = dishes;

        for (int i = 0; i < dishes.size(); i++) {
            if (!alreadyDisplayedDishesIDs.contains(dishes.get(i).getId())) {
                alreadyDisplayedDishesIDs.add(dishes.get(i).getId());
                alreadyDisplayedDishes.add(dishes.get(i));
            }
        }

        ///**debug**///Log.d("DISHINCARTADAPTER", "dishes = " + dishes.toString());
        ///**debug**///Log.d("DISHINCARTADAPTER", "unique = " + alreadyDisplayedDishes.toString());
    }

    @Override
    public int getCount() {
        return /*dishes.size()*/alreadyDisplayedDishes.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return /*dishes.get(position).getId()*/alreadyDisplayedDishes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Dish dish = alreadyDisplayedDishes.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.dish_in_cart_layout, null);
        }

        final TextView dishNameText = convertView.findViewById(R.id.dishNameText);
        final TextView dishPriceText = convertView.findViewById(R.id.dishPriceText);
        final TextView dishQuantityText = convertView.findViewById(R.id.counterTextView);
        final ImageView minusImageView = convertView.findViewById(R.id.minusOneDishImageView);
        final ImageView plusImageView = convertView.findViewById(R.id.plusOneDishImageView);
        final ImageView deleteImageView = convertView.findViewById(R.id.deleteFromCartImageView);

        dishNameText.setText(dish.getName());
        dishPriceText.setText(String.valueOf(dish.getPrice()) + " BYN");
        dishQuantityText.setText(String.valueOf(CartControl.dishesWithQuantity.get(dish.getId())));

        if (convertView != null) {
            minusImageView.setOnClickListener(this);
            plusImageView.setOnClickListener(this);
            deleteImageView.setOnClickListener(this);
        }

        return convertView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.deleteFromCartImageView:
                Toast.makeText(mContext, "DELETE CLICKED", Toast.LENGTH_SHORT);
                Log.d("DISHINCARTADAPTER", "DELETE CLICKED");
                break;
            case R.id.plusOneDishImageView:
                Toast.makeText(mContext, "PLUS CLICKED", Toast.LENGTH_SHORT);
                Log.d("DISHINCARTADAPTER", "PLUS CLICKED");
                break;
            case R.id.minusOneDishImageView:
                Toast.makeText(mContext, "MINUS CLICKED", Toast.LENGTH_SHORT);
                Log.d("DISHINCARTADAPTER", "MINUS CLICKED");
                break;
        }
    }
}
