package by.overpass.hunger;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by MSI GE62 2QE Apache on 22.10.2017.
 */

public class DishCategoryAdapter extends BaseAdapter {
    private final Context mContext;
    private final DishCategory[] categories;

    public DishCategoryAdapter(Context mContext, DishCategory[] categories) {
        this.mContext = mContext;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.length;
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
        final DishCategory category = categories[position];

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.dish_category_layout, null);
        }

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageview_cover);
        final TextView textView = (TextView) convertView.findViewById(R.id.category_caption);

        imageView.setImageResource(category.getImageResourceID());
        ViewGroup.LayoutParams params = convertView.getLayoutParams();
        if (params != null)
            Log.d("DISHCATEGORYADAPTER", String.valueOf(params.width));
        textView.setText(mContext.getString(category.getName()));

        return convertView;
    }
}
