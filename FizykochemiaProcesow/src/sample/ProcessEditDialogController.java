package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ProcessEditDialogController {

    @FXML
    private TextField tpField;
    @FXML
    private TextField tkField;
    @FXML
    private TextField ecField;
    @FXML
    private ComboBox<ProcessType> comboBox;

    private Stage dialogStage;
    private ProcessModel process;
    private boolean okClicked = false;

    private Main main;
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }
    /**r
     * Is called by the main application to give a reference back to itself.
     *
     * @param main
     */

    public void setMain(Main main) {
        this.main = main;
        comboBox.setItems(main.getTypeData());

        //comboBox.setValue(ProcessType.EVEN);
    }
    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the person to be edited in the dialog.
     *
     * @param process
     */
    public void setProcessModel(ProcessModel process) {
        this.process = process;

        tpField.setText(String.valueOf(process.getTp()));
        tkField.setText(String.valueOf(process.getTk()));
        ecField.setText(String.valueOf(process.getEc()));
        comboBox.setValue(process.getProcessType());
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            process.setTp(Integer.parseInt(tpField.getText()));
            process.setTk(Integer.parseInt(tkField.getText()));
            process.setEc(Double.parseDouble(ecField.getText()));
            process.setProcessType(comboBox.getValue());

            okClicked = true;
            dialogStage.close();
        }
    }



    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";


        if (tpField.getText() == null || tpField.getText().length() == 0 ) {
            errorMessage += "Niewłaściwa wartość temperatury początkowej!\n";
        } else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(tpField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Niewłaściwa wartość temperatury początkowej - temperatura musi być liczbą całkowitą!\n";
            }
        }

        if (tkField.getText() == null || tkField.getText().length() == 0) {
            errorMessage += "Niewłaściwa wartość temperatury końcowej!\n";
        } else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(tkField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Niewłaściwa wartość temperatury końcowej - temperatura musi być liczbą całkowitą!\n";
            }
        }

        if (ecField.getText() == null || ecField.getText().length() == 0) {
            errorMessage += "Niewłaściwa wartość efektu cieplnego!\n";
        } else {
            // try to parse the postal code into an int.
            try {
                Double.parseDouble(ecField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Niewłaściwa wartość efektu cieplnego - temperatura musi być liczbą!\n";
            }
        }


        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Niewłaściwe dane");
            alert.setHeaderText("Proszę poprawić niewłaściwe dane.");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}