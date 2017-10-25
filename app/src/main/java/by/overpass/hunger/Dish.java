package by.overpass.hunger;

/**
 * Created by MSI GE62 2QE Apache on 24.10.2017.
 */

public class Dish {

    private int id;
    private int categoryID;
    private String name;
    private String url;
    private String description;
    private float weight;
    private float calorificValue;
    private float price;

    public Dish(int id, int categoryID, String name, String url, float price) {
        this.id = id;
        this.categoryID = categoryID;
        this.name = name;
        this.url = url;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public Float getWeight() {
        return weight;
    }

    public Float getCalorificValue() {
        return calorificValue;
    }

    public Float getPrice() {
        return price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setCalorificValue(float calorificValue) {
        this.calorificValue = calorificValue;
    }
}
