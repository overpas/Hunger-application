package by.overpass.hunger.datamodel;

/**
 * Created by MSI GE62 2QE Apache on 24.10.2017.
 */

public class Dish {

    private int id;
    private int categoryID;
    private String name;
    private String url;
    private String description;
    private double weight;
    private double calorificValue;
    private double price;

    public Dish(int id, int categoryID, String name, String url, double price) {
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

    public double getWeight() {
        return weight;
    }

    public double getCalorificValue() {
        return calorificValue;
    }

    public double getPrice() {
        return price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setCalorificValue(double calorificValue) {
        this.calorificValue = calorificValue;
    }

    @Override
    public String toString() {
        return "id = " + getId() + ", name = " + getName() + ", url = " + getUrl();
    }
}
