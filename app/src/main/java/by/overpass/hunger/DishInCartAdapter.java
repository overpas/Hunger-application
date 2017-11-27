package by.overpass.hunger;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MSI GE62 2QE Apache on 27.10.2017.
 */

public class DishInCartAdapter extends BaseAdapter {

    private final Context mContext;
    private CartActivity cartActivity;
    private final List<Dish> dishes;
    private final List<Dish> alreadyDisplayedDishes = new ArrayList<>();
    private final List<Integer> alreadyDisplayedDishesIDs = new ArrayList<>();

    public DishInCartAdapter(Context mContext, List<Dish> dishes, CartActivity cartActivity) {
        this.mContext = mContext;
        this.dishes = dishes;
        this.cartActivity = cartActivity;

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
        dishPriceText.setText(String.valueOf(dish.getPrice()) + " " +
                mContext.getResources().getString(R.string.currency));
        dishQuantityText.setText(String.valueOf(CartController.dishesWithQuantity.get(dish.getId())));

        View.OnClickListener plusMinusOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.deleteFromCartImageView:
                        ///**debug**///Log.d("DISHINCARTADAPTER", "DELETE CLICKED");
                        cartActivity.showProgressBar();

                        CartController.deleteDishFromCart(mContext, dish.getId());
                        CartController.updateCartList(mContext);
                        CartController.getTotalPrice(mContext, CartController.currentOrderID);

                        Intent intent1 = new Intent(mContext, CartActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        mContext.startActivity(intent1);

                        break;
                    case R.id.plusOneDishImageView:
                        ///**debug**///Log.d("DISHINCARTADAPTER", "PLUS CLICKED");

                        CartController.addAnItem(mContext, dish.getId(), cartActivity);
                        CartController.updateCartList(mContext);
                        CartController.getTotalPrice(mContext, CartController.currentOrderID);

                        Intent intent2 = new Intent(mContext, CartActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        mContext.startActivity(intent2);

                        break;
                    case R.id.minusOneDishImageView:
                        ///**debug**///Log.d("DISHINCARTADAPTER", "MINUS CLICKED");
                        cartActivity.showProgressBar();

                        CartController.deleteAnItem(mContext, dish.getId());
                        CartController.updateCartList(mContext);
                        CartController.getTotalPrice(mContext, CartController.currentOrderID);

                        Intent intent3 = new Intent(mContext, CartActivity.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        mContext.startActivity(intent3);

                        break;
                }
            }
        };

        if (convertView != null) {
            minusImageView.setOnClickListener(plusMinusOnClickListener);
            plusImageView.setOnClickListener(plusMinusOnClickListener);
            deleteImageView.setOnClickListener(plusMinusOnClickListener);
        }

        return convertView;
    }
}