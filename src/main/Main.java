package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

/** This class creates an application for managing an inventory of parts and products.
 * @author James Wilson
 */
public class Main extends Application {
    // These counters are used to generate unique IDs for parts and products
    public static int partIdCounter = 1;
    public static int productIdCounter = 1;


    /** This is the main method.
     * It preloads the tableviews of parts and products with data at startup, and launches the program.
     * @param args an array storing command-line arguments
     */
    public static void main(String[] args) {

        // Preloaded part information
        InHouse part1 = new InHouse(1, "Axle", 9.99, 8, 4, 32, 10001000);
        Outsourced part2 = new Outsourced(2, "Wheel", 4.99, 24, 12, 64, "Jimmy's Wheels");
        InHouse part3 = new InHouse(3, "String", 3.99, 4, 2, 10, 10001001);
        Outsourced part4 = new Outsourced(4, "Weight", 4.79, 2, 1, 8, "Why Weight");

        // Add parts to tableview
        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);
        Inventory.addPart(part4);

        // Preloaded product information
        Product product1 = new Product(1, "Toy Car", 39.94, 4, 2, 16);
        Product product2 = new Product(2, "Pulley", 23.76, 2, 1, 8);

        // Add products to tableview
        Inventory.addProduct(product1);
        product1.addAssociatedPart(part1);
        product1.addAssociatedPart(part2);
        Inventory.addProduct(product2);
        product2.addAssociatedPart(part3);
        product2.addAssociatedPart(part4);

        launch(args);
    }

    /** This is the start method. It loads and displays the main menu.
     * @param primaryStage the stage, a javafx container for scenes
     * @throws Exception scene unable to launch; error loading file
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/MainMenu.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
