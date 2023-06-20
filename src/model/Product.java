package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This class defines the Product object.
 * @author James Wilson
 */

public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id, stock, min, max;
    private String name;
    private double price;
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.name = name;
        this.price = price;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @param part the part to associate with this product
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * @param selectedAssociatedPart the part to delete from associated parts
     * @return false if the part isn't found
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart)
    {
        // If the part is associated with the product, remove it and return true
        if(associatedParts.contains(selectedAssociatedPart)) {
            return associatedParts.remove(selectedAssociatedPart);
        }
        // If the part isn't associated with the product, return false
        else {
            return false;
        }
    }

    /**
     * @return all associated parts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
