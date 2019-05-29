package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;

public class Main extends Application {

    private Stage primaryStage;
    private ObservableList<ProcessModel> process = FXCollections.observableArrayList();
    private ObservableList<ProcessType> choiceBox = FXCollections.observableArrayList();


    public Main() {
        choiceBox.addAll(ProcessType.EVEN, ProcessType.PARABOLA, ProcessType.SINUS, ProcessType.EVERY_SECOND_HAS_MORE, ProcessType.NONE);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        /*Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();*/


        /*Controller controller = loader.getController();
        controller.setDialogStage(primaryStage);*/

        // Load root layout from fxml file.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class
                .getResource("sample.fxml"));
        AnchorPane rootLayout = (AnchorPane) loader.load();

        // Show the scene containing the root layout.
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);

        // Give the mainOverviewController access to the main app.
        Controller controller = loader.getController();
        controller.setDialogStage(primaryStage);
        controller.setMain(this);
        primaryStage.show();
    }


    public boolean showProcessEditDialog(ProcessModel book) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("ProcessEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit process");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            ProcessEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setProcessModel(book);
            controller.setMain(this);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    public ObservableList<ProcessModel> getProcessData() {
        return process;
    }

    public ObservableList<ProcessType> getTypeData() {
        return choiceBox;
    }

    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

}
