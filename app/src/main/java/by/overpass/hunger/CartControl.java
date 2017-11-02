package by.overpass.hunger;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by MSI GE62 2QE Apache on 29.10.2017.
 */

public class CartControl {
    public static int currentOrderID = -1;
    public static List<Dish> cartList = null;
    public static Map<Integer, Integer> dishesWithQuantity = null;

    public static void updateCartList(Context context) {
        assert currentOrderID != -1;

        String link = context.getResources().getString(R.string.fetch_order_dishes_http_link)
                + currentOrderID;
        AsyncTask<String, Void, String> orderDishesFetcher = new DishesFetcher().execute(link);

        String stringJSON;
        JSONArray arrayOfDishes;
        JSONObject jsonObject;
        List<Dish> orderDishesList = new ArrayList<>();
        List<Integer> dishesIDList = new ArrayList<>();

        try {
            stringJSON = orderDishesFetcher.get();
            ///**debug**///Log.d("CartControl", stringJSON);
            arrayOfDishes = new JSONArray(stringJSON);
            dishesWithQuantity = new HashMap<>();

            for (int i = 0; i < arrayOfDishes.length(); i++) {
                jsonObject = arrayOfDishes.getJSONObject(i);
                Dish dish = new Dish(jsonObject.getInt("id"), jsonObject.getInt("category_id"),
                        jsonObject.getString("name"), jsonObject.getString("url").trim(),
                        jsonObject.getDouble("price"));
                dish.setDescription(jsonObject.getString("description").trim());
                dish.setCalorificValue(jsonObject.getDouble("calorific_value"));
                dish.setWeight(jsonObject.getDouble("weight"));
                orderDishesList.add(dish);
                dishesIDList.add(dish.getId());
                dishesWithQuantity.put(dish.getId(), 1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cartList = new ArrayList<>(orderDishesList);

        ///**debug**///Log.d("CartControl", cartList.toString());

        for (Map.Entry<Integer, Integer> pair : dishesWithQuantity.entrySet()) {
            int count = 0;
            while (dishesIDList.contains(pair.getKey())) {
                count++;
                dishesIDList.remove(pair.getKey());
            }
            dishesWithQuantity.put(pair.getKey(), count);
        }

        ///**debug**///Log.d("CartControl", dishesWithQuantity.toString());
    }
}
