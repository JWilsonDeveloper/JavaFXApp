package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** This class is the controller for the modify part menu.
 * @author James Wilson
 */
public class ModifyPartMenuController implements Initializable {
    // Declaration of variables
    Stage stage;
    Parent scene;
    boolean eOccurred;

    // GUI Controls
    @FXML
    private RadioButton inHouseRBtn;

    @FXML
    private ToggleGroup inOutTG;

    @FXML
    private TextField inventoryTxt;

    @FXML
    private TextField machineIDTxt;

    @FXML
    private TextField maxTxt;

    @FXML
    private TextField minTxt;

    @FXML
    private RadioButton outsourcedRBtn;

    @FXML
    private TextField partIDTxt;

    @FXML
    private TextField partNameTxt;

    @FXML
    private TextField priceTxt;

    @FXML
    private Label machineCompanyLbl;

    @FXML
    private TextArea modifyPartMenuMsgTxt;

    /** This method changes the last text field label.
     * @param event The outsourced radio button was clicked.
     */
    @FXML
    void onActionCompanyLbl(ActionEvent event) {
        machineCompanyLbl.setText("Company Name");
    }

    /** This method changes the last text field label.
     * @param event The in-House radio button was clicked.
     */
    @FXML
    void onActionMachineLbl(ActionEvent event) {
        machineCompanyLbl.setText("Machine ID");
    }

    /** This method loads part data into the text fields.
     * @param part The part to be modified.
     */
    public void sendPart(Part part) {
        // Fill the text fields with the part's data
        partIDTxt.setText(String.valueOf(part.getId()));
        partNameTxt.setText(part.getName());
        priceTxt.setText(String.valueOf(part.getPrice()));
        inventoryTxt.setText(String.valueOf(part.getStock()));
        minTxt.setText(String.valueOf(part.getMin()));
        maxTxt.setText(String.valueOf(part.getMax()));
        if (part instanceof InHouse) {
            machineIDTxt.setText(String.valueOf(((InHouse) part).getMachineId()));
            inHouseRBtn.setSelected(true);
            machineCompanyLbl.setText("Machine ID");
        } else if (part instanceof Outsourced) {
            machineIDTxt.setText(((Outsourced) part).getCompanyName());
            outsourcedRBtn.setSelected(true);
            machineCompanyLbl.setText("Company Name");
        }
    }

    /** This method loads the main menu.
     * @param event The cancel button was clicked.
     * @throws IOException Scene unable to launch; error loading file.
     */
    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        // Load the main menu
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
    }

    /** This method eliminates some redundancy for onActionSavePart().
     * It prints errors preventing the part from saving.
     * @param eMessage The exception message to be printed.
     */
    void exceptionPrint(String eMessage) {
        // If this isn't the first error, add a new line
        if (!modifyPartMenuMsgTxt.getText().equals("")) {
            modifyPartMenuMsgTxt.setText(modifyPartMenuMsgTxt.getText() + "\n");
        }
        // Add the error message
        modifyPartMenuMsgTxt.setText(modifyPartMenuMsgTxt.getText() + eMessage);
        eOccurred = true;
    }

    /** This method saves the part and loads the main menu.
     * @param event The save button was clicked.
     */
    @FXML
    void onActionSavePart(ActionEvent event) {
        modifyPartMenuMsgTxt.setText("");
        eOccurred = false;
        boolean onlyDigits = true;

        // Error checking
        if (partNameTxt.getText().equals("")) {
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
        if (machineIDTxt.getText().equals("")) {
            exceptionPrint("MachineID/CompanyName requires input");
        }
        if (!(inHouseRBtn.isSelected()) && !(outsourcedRBtn.isSelected())) {
            exceptionPrint("Neither radio button was selected");
        }
        for(int i = 0; i < machineIDTxt.getText().length(); ++i) {
            if (!Character.isDigit(machineIDTxt.getText().charAt(i))) {
                onlyDigits = false;
            }
        }
        if (inHouseRBtn.isSelected() && !onlyDigits) {
            exceptionPrint("Machine ID is not an integer");
        }
        // If an error hasn't occurred, check for more errors
        if(!eOccurred) {
            try {
                int id = Integer.parseInt(partIDTxt.getText());
                String name = partNameTxt.getText();
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
                    // Set i to the index of the part to be modified
                    for (Part part : Inventory.getAllParts()) {
                        if (part.getId() == id) {
                            break;
                        }
                        ++i;
                    }
                    // Update the part as either an InHouse part or an Outsourced part
                    if (outsourcedRBtn.isSelected()) {
                        String company = machineIDTxt.getText();
                        Inventory.updatePart(i, new Outsourced(id, name, price, stock, min, max, company));
                    } else {
                        int machineId = Integer.parseInt(machineIDTxt.getText());
                        Inventory.updatePart(i, new InHouse(id, name, price, stock, min, max, machineId));
                    }
                    // Load the main menu
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                    stage.setScene(new Scene(scene));
                }
            } catch (Exception e) {
                exceptionPrint("Unable to save part. Error unaccounted for.");
            }
        }
    }

    /**
     * @param url file path
     * @param resourceBundle null
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }
}
