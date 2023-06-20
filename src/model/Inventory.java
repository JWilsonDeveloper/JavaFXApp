package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Main;

/** This class defines the inventory and contains methods for adding, deleting, updating, and searching for parts and products.
 * @author James Wilson
 */

public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * @param newPart the part to add
     */
    public static void addPart(Part newPart)
    {
        allParts.add(newPart);
        ++Main.partIdCounter;
    }

    /**
     * @param newProduct the product to add
     */
    public static void addProduct(Product newProduct)
    {
        allProducts.add(newProduct);
        ++Main.productIdCounter;
    }

    /**
     * @param partId the id to search for
     * @return the part with the matching id
     * @throws Exception part not found
     */
    public static Part lookupPart(int partId) throws Exception
    {
        // Iterate through allParts and return the part with the matching id
        for(Part part: allParts) {
            if(part.getId() == partId) {
                return part;
            }
        }
        throw new Exception("No part was found with this ID");
    }

    /**
     * @param productId the id to search for
     * @return the product with the matching id
     * @throws Exception product not found
     */
    public static Product lookupProduct(int productId) throws Exception
    {
        // Iterate through allProducts and return the product with the matching id
        for(Product product: allProducts) {
            if(product.getId() == productId) {
                return product;
            }
        }
        throw new Exception("No product was found with this ID");
    }

    /**
     * @param partName the part name to search for
     * @return a list of parts with names that match/contain the searched name
     */
    public static ObservableList<Part> lookupPart(String partName)
    {
        // Create a list to store the search matches
        ObservableList<Part> searchMatches = FXCollections.observableArrayList();
        // Iterate through allParts and add any parts with names that match/contain the searched name to the list
        for(Part part: Inventory.getAllParts()) {
            if(String.valueOf(part.getName()).contains(partName)) {
                searchMatches.add(part);
            }
        }
        // Return the list of search matches
        return searchMatches;
    }

    /**
     * @param productName the product name to search for
     * @return a list of products with names that match/contain the searched name
     */
    public static ObservableList<Product> lookupProduct(String productName)
    {
        // Create a list to store the search matches
        ObservableList<Product> searchMatches = FXCollections.observableArrayList();
        // Iterate through allProducts and add any products with names that match/contain the searched name to the list
        for(Product product: Inventory.getAllProducts()) {
            if(String.valueOf(product.getName()).contains(productName)) {
                searchMatches.add(product);
            }
        }
        // Return the list of search matches
        return searchMatches;
    }

    /**
     * @param index the index of ObservableList<Part> allParts at which the part exists
     * @param selectedPart the part to update
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * @param index the index of ObservableList<Product> allProducts at which the product exists
     * @param newProduct the product to update
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * @param selectedPart the part to delete
     * @return false if the part isn't found
     */
    public static boolean deletePart(Part selectedPart)
    {
        // If the part is found...
        if(allParts.contains(selectedPart)) {
            // Iterate through all products and remove the part from any products with which it is associated
            for (Product product: allProducts){
                if(product.getAllAssociatedParts().contains(selectedPart)){
                    product.deleteAssociatedPart(selectedPart);
                }
            }
            // Remove the part from allParts
            allParts.remove(selectedPart);
        }
        return false;
    }

    /**
     * @param selectedProduct the product to delete
     * @return false if the product isn't found
     */
    public static boolean deleteProduct(Product selectedProduct)
    {
        // If the product is found...
        if(allProducts.contains(selectedProduct)) {
            // Remove the product from allProducts
            return allProducts.remove(selectedProduct);
        }
        return false;
    }

    /**
     * @return all parts
     */
    public static ObservableList<Part> getAllParts()
    {
        return allParts;
    }

    /**
     * @return all products
     */
    public static ObservableList<Product> getAllProducts()
    {
        return allProducts;
    }

}
