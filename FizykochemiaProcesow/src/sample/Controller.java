package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {


    @FXML
    private TableView<ProcessModel> processModelTable;
    @FXML
    private TableColumn<ProcessModel, Integer> tpColumn;
    @FXML
    private TableColumn<ProcessModel, Integer> tkColumn;
    @FXML
    private TableColumn<ProcessModel, Double> ecColumn;

    @FXML
    private TableColumn<ProcessModel, ProcessType> typeColumn;

    Stage dialogStage;

    @FXML
    LineChart scatterChart;
    @FXML
    CategoryAxis xAxis;
    @FXML
    NumberAxis yAxis;
    @FXML
    TextArea textArea;

    // Reference to the main application.
    private Main main;

    XYChart.Series<String, Double> aSeries;

    ObservableList<XYChart.Series<String, Double>> answer;

    int[] T;
    double[] Cp;
    double[] deltaH;
    int processesQuantity;
    int[] processesTp;
    int[] processesTk;
    double[] processesEk;
    List<double[]> enthapls = new ArrayList<>();
    List<ProcessModel[]> currentProcesses = new ArrayList<>();
    @FXML
    public void initialize() {

        tpColumn.setCellValueFactory(
                cellData -> cellData.getValue().tpProperty().asObject());
        tkColumn.setCellValueFactory(
                cellData -> cellData.getValue().tkProperty().asObject());
        ecColumn.setCellValueFactory(
                cellData -> cellData.getValue().ecProperty().asObject());
        typeColumn.setCellValueFactory(
                cellData -> cellData.getValue().processTypeProperty());

        // Clear book details.
        showProcessDetails(null);

        // Listen for selection changes and show the book details when changed.
        processModelTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showProcessDetails(newValue));


    }

    private void showProcessDetails(ProcessModel processModel) {
        /*if (processModel != null) {
            // Fill the labels with info from the person object.
            tpLabel.setText(processModel.getTp());
            tkLabel.setText(processModel.getTk());
            ecLabel.setText(processModel.getEc());
        } else {
            // Person is null, remove all the text.
            titleLabel.setText("");
            authorLabel.setText("");
            publisherLabel.setText("");
            subjectLabel.setText("");
            copiesLabel.setText("");
            statusLabel.setText("");
        }*/
    }

    public void setMain(Main main) {
        this.main = main;

        processModelTable.setItems(main.getProcessData());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    @FXML
    public void handleLoadButton() {
        int[] T_temp = new int[93];
        double[] Cp_temp = new double[93];
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT", "*.txt")
        );

        answer = FXCollections.observableArrayList();
        aSeries = new XYChart.Series<String, Double>();

        xAxis.setLabel("T");
        yAxis.setLabel("Cp");

        try {
            File file = fileChooser.showOpenDialog(dialogStage);
            if (file != null) {
                Scanner myReader = new Scanner(file);


                int counter = 0;
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    if (counter > 4) {

                        String[] parts = data.split(" ");
                        T_temp[counter - 5] = (int) Double.parseDouble(parts[0]);
                        Cp_temp[counter - 5] = Double.parseDouble(parts[1]);
//                    System.out.println(T_temp[counter - 5] + " " + Cp_temp[counter - 5]);
                        //aSeries.getData().add( new XYChart.Data(String.valueOf(T_temp[counter - 5]), Cp_temp[counter - 5]));
                    }
                    counter++;
                }
                myReader.close();

                int size = (T_temp[92] - T_temp[0]);

                T = new int[size];
                Cp = new double[size];

                int jCounter = 0;
                for (int i = 1; i < T_temp.length; i++) {
                    for (int j = T_temp[i - 1]; j < T_temp[i]; j++) {
                        T[jCounter] = j;
                        Cp[jCounter] = interpolate(T_temp[i - 1], Cp_temp[i - 1], T_temp[i], Cp_temp[i], j);
                        aSeries.getData().add(new XYChart.Data(String.valueOf(T[jCounter]), Cp[jCounter]));
                        jCounter++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        answer.addAll(aSeries);
        scatterChart.setData(answer);
    }

    @FXML
    public void enthalpyButton() {
        double average = 0;
        int size = T.length;
        answer.remove(aSeries);
        aSeries = new XYChart.Series<String, Double>();
        yAxis.setLabel("Ec");
        deltaH = new double[size];
        deltaH[0] = 0;
        for (int i = 1; i < size; i++) {
            average = (Cp[i] + Cp[i - 1]) / 2;
            deltaH[i] = deltaH[i - 1] + average * (T[i] - T[i - 1]);
        }

        for (int i = 0; i < size; i++) {
//            System.out.println(deltaH[i]);
//            System.out.println(T[i]);
        }

        for (int i = 0; i < size; i++) {
            aSeries.getData().add(new XYChart.Data(String.valueOf(T[i]), deltaH[i]));
        }


        answer.addAll(aSeries);

        scatterChart.setData(answer);
    }

    @FXML
    public void handle2Button() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT", "*.txt")
        );

        try {
            File file = fileChooser.showOpenDialog(dialogStage);
            if (file != null) {
                Scanner myReader = new Scanner(file);

            /*double[] T = new double[93];
            double[] Cp = new double[93];
            int counter = 0;*/
                processesQuantity = 0;
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    processesQuantity++;
                }
                myReader.close();

                Scanner myReader2 = new Scanner(file);
                processesTp = new int[processesQuantity];
                processesTk = new int[processesQuantity];
                processesEk = new double[processesQuantity];

                int counter = 0;
                while (myReader2.hasNextLine()) {
                    String data = myReader2.nextLine();

                    String[] parts = data.split(" ");
                    processesTp[counter] = Integer.parseInt(parts[0]);
                    processesTk[counter] = Integer.parseInt(parts[1]);
                    processesEk[counter] = Double.parseDouble(parts[2]);
                    textArea.appendText(data + "\n");
//                    System.out.println(data);
                    counter++;
                }
                //textArea.appendText(String.valueOf(lines));
                myReader2.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    public void enthalpyButton2() {
        double average = 0;
        int size = T.length;
        deltaH = new double[size];
        deltaH[0] = 0;
        //answer.remove(aSeries);
        yAxis.setLabel("Ec");
        aSeries = new XYChart.Series<String, Double>();

        for (int i = 1; i < (size); i++) {
            average = (Cp[i] + Cp[i - 1]) / 2;
            double ek = isProcess(T[i]);
            if (ek == -1) {
                deltaH[i] = deltaH[i - 1] + average * (T[i] - T[i - 1]);
            } else {
                deltaH[i] = deltaH[i - 1] + average * (T[i] - T[i - 1]) + ek;
            }
            aSeries.getData().add(new XYChart.Data(String.valueOf(T[i]), deltaH[i]));
        }
        for (int i = 0; i < size; i++) {
            System.out.println("temp: " + T[i] + ", delta: " + deltaH[i]);
//            System.out.println(T[i]);
        }

        enthapls.add(deltaH);
        ProcessModel[] processes = main.getProcessData().toArray(new ProcessModel[0]);
        currentProcesses.add(processes);

        answer.addAll(aSeries);

        scatterChart.setData(answer);
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new person.
     */
    @FXML
    private void handleNewBook() {
        ProcessModel tempProcess = new ProcessModel();
        boolean okClicked = main.showProcessEditDialog(tempProcess);
        if (okClicked) {
            main.getProcessData().add(tempProcess);
        }
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeleteProcess() {
        int selectedIndex = processModelTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            processModelTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(main.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No process Selected");
            alert.setContentText("Please select a process in the table.");

            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected person.
     */
    @FXML
    private void handleEditProcess() {
        ProcessModel selectedProcess = processModelTable.getSelectionModel().getSelectedItem();
        if (selectedProcess != null) {
            boolean okClicked = main.showProcessEditDialog(selectedProcess);
            if (okClicked) {
                showProcessDetails(selectedProcess);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(main.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Selection");
            alert.setContentText("Please select the transformation from the table.");

            alert.showAndWait();
        }
    }

    @FXML
    public void handleClearChart(){
        answer.clear();
        scatterChart.setData(answer);
        enthapls.clear();
    }

    @FXML
    public void handleSaveChart(){

        WritableImage image = scatterChart.snapshot(new SnapshotParameters(), null);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File file = fileChooser.showSaveDialog(dialogStage);
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image,
                        null), "png", file);

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @FXML
    public void  handleSaveEnthalpy(){



        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Zapisz obliczenia");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT", "*.txt")
        );
        File file = fileChooser.showSaveDialog(dialogStage);
        if (file != null) {
            try {
                PrintWriter writer;
                writer = new PrintWriter(file);
                for(int i = 0; i<enthapls.size();i++) {
                    ProcessModel[] pmArray = currentProcesses.get(i);
                    for (int j = 0; j < pmArray.length; j++) {
                        String pm = "Przemiana " + j + ": Tp:" + pmArray[j].getTp() + ", Tk:" + pmArray[j].getTk() + ", Ec:" + pmArray[j].getEc();
                        writer.println(pm);
                    }
                    for (int j = 0; j < enthapls.get(i).length; j++){
                         writer.println(T[j] + " " + enthapls.get(i)[j]);
                    }
                    writer.println();
                }
                writer.close();

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }


    }

    public double interpolate(double x1, double y1, double x2, double y2, double x) {
        return (x - x1) * (y2 - y1) / (x2 - x1) + y1;
    }

    public double isProcess(int temperature) {
        //przemiany z pliku
        /*for(int i=0; i<processesQuantity; i++){
            if((temperature > processesTp[i]) && (temperature < processesTk[i])){
                //sposób 1 - efekt cieplny nałożony równomienie
                return processesEk[i]/(processesTk[i] - processesTp[i]);
            }
        }
        return -1;*/


        for (int i = 0; i < main.getProcessData().size(); i++) {
            if ((temperature >= main.getProcessData().get(i).getTp() && (temperature < main.getProcessData().get(i).getTk()))) {
                ProcessType pT = main.getProcessData().get(i).getProcessType();
                double L = main.getProcessData().get(i).getEc();
                double deltaT = main.getProcessData().get(i).getTk() - main.getProcessData().get(i).getTp();
                double tempP = main.getProcessData().get(i).getTp();
                switch (pT) {
                    case NONE:
                        return -1;
                    //efekt cieplny nałożony równomienie
                    case EVEN:
                        return L / deltaT;

                    // parzyste temperatury dostają efekt cieplny, nieparzyste nie
                    case EVERY_SECOND_HAS_MORE:

                        System.out.println(deltaT);
                        if (deltaT % 2 == 0) {
                            if (temperature % 2 == 0) {
                                return L / (deltaT / 2.0);
                            } else {
                                return 0.0;
                            }
                        } else {
                            if (main.getProcessData().get(i).getTp() % 2 == 0) {
                                if (temperature % 2 == 0) {
                                    return L / ((deltaT + 1) / 2.0);
                                } else {
                                    return 0.0;
                                }
                            } else {
                                if (temperature % 2 == 0) {
                                    return L / ((deltaT - 1) / 2.0);
                                } else {
                                    return 0.0;
                                }
                            }
                        }
                    case PARABOLA:
                        double stride = 4.0/(deltaT + 1.0);
                        double sum = 0.0;
                        int counter = 0;
                        for(double k = -2; k <= 2; k+=stride){
                            sum += parabola(k);
                            counter++;
                        }
                        double[] values = new double[counter];
                        counter = 0;
                        for(double k = -2; k <= 2; k+=stride){
                            values[counter++] = parabola(k);
                        }
                        int index = (int) (temperature - tempP);
                        return (values[index]/sum)*L;
                }

            }
        }

        return -1;
    }

    public double parabola(double x){
        return - (x*x) + 4;
    }



}
