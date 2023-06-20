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
import main.Main;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** This class is the controller for the add part menu.
 * @author James Wilson
 */
public class AddPartMenuController implements Initializable {
    // Declaration of variables
    Stage stage;
    Parent scene;
    boolean eOccurred = false;

    // GUI Controls
    @FXML
    private ToggleGroup inOutTG;

    @FXML
    private RadioButton inHouseRBtn;

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
    private TextArea addPartMenuMsgTxt;

    /** This method changes the last text field label.
     * @param event The outsourced radio button was clicked.
     */
    @FXML
    void onActionCompanyLbl(ActionEvent event) {
        machineCompanyLbl.setText("Company Name");
    }

    /** This method changes the last text field label.
     * @param event The in_House radio button was clicked.
     */
    @FXML
    void onActionMachineLbl(ActionEvent event) {
        machineCompanyLbl.setText("Machine ID");
    }

    /** This method loads the main menu.
     * @param event The cancel button was clicked.
     * @throws IOException Scene unable to launch; error loading file.
     */
    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        // Load main menu
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
    }

    /** This method eliminates some redundancy for onActionSavePart().
     * It prints errors preventing the part from saving.
     * @param eMessage The exception message to be printed.
     */
    void exceptionPrint(String eMessage) {
        // If this is not the first error message, add a new line
        if (!addPartMenuMsgTxt.getText().equals("")) {
            addPartMenuMsgTxt.setText(addPartMenuMsgTxt.getText() + "\n");
        }
        // Add the error message
        addPartMenuMsgTxt.setText(addPartMenuMsgTxt.getText() + eMessage);
        eOccurred = true;
    }

    /*
        Future enhancement opportunity:
        The save part/product methods may be an area for improvement.
        Consecutive try/catch blocks appeared necessary in order to
        register multiple exception messages at once,
        but these cluttered up the code.
     */
    /** This method saves the part to the inventory and loads the main menu.
     * @param event The save button was clicked.
     */
    @FXML
    void onActionSavePart(ActionEvent event) {
        addPartMenuMsgTxt.setText("");
        boolean onlyDigits = true;
        eOccurred = false;

        //Check for errors
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
        if (!eOccurred) {
            try {
                int id = Main.partIdCounter;
                String name = partNameTxt.getText();
                int stock = Integer.parseInt(inventoryTxt.getText());
                int max = Integer.parseInt(maxTxt.getText());
                int min = Integer.parseInt(minTxt.getText());
                Double price = Double.parseDouble(priceTxt.getText());
                if (min >= max) {
                    exceptionPrint("Min must be less than Max");
                }
                if (!(stock <= max && stock >= min)) {
                    exceptionPrint("Inventory must be between Min and Max");
                }
                // If an error still hasn't occurred, save the part as either in-house or outsourced.
                if(!eOccurred) {
                    if (outsourcedRBtn.isSelected()) {
                        String company = machineIDTxt.getText();
                        Outsourced newOutsourced = new Outsourced(id, name, price, stock, min, max, company);
                        Inventory.addPart(newOutsourced);
                    } else {
                        int machineId = Integer.parseInt(machineIDTxt.getText());
                        InHouse newInHouse = new InHouse(id, name, price, stock, min, max, machineId);
                        Inventory.addPart(newInHouse);
                    }
                    // Load main menu
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
     * @param url The file path.
     * @param resourceBundle null
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
