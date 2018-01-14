package by.overpass.hunger.datamanipulation;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import by.overpass.hunger.CartActivity;
import by.overpass.hunger.R;
import by.overpass.hunger.datamodel.Dish;

/**
 * Created by MSI GE62 2QE Apache on 29.10.2017.
 */

public class CartController {
    public static int currentOrderID = -1;
    public static int currentCustomerID = -1;
    public static List<Dish> cartList = new ArrayList<>();
    public static Map<Integer, Integer> dishesWithQuantity = null;
    public static double totalCost = 0;
    public static boolean orderSuccessful = false;
    public static String answer;

    public static void updateCartList(Context context) {
        assert currentOrderID != -1;

        Log.d("CartControl", String.valueOf(currentOrderID));

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
            ///**debug**///
            Log.d("CartControl", stringJSON);

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

        ///**debug**///
        Log.d("CartControl", cartList.toString());
        Log.d("CartControl", dishesWithQuantity.toString());

        for (Map.Entry<Integer, Integer> pair : dishesWithQuantity.entrySet()) {
            int count = 0;
            while (dishesIDList.contains(pair.getKey())) {
                count++;
                dishesIDList.remove(pair.getKey());
            }
            dishesWithQuantity.put(pair.getKey(), count);
        }

        if (cartList.size() == 0) {
            deleteOrder(context, currentOrderID);
            currentOrderID = -1;
        }

        Log.d("3RD STEP", "UPDATED CART LIST");
        ///**debug**///
        Log.d("CartControl", dishesWithQuantity.toString());
    }

    private static void deleteOrder(Context context, int currentOrderID) {
        String cartLink = context.getResources().getString(R.string.delete_order_http_link);
        String orderID = String.valueOf(CartController.currentOrderID);

        AsyncTask<String, Void, String> deleter = new CreatorAndDeleter().execute(cartLink, orderID);
    }

    public static void createNewOrder(Context context) {
        String newOrderLink = context.getResources().getString(R.string.create_new_order_http_link);
        AsyncTask<String, Void, String> creator = new CreatorAndDeleter().execute(newOrderLink);

        try {
            Log.d("CREATED ORDER", String.valueOf(creator.get()));
            int newlyInsertedID = Integer.parseInt(creator.get());
            CartController.currentOrderID = newlyInsertedID;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("1ST STEP", "CREATED NEW ORDER");
    }

    public static void addToCart(Context context, int chosenDishID) {
        String cartLink = context.getResources().getString(R.string.add_to_cart_http_link);
        String orderID = String.valueOf(CartController.currentOrderID);
        String dishID = String.valueOf(chosenDishID);

        AsyncTask<String, Void, String> adder = new CreatorAndDeleter().execute(cartLink, dishID,
                orderID);

        Log.d("2ND STEP", "ADDED TO CART");
    }

    public static Dish fetchDishInfoParser(String string) throws JSONException {
        JSONArray arrayOfDishes;
        JSONObject jsonObject;
        Dish chosenDish = null;

        arrayOfDishes = new JSONArray(string);
        jsonObject = arrayOfDishes.getJSONObject(0);

        chosenDish = new Dish(jsonObject.getInt("id"), jsonObject.getInt("category_id"),
                jsonObject.getString("name"), jsonObject.getString("url").trim(),
                jsonObject.getDouble("price"));
        chosenDish.setCalorificValue(jsonObject.getDouble("calorific_value"));
        chosenDish.setDescription(jsonObject.getString("description").trim());
        chosenDish.setWeight(jsonObject.getDouble("weight"));

        return chosenDish;
    }

    public static Dish fetchDishInfo(Context context, int chosenDishID) {
        String link = context.getResources().getString(R.string.fetch_dish_info_http_link) + chosenDishID;
        AsyncTask<String, Void, String> dishInfoFetcher = new DishesFetcher().execute(link);
        String stringJSON;
        JSONArray arrayOfDishes;
        JSONObject jsonObject;
        Dish chosenDish = null;

        try {
            stringJSON = dishInfoFetcher.get();
            arrayOfDishes = new JSONArray(stringJSON);
            jsonObject = arrayOfDishes.getJSONObject(0);

            chosenDish = new Dish(jsonObject.getInt("id"), jsonObject.getInt("category_id"),
                    jsonObject.getString("name"), jsonObject.getString("url").trim(),
                    jsonObject.getDouble("price"));
            chosenDish.setCalorificValue(jsonObject.getDouble("calorific_value"));
            chosenDish.setDescription(jsonObject.getString("description").trim());
            chosenDish.setWeight(jsonObject.getDouble("weight"));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return chosenDish;
    }

    public static List<Dish> fetchDishesParser(String string) throws JSONException {
        JSONArray arrayOfDishes;
        JSONObject jsonObject;
        List<Dish> selectedDishesList = new ArrayList<>();

        arrayOfDishes = new JSONArray(string);

        for (int i = 0; i < arrayOfDishes.length(); i++) {
            jsonObject = arrayOfDishes.getJSONObject(i);
            selectedDishesList.add(new Dish(jsonObject.getInt("id"),
                    jsonObject.getInt("category_id"), jsonObject.getString("name"),
                    jsonObject.getString("url").trim(), jsonObject.getDouble("price")));
        }

        return selectedDishesList;
    }

    public static List<Dish> fetchDishes(Context context, int categoryID) {
        String link = context.getResources().getString(R.string.fetch_dishes_http_link) + categoryID;
        AsyncTask<String, Void, String> dishesFetcher = new DishesFetcher().execute(link);
        String stringJSON;
        JSONArray arrayOfDishes;
        JSONObject jsonObject;
        List<Dish> selectedDishesList = new ArrayList<>();

        try {

            stringJSON = dishesFetcher.get();
            arrayOfDishes = new JSONArray(stringJSON);

            for (int i = 0; i < arrayOfDishes.length(); i++) {
                jsonObject = arrayOfDishes.getJSONObject(i);
                selectedDishesList.add(new Dish(jsonObject.getInt("id"), categoryID,
                        jsonObject.getString("name"), jsonObject.getString("url").trim(),
                        jsonObject.getDouble("price")));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return selectedDishesList;
    }

    public static double totalPriceParser(String string) throws JSONException {
        JSONArray arrayOfDishes;
        JSONObject jsonObject = null;
        double totalPrice = -1;

        arrayOfDishes = new JSONArray(string);

        if (arrayOfDishes.length() > 0) {
            jsonObject = arrayOfDishes.getJSONObject(0);
        }

        if (jsonObject != null)
            totalPrice = jsonObject.getDouble("SUM(price)");

        totalCost = totalPrice;
        return totalPrice;
    }

    public static double getTotalPrice(Context context, int orderID) {
        String link = context.getResources().getString(R.string.get_total_price_http_link) + orderID;
        AsyncTask<String, Void, String> dishesFetcher = new DishesFetcher().execute(link);
        String stringJSON;
        JSONArray arrayOfDishes;
        JSONObject jsonObject = null;
        double totalPrice = -1;

        try {
            stringJSON = dishesFetcher.get();
            arrayOfDishes = new JSONArray(stringJSON);

            if (arrayOfDishes.length() > 0) {
                jsonObject = arrayOfDishes.getJSONObject(0);
            }

            if (jsonObject != null)
                totalPrice = jsonObject.getDouble("SUM(price)");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        totalCost = totalPrice;
        return totalPrice;
    }

    public static void deleteDishFromCart(Context context, int dishID) {
        String cartLink = context.getResources().getString(R.string.remove_items_http_link);
        String orderID = String.valueOf(CartController.currentOrderID);
        String dishIDString = String.valueOf(dishID);

        AsyncTask<String, Void, String> deleter = new CreatorAndDeleter().execute(cartLink,
                dishIDString, orderID);
    }

    public static void deleteAnItem(Context context, int dishID) {
        String cartLink = context.getResources().getString(R.string.remove_an_item_http_link);
        String orderID = String.valueOf(CartController.currentOrderID);
        String dishIDString = String.valueOf(dishID);

        AsyncTask<String, Void, String> deleter = new CreatorAndDeleter().execute(cartLink,
                dishIDString, orderID);
    }

    public static void addAnItem(Context context, int dishID, CartActivity cartActivity) {
        cartActivity.showProgressBar();
        addToCart(context, dishID);
    }

    public static void createNewCustomer(Context context, String customerName, String customerPhone) {
        String newCustomerLink = context.getResources().getString(R.string.create_new_customer_http_link);
        AsyncTask<String, Void, String> creator = new CustomerCreator().execute(newCustomerLink,
                customerName, customerPhone);
        try {
            int newlyInsertedID = Integer.parseInt(creator.get());
            CartController.currentCustomerID = newlyInsertedID;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void completeOrder(Context context, String destination) {
        assert currentOrderID != -1 && currentCustomerID != -1;

        String link = context.getResources().getString(R.string.complete_order_http_link);
        String orderIDString = String.valueOf(CartController.currentOrderID);
        String customerIDString = String.valueOf(CartController.currentCustomerID);

        AsyncTask<String, Void, String> orderCompleter = new OrderCompleter().execute(link,
                orderIDString, customerIDString, destination);

        try {
            String result = orderCompleter.get();
            if (result.trim().equals("Updated"))
                orderSuccessful = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}