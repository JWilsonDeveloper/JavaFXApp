package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;


/** This class is the controller for the main menu.
 * @author James Wilson
 */
public class MainMenuController implements Initializable {
    // Declaration of variables
    Stage stage;
    Parent scene;

    // GUI Controls
    @FXML
    private TextArea mainMenuMsgTxt;

    @FXML
    private TextField mainMenuPartSearchTxt;

    @FXML
    private TextField mainMenuProductSearchTxt;

    @FXML
    private TableColumn<Part, Integer> partIDCol;

    @FXML
    private TableColumn<Part, Integer> partInventoryCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private TableView<Part> partTableView;

    @FXML
    private TableColumn<Product, Integer> productIDCol;

    @FXML
    private TableColumn<Product, Integer> productInventoryCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, Double> productPriceCol;

    @FXML
    private TableView<Product> productTableView;

    /** This method deletes the selected part.
     * @param event The delete-part button was clicked.
     */
    @FXML
    void onActionDeletePart(ActionEvent event) {
        try {

            Part deletedPart = partTableView.getSelectionModel().getSelectedItem();
            AtomicBoolean confirmDelete = new AtomicBoolean(false);
            // Check if a part is selected
            if(deletedPart == null) {
                throw new NullPointerException("The delete-part button requires a selected part.");
            }
            // Otherwise, confirm the user wants to delete the part
            String partName = deletedPart.getName();
            Alert deleteConfirm = new Alert(AlertType.CONFIRMATION);
            deleteConfirm.setContentText("Are you sure you would like to delete this part?\nPart: " + partName);
            deleteConfirm.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> confirmDelete.set(true));
            // If the user confirms...
            if (confirmDelete.get()) {
                // Delete the part, display a message, and refresh the parts table
                Inventory.deletePart(deletedPart);
                mainMenuMsgTxt.setText(partName + " deleted from Parts table");
                partTableView.setItems(Inventory.getAllParts());
            }
        }
        catch (NullPointerException e) {
            mainMenuMsgTxt.setText(e.getMessage());
        }
    }

    /** This method deletes the selected product.
     * @param event The delete-product button was clicked.
     */
    @FXML
    void onActionDeleteProduct(ActionEvent event) {
        try {
            Product deletedProduct = productTableView.getSelectionModel().getSelectedItem();
            AtomicBoolean confirmDelete = new AtomicBoolean(false);
            // Check if a product is selected
            if (deletedProduct == null) {
                throw new NullPointerException("The delete-product button requires a selected part.");
            }
            // Check if the product has associated parts
            if (!(deletedProduct.getAllAssociatedParts().isEmpty())) {
                throw new Exception("Products with one or more associated parts may not be deleted.");
            }
            // Otherwise, confirm the user wants to delete the product
            String productName = deletedProduct.getName();
            Alert deleteConfirm = new Alert(AlertType.CONFIRMATION);
            deleteConfirm.setContentText("Are you sure you would like to delete this product?\nProduct: " + deletedProduct.getName());
            deleteConfirm.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> confirmDelete.set(true));
            // If the user confirms...
            if (confirmDelete.get()) {
                // Delete the product, display a message, and refresh the products table
                Inventory.deleteProduct(deletedProduct);
                mainMenuMsgTxt.setText(productName + " deleted from Products table");
                productTableView.setItems(Inventory.getAllProducts());
            }
        } catch (Exception e) {
            mainMenuMsgTxt.setText(e.getMessage());
        }
    }

    /** This method filters the parts table based on the parts search bar.
     * @param event The enter key was pressed on the parts search bar.
     */
    @FXML
    void onKeyPressPartSearch(KeyEvent event) {
        // If the enter key is pressed...
        if(event.getCode().getCode() == 10) {
            try {
                // Check if the search text is an integer matching a part ID
                partTableView.getSelectionModel().select(Inventory.lookupPart(Integer.parseInt(mainMenuPartSearchTxt.getText())));
            }
            catch (Exception e) {
                // If the search text does not match a part name...
                if(Inventory.lookupPart(mainMenuPartSearchTxt.getText()).isEmpty()){
                    // Display a message and refresh the parts table
                    partTableView.setItems(Inventory.getAllParts());
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setContentText("No parts matched your search.");
                    alert.show();
                }
                // Otherwise, filter the parts table based on the search text
                else {
                    partTableView.setItems(Inventory.lookupPart(mainMenuPartSearchTxt.getText()));
                }
            }
        }
    }

    /** This method filters the products table based on the products search bar.
     * @param event The enter key was pressed on the products search bar.
     */
    @FXML
    void onKeyPressProductSearch(KeyEvent event) {
        // If the enter key is pressed...
        if(event.getCode().getCode() == 10) {
            try {
                // Check if the search text is an integer matching a product ID
                productTableView.getSelectionModel().select(Inventory.lookupProduct(Integer.parseInt(mainMenuProductSearchTxt.getText())));
            }
            catch (Exception e) {
                // If the search text does not match a product name...
                if(Inventory.lookupProduct(mainMenuProductSearchTxt.getText()).isEmpty()){
                    // Display a message and refresh the products table
                    productTableView.setItems(Inventory.getAllProducts());
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setContentText("No products matched your search.");
                    alert.show();
                }
                // Otherwise, filter the products table based on the search text
                else {
                    productTableView.setItems(Inventory.lookupProduct(mainMenuProductSearchTxt.getText()));
                }
            }
        }
    }

    /** This method loads the add part menu.
     * @param event The add-part button was clicked.
     * @throws IOException Scene unable to launch; error loading file.
     */
    @FXML
    void onActionAddPart(ActionEvent event) throws IOException {
        // Load the add part menu
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPartMenu.fxml"));
        stage.setScene(new Scene(scene));
    }

    /** This method loads the add product menu.
     * @param event The add-product button was clicked.
     * @throws IOException Scene unable to launch; error loading file.
     */
    @FXML
    void onActionAddProduct(ActionEvent event) throws IOException {
        // Load the add product menu
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProductMenu.fxml"));
        stage.setScene(new Scene(scene));
    }

    /** This method loads the modify part menu and sends the selected part to the modify part menu.
     * @param event The modify-part button was clicked.
     * @throws IOException Scene unable to launch; error loading file.
     */
    @FXML
    void onActionModifyPart(ActionEvent event) throws IOException {
        try
        {
            // Create a new loader
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyPartMenu.fxml"));
            loader.load();

            // Send the selected part to the modify part menu controller
            ModifyPartMenuController MPartMController = loader.getController();
            MPartMController.sendPart(partTableView.getSelectionModel().getSelectedItem());

            // Load the modify part menu
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (NullPointerException e) {
            mainMenuMsgTxt.setText("The modify-part button requires a selected part.");
        }
    }

    /** This method loads the modify product menu and sends the selected product to the modify product menu.
     * @param event The modify product button was clicked.
     * @throws IOException Scene unable to launch; error loading file.
     */
    @FXML
    void onActionModifyProduct(ActionEvent event) throws IOException {
        try {
            // Create a new loader
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyProductMenu.fxml"));
            loader.load();

            // Send the selected product to the modify product menu controller
            ModifyProductMenuController MProductMController = loader.getController();
            MProductMController.sendProduct(productTableView.getSelectionModel().getSelectedItem());

            // Load the modify product menu
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (NullPointerException e) {
            mainMenuMsgTxt.setText("The modify-product button requires a selected product.");
        }
    }

    /** This method exits the program.
     * @param event exit button clicked
     */
    @FXML
    void onActionExit(ActionEvent event) {
        System.exit(0);
    }


     /*
        Future enhancement opportunity:
        Price columns in both tables should be set so that
        prices appear with two decimal places automatically.
     */

    /** This method loads existing parts and products into the tableviews.
     * @param url The file path.
     * @param resourceBundle null
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load parts into the parts table
        partTableView.setItems(Inventory.getAllParts());
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Load products into the products table
        productTableView.setItems(Inventory.getAllProducts());
        productIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
