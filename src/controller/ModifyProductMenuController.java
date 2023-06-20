package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

/** This class is the controller for the modify product menu.
 * @author James Wilson
 */
public class ModifyProductMenuController implements Initializable {
    // Declaration of variables
    Stage stage;
    Parent scene;
    ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    boolean eOccurred;

    // GUI Controls
    @FXML
    private TextField addProductSearchTxt;

    @FXML
    private TableColumn<Part, Integer> assocPartIDCol;

    @FXML
    private TableColumn<Part, Integer> assocPartInventoryCol;

    @FXML
    private TableColumn<Part, String> assocPartNameCol;

    @FXML
    private TableColumn<Part, Double> assocPartPriceCol;

    @FXML
    private TableView<Part> assocPartTableView;

    @FXML
    private TextField inventoryTxt;

    @FXML
    private TextField maxTxt;

    @FXML
    private TextField minTxt;

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
    private TextField priceTxt;

    @FXML
    private TextField productIDTxt;

    @FXML
    private TextField productNameTxt;

    @FXML
    private TextArea modifyProductMenuMsgTxt;

    /** This method adds a part to the associated parts table.
     * @param event Add button was clicked.
     */
    @FXML
    void OnActionAssociatePart(ActionEvent event) {
        // If a part is selected...
        if (partTableView.getSelectionModel().getSelectedItem() != null)
        {
            // Add the part to the associated parts list
            associatedParts.add(partTableView.getSelectionModel().getSelectedItem());
        }
        else {
            modifyProductMenuMsgTxt.setText("The add button requires a selected part.");
        }
    }

    /** This method removes a part from the associated parts list.
     * @param event Remove associated part button was clicked.
     */
    @FXML
    void onActionRemoveAssociatedPart(ActionEvent event) {
        // If a part is selected...
        if (assocPartTableView.getSelectionModel().getSelectedItem() != null) {
            // Display a confirmation dialog
            AtomicBoolean confirmDelete = new AtomicBoolean(false);
            Part associatedPart = assocPartTableView.getSelectionModel().getSelectedItem();
            Alert removeConfirm = new Alert(Alert.AlertType.CONFIRMATION);
            removeConfirm.setContentText("Are you sure you would like to remove this associated part?\nPart: " + associatedPart.getName());
            removeConfirm.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> confirmDelete.set(true));
            // If the user confirms the delete...
            if (confirmDelete.get()) {
                // Remove the associated part, display a message, and reset the table
                modifyProductMenuMsgTxt.setText(associatedPart.getName() + " deleted from the Associated Parts table");
                associatedParts.remove(assocPartTableView.getSelectionModel().getSelectedItem());
                assocPartTableView.setItems(associatedParts);
            }
        }
        else {
            modifyProductMenuMsgTxt.setText("The remove-associated-part button requires a selected part.");
        }
    }

    /** This method filters the parts table based on the search bar.
     * @param event Enter was pressed on the part search bar.
     */
    @FXML
    void onKeyPressedPartSearch(KeyEvent event) {
        // If the enter key is pressed...
        if(event.getCode().getCode() == 10) {
            try {
                // Check if the search text is an integer matching a part ID
                partTableView.getSelectionModel().select(Inventory.lookupPart(Integer.parseInt(addProductSearchTxt.getText())));
            }
            catch (Exception e) {
                // If the search text does not match a part name...
                if(Inventory.lookupPart(addProductSearchTxt.getText()).isEmpty()){
                    // Display a message and reset the table
                    partTableView.setItems(Inventory.getAllParts());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setContentText("No parts matched your search.");
                    alert.showAndWait();
                }
                else {
                    // Otherwise, filter the table based on the search text
                    partTableView.setItems(Inventory.lookupPart(addProductSearchTxt.getText()));
                }
            }
        }
    }

    /** This method fills the text fields with the product's data.
     * @param product The product to be modified.
     */
    public void sendProduct(Product product) {
        /*
            Error addressed:
            associatedParts was assigned as product.getAllAssociatedParts().
            This gave them identical references,
            meaning product.associatedParts might be changed
            even if the user canceled rather than saving modifications.
            Instead, associatedParts is now created as a copy of product.associatedParts.
        */
        // Fill the text fields with the product's data
        productIDTxt.setText(String.valueOf(product.getId()));
        productNameTxt.setText(product.getName());
        priceTxt.setText(String.valueOf(product.getPrice()));
        inventoryTxt.setText(String.valueOf(product.getStock()));
        minTxt.setText(String.valueOf(product.getMin()));
        maxTxt.setText(String.valueOf(product.getMax()));
        // Iterate through product's associated parts and add them to associatedParts
        for (Part part: product.getAllAssociatedParts()) {
            associatedParts.add(part);
        }
    }

    /** This method loads the main menu.
     * @param event Cancel button was clicked.
     * @throws IOException Scene unable to launch; error loading file.
     */
    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        // Load main menu
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
    }

    /**
     * This method eliminates some redundancy for onActionSaveProduct().
     * It prints errors preventing the product from saving.
     * @param eMessage The exception message to print.
     */
    void exceptionPrint(String eMessage) {
        // If this isn't the first error, add a new line
        if (!modifyProductMenuMsgTxt.getText().equals("")) {
            modifyProductMenuMsgTxt.setText(modifyProductMenuMsgTxt.getText() + "\n");
        }
        // Add the error message
        modifyProductMenuMsgTxt.setText(modifyProductMenuMsgTxt.getText() + eMessage);
        eOccurred = true;
    }

    /** This method saves the modified product and loads the main menu.
     * @param event The save button was clicked.
     */
    @FXML
    void onActionSaveProduct(ActionEvent event) {
        // Clear the message field
        modifyProductMenuMsgTxt.setText("");
        eOccurred = false;
        // Check for errors
        if (productNameTxt.getText().equals("")) {
            exceptionPrint("No data in name field");
        }
        try {
            Integer.parseInt(inventoryTxt.getText());
        } catch (NumberFormatException e) {
            exceptionPrint("Inventory is not an integer");
        }
        try {
            Double.parseDouble(priceTxt.getText());
        } catch (NumberFormatException e) {
            exceptionPrint("Price is not a double");
        }
        try {
            Integer.parseInt(maxTxt.getText());
        } catch (NumberFormatException e) {
            exceptionPrint("Max is not an integer");
        }
        try {
            Integer.parseInt(minTxt.getText());
        } catch (NumberFormatException e) {
            exceptionPrint("Min is not an integer");
        }
        // If an error hasn't occurred, check for more errors
        if (!eOccurred) {
            try {
                int id = Integer.parseInt(productIDTxt.getText());
                String name = productNameTxt.getText();
                Double price = Double.parseDouble(priceTxt.getText());
                int stock = Integer.parseInt(inventoryTxt.getText());
                int min = Integer.parseInt(minTxt.getText());
                int max = Integer.parseInt(maxTxt.getText());
                int i = 0;
                if (min >= max) {
                    exceptionPrint("Min must be less than Max");
                }
                if (!(stock <= max && stock >= min)) {
                    exceptionPrint("Inventory must be between Min and Max");
                }
                // If an error still hasn't occurred...
                if (!eOccurred) {
                    // Get the product's id
                    for (Product product : Inventory.getAllProducts()) {
                        if (product.getId() == id) {
                            break;
                        }
                        ++i;
                    }
                    // Update the product
                    Product updatedProduct = new Product(id, name, price, stock, min, max);
                    Inventory.updateProduct(i, updatedProduct);

                    // Delete previously associated parts
                    while (updatedProduct.getAllAssociatedParts().size() > 0) {
                        updatedProduct.deleteAssociatedPart(updatedProduct.getAllAssociatedParts().get(0));
                    }
                    // Add associated parts to updatedProduct
                    for (Part part : associatedParts) {
                        updatedProduct.addAssociatedPart(part);
                    }

                    // Load main menu
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                    stage.setScene(new Scene(scene));
                }
            } catch (Exception e) {
                exceptionPrint("Unable to save product. Error unaccounted for.");
            }
        }
    }

    /** This method loads existing parts into parts table and loads associated parts into associated parts table.
     * @param url The file path.
     * @param resourceBundle null
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load parts into parts table
        partTableView.setItems(Inventory.getAllParts());
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Load associated parts into associated parts table
        assocPartTableView.setItems(associatedParts);
        assocPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
