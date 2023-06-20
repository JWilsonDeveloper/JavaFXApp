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
import main.Main;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

/** This class is the controller for the add product menu.
 * @author James Wilson
 */
public class AddProductMenuController implements Initializable {
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
    private TextArea addProductMenuMsgTxt;

    /** This method adds a part to associated parts table.
     * @param event The add button was clicked.
     */
    @FXML
    void onActionAssociatePart(ActionEvent event) {
        // If a part is selected...
        if (partTableView.getSelectionModel().getSelectedItem() != null) {
            // Add the selected part to the product's associated parts and reset the associated parts table.
            associatedParts.add(partTableView.getSelectionModel().getSelectedItem());
            assocPartTableView.setItems(associatedParts);
            assocPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            assocPartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            assocPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        }
        // Otherwise, display an error message.
        else {
            addProductMenuMsgTxt.setText("The add button requires a selected part.");
        }
    }

    /** This method removes a part from the associated parts list.
     * @param event The remove associated button was clicked.
     */
    @FXML
    void onActionRemoveAssociatedPart(ActionEvent event) {
        // If a part is selected...
        if (assocPartTableView.getSelectionModel().getSelectedItem() != null) {
            // Display a confirmation dialog.
            AtomicBoolean confirmDelete = new AtomicBoolean(false);
            Part associatedPart = assocPartTableView.getSelectionModel().getSelectedItem();
            Alert removeConfirm = new Alert(Alert.AlertType.CONFIRMATION);
            removeConfirm.setContentText("Are you sure you would like to remove this associated part?\nPart: " + associatedPart.getName());
            removeConfirm.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> confirmDelete.set(true));
            // If the user confirms...
            if (confirmDelete.get()) {
                // Remove the part from the associated parts list, display a message, and reset the associated parts table.
                addProductMenuMsgTxt.setText(associatedPart.getName() + " deleted from the Associated Parts table");
                associatedParts.remove(assocPartTableView.getSelectionModel().getSelectedItem());
                assocPartTableView.setItems(associatedParts);
            }
        }
        // Otherwise, display an error message.
        else {
            addProductMenuMsgTxt.setText("The remove-associated-part button requires a selected part.");
        }
    }

    /** This method filters the parts table based on the search text field.
     * @param event The enter key was pressed on the part search bar.
     */
    @FXML
    void onKeyPressedPartSearch(KeyEvent event) {
        // If the enter key was pressed...
        if(event.getCode().getCode() == 10) {
            try {
                // Check if the search text is an integer matching a part ID.
                partTableView.getSelectionModel().select(Inventory.lookupPart(Integer.parseInt(addProductSearchTxt.getText())));
            }
            catch (Exception e) {
                // If the search text does not match a part name...
                if(Inventory.lookupPart(addProductSearchTxt.getText()).isEmpty()){
                    // Display a message and reset the parts table.
                    partTableView.setItems(Inventory.getAllParts());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setContentText("No parts matched your search.");
                    alert.showAndWait();
                }
                // Otherwise, filter the parts table based on the search text.
                else {
                    partTableView.setItems(Inventory.lookupPart(addProductSearchTxt.getText()));
                }
            }
        }
    }

    /** This method loads the main menu.
     * @param event The cancel button was clicked.
     * @throws IOException Scene unable to launch; error loading file.
     */
    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        // Load the main menu.
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
    }

    /** This method eliminates some redundancy for onActionSaveProduct().
     * It prints errors preventing the product from saving.
     * @param eMessage The exception message to be printed.
     */
    void exceptionPrint(String eMessage) {
        // If this isn't the first error, add a new line
        if (!addProductMenuMsgTxt.getText().equals("")) {
            addProductMenuMsgTxt.setText(addProductMenuMsgTxt.getText() + "\n");
        }
        // Add the error message
        addProductMenuMsgTxt.setText(addProductMenuMsgTxt.getText() + eMessage);
        eOccurred = true;
    }

    /** This method saves the product and loads the main menu.
     * @param event The save button was clicked.
     */
    @FXML
    void onActionSaveProduct(ActionEvent event) {
        addProductMenuMsgTxt.setText("");
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
        // If no errors occurred, check for more errors.
        if (!eOccurred) {
            try {
                int id = Main.productIdCounter;
                String name = productNameTxt.getText();
                Double price = Double.parseDouble(priceTxt.getText());
                int stock = Integer.parseInt(inventoryTxt.getText());
                int min = Integer.parseInt(minTxt.getText());
                int max = Integer.parseInt(maxTxt.getText());
                if (min >= max) {
                    exceptionPrint("Min must be less than Max");
                }
                if (!(stock <= max && stock >= min)) {
                    exceptionPrint("Inventory must be between Min and Max");
                }
                // If an error still hasn't occurred...
                if (!eOccurred) {
                    // Create a new product and add it to the inventory.
                    Product newProduct = new Product(id, name, price, stock, min, max);
                    Inventory.addProduct(newProduct);
                    for (Part part : associatedParts) {
                        newProduct.addAssociatedPart(part);
                    }
                    // Load the main menu
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                    stage.setScene(new Scene(scene));
                }
            } catch (Exception e) {
                exceptionPrint("Unable to save product. Error unaccounted for.");
            }
        }
    }

    /** This method loads existing parts into the parts table.
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
    }
}
