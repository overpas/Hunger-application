package by.overpass.hunger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by MSI GE62 2QE Apache on 27.10.2017.
 */

public class DishInCartAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<Integer> numbers;

    public DishInCartAdapter(Context mContext, List<Integer> dishes) {
        this.mContext = mContext;
        this.numbers = dishes;
    }

    @Override
    public int getCount() {
        return numbers.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final int number = numbers.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.dish_in_cart_layout, null);
        }

        return convertView;
    }
}
