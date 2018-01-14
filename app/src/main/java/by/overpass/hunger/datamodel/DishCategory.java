package by.overpass.hunger.datamodel;

/**
 * Created by MSI GE62 2QE Apache on 22.10.2017.
 */

public class DishCategory {
    private final int name;
    private final int imageResourceID;

    public DishCategory(int name, int imageResourceID) {
        this.name = name;
        this.imageResourceID = imageResourceID;
    }

    public int getName() {
        return name;
    }

    public int getImageResourceID() {
        return imageResourceID;
    }
}
