package by.overpass.hunger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by MSI GE62 2QE Apache on 24.10.2017.
 */

public class DishPreviewAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<Dish> dishes;

    public DishPreviewAdapter(Context mContext, List<Dish> dishes) {
        this.mContext = mContext;
        this.dishes = dishes;
    }

    @Override
    public int getCount() {
        return dishes.size();
    }

    @Override
    public Object getItem(int position) {
        return dishes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dishes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final Dish dish = dishes.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.dish_preview_layout, null);
        }

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.dish_imageview_cover);
        final TextView nameTextView = (TextView) convertView.findViewById(R.id.dish_name_caption);
        final TextView priceTextView = (TextView) convertView.findViewById(R.id.dish_price_caption);

        Picasso.with(mContext).load(dish.getUrl())
                .placeholder(R.drawable.dish_placeholder).into(imageView);
        nameTextView.setText(dish.getName());
        priceTextView.setText(String.valueOf(dish.getPrice()) + mContext.getResources().getString(R.string.currency));

        return convertView;
    }
}
