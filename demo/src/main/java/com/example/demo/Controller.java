package com.example.demo;

import com.example.demo.Algorithms.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.Pair;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    @FXML
    private Button simulateBtn;
    @FXML
    private Button addBtn;
    @FXML
    private TextField arrivalText;
    @FXML
    private Button resetBtn;
    @FXML
    private TextField cpuBurstText;
    @FXML
    private TextField priorityText;
    @FXML
    private TextField quantumText;
    @FXML
    private Button removeBtn;
    enum schedularAlgorithm {
        FCFS,
        SJF_Preemptive,
        SJF_NonPreemptive,
        Priority_Preemptive,
        Priority_NonPreemptive,
        Round_Robin
    }
    enum mode{
        Static,
        Live
    }
    @FXML
    private ChoiceBox<String> schedular;
    @FXML
    private ChoiceBox<String> modeBox;
    private final String SCHEDULAR_DEFAULT = "Algorithm";
    private final String MODE_DEFAULT = "Mode";
    @FXML
    private TableColumn<Process, Integer> arrivalCol;
    @FXML
    private TableColumn<Process, Integer> remCol;
    @FXML
    private TableColumn<Process, Color> colorCol;
    @FXML
    private TableColumn<Process, Integer> priorityCol;
    private List<Color> colors;
    private ObservableList<Process> processes;
    private Timeline timeline;
    @FXML
    private StackedBarChart<String , Double> chart;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private CategoryAxis yAxis;
    @FXML
    private TableColumn<Process, Integer> cpuBurstCol;
    @FXML
    private TableColumn<Process, String> processCol;
    @FXML
    private TableView processTable;
    @FXML
    private Button stopBtn;

    @FXML
    private Label avgWaitTimeLbl;
    @FXML
    private Label avgtTurnaroundTimeLbl;
    static boolean stopState;
    static int k = 0;// for timeline
    // Indicate whether adding processes while running or at the beginning
    boolean liveFlag = false;
    private Integer timeQuantum = null;
    private static int processIndex = 0;
    private static int running_time = 0;
    private Vector<Pair<String,Integer>>readyProcesses;
    @FXML
    void showChart(ActionEvent event) {
        if(timeline!=null){
            timeline.stop();
        }
        if(processes!=null && processes.size()!=0){
            removeBtn.setDisable(true);
            simulateBtn.setDisable(true);
            ObservableList<Process>processes1 = FXCollections.observableArrayList();
            for(int i=0;i<processes.size();i++){
                processes1.add(processes.get(i).clone());
            }
            String schedulingAlgo = schedular.getValue().toString();
            if(schedulingAlgo.equals(schedularAlgorithm.FCFS.toString())){
                readyProcesses = Fcfs.getganttChart(processes1);
            }
            else if(schedulingAlgo.equals(schedularAlgorithm.Priority_NonPreemptive.toString())){
                readyProcesses = Priority_NonPreemptive.getganttChart(processes1);
            }
            else if(schedulingAlgo.equals(schedularAlgorithm.SJF_NonPreemptive.toString())){
                readyProcesses = SJF_NonPreemptive.getganttChart(processes1);
            }
            else if(schedulingAlgo.equals(schedularAlgorithm.Priority_Preemptive.toString())){
                readyProcesses = Priority_Preemptive.getganttChart(processes1);
            }
            else if(schedulingAlgo.equals(schedularAlgorithm.Round_Robin.toString())){
                readyProcesses = RoundRobin.getGanttChart(processes1,timeQuantum);
            }
            else if(schedulingAlgo.equals(schedularAlgorithm.SJF_Preemptive.toString())){
                readyProcesses = SJF_Preemptive.getGanttChart(processes1);
            }
            float averageWaiting = Process.getAverageWaitingTime(readyProcesses,processes);
            float averageTurnAround = Process.getAverageTurnAroundTime(readyProcesses,processes);
            System.out.println("Average Turnaround : "+averageTurnAround);
            System.out.println("Average Waiting Time :"+averageWaiting);
            avgWaitTimeLbl.setText(averageWaiting+"");
            avgtTurnaroundTimeLbl.setText(averageTurnAround+"");
            XYChart.Series<String, Double> series1 = new XYChart.Series<String, Double>();
            chart.getData().add(series1);
            if(modeBox.getValue().equals(mode.Live.toString())) {
                stopBtn.setDisable(false);
                liveFlag = true;
                //ersm el chart mn el 2wl
                int k = running_time;
                while (k > 0) {
                    if (k >= readyProcesses.getFirst().getValue()) {
                        k = k - readyProcesses.getFirst().getValue();
                        readyProcesses.removeFirst();
                    } else {
                        Pair pair = new Pair(readyProcesses.getFirst().getKey(), readyProcesses.getFirst().getValue() - k);
                        k = 0;
                        readyProcesses.removeFirst();
                        readyProcesses.addFirst(pair);
                    }
                }
                KeyFrame keyFrame;
                keyFrame = new KeyFrame(Duration.seconds(1), e -> {
                    running_time++;
                    Color color = null;
                    boolean me = false;//in case of remaining time =0 m3na kda enha etrsmt
                    for (int i = 0; i < processes.size(); i++) {
                        if (processes.get(i).getName().equals(readyProcesses.getFirst().getKey())) {
                            if (processes.get(i).getRemainingTime() == 0) {
                                me = true;
                                readyProcesses.removeFirst();
                                break;
                            }
                            processes.get(i).setRemainingTime(processes.get(i).getRemainingTime() - 1);
                            processTable.refresh();
                            color = processes.get(i).getColor();
                            break;
                        }
                    }
                    if (!me) {
                        XYChart.Data<String, Double> data = new XYChart.Data<String, Double>("Os", 1.0);//
                        series1.getData().add(data);
                        Pair pair = new Pair(readyProcesses.getFirst().getKey(), readyProcesses.getFirst().getValue() - 1);
                        readyProcesses.removeFirst();
                        if ((int) pair.getValue() != 0) {
                            readyProcesses.addFirst(pair);
                        }
                        if (color == null) {
                            series1.getData().getLast().getNode().setStyle("-fx-bar-fill:#cbcbcb;");
                        } else {
                            series1.getData().getLast().getNode().setStyle(String.format("-fx-bar-fill:#%h;", color));
                        }
                        if (readyProcesses.size() == 0) {
                            disableAll();
                            addBtn.setDisable(true);
                            stopBtn.setDisable(true);
                            if(timeline!=null)timeline.stop();
                        }
                    }
                });
                // Create a Timeline and add the KeyFrame
                timeline = new Timeline(keyFrame);
                // Set the cycle count to 1 (play the timeline once)
                timeline.setCycleCount(Timeline.INDEFINITE);
                // Start the timeline
                if(timeline!=null)timeline.play();
            }
            else if(modeBox.getValue().equals(mode.Static.toString())){
                disableAll();
                for(int i=0;i<readyProcesses.size();i++){
                    XYChart.Data<String, Double> data = new XYChart.Data<String, Double>("Os", (double)readyProcesses.get(i).getValue());//
                    series1.getData().add(data);
                    if(readyProcesses.get(i).getKey().equals("empty")){
                        series1.getData().getLast().getNode().setStyle("-fx-bar-fill:#cbcbcb;");
                    }
                    else {
                        for (int j = 0; j < processes.size(); j++) {
                            if (processes.get(j).getName().equals(readyProcesses.get(i).getKey())) {
                                series1.getData().getLast().getNode().setStyle(String.format("-fx-bar-fill:#%h;",processes.get(j).getColor()));
                                processes.get(j).setRemainingTime(0);
                                processTable.refresh();
                                break;
                            }
                        }
                    }
                }
                addBtn.setDisable(true);
            }
            addBtn.setDisable(true);
            disableAll();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Please insert processes first");
            alert.setTitle("No processes error");
            alert.show();
        }
    }

    @FXML
    void addProcess(ActionEvent event) {
        String schedularValue = schedular.getValue().toString();
        boolean okFlag = true;
        if (schedularValue.equals(SCHEDULAR_DEFAULT)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "please choose Scheduling Algorithm");
            alert.setTitle("Error");
            alert.show();
        }
        else if(modeBox.getValue().toString().equals(MODE_DEFAULT)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "please choose Mode");
            alert.setTitle("Error");
            alert.show();
        }
        else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "you cannot enter past Arrival Time");
            alert1.setOnCloseRequest(e -> {
                ///if(timeline!=null)timeline.play();
            });
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "please fill all the textFields");
            alert2.setTitle("Incomplete Input");
            alert2.setOnCloseRequest(e->{
                //if(timeline!=null)timeline.play();
            });
            if (schedularValue.equals(schedularAlgorithm.FCFS.toString())
                    || schedularValue.equals(schedularAlgorithm.SJF_Preemptive.toString())
                    || schedularValue.equals(schedularAlgorithm.SJF_NonPreemptive.toString())) {
                if (!isValidInputForFCFS_SJF()) {
                    okFlag = false;
                    if(timeline!=null)timeline.stop();
                    alert2.show();
                } else {
                    if (Integer.parseInt(arrivalText.getText()) < running_time) {
                        okFlag = false;
                        if(timeline!=null)timeline.stop();
                        alert1.show();
                    }
                    else {
                        processes.add(new Process("P" + (processIndex + 1), Integer.parseInt(arrivalText.getText()), Integer.parseInt(cpuBurstText.getText()), 0, colors.get(processIndex)));
                        processIndex++;
                        processTable.refresh();
                    }
                }
            } else if (schedularValue.equals(schedularAlgorithm.Priority_Preemptive.toString())
                    || schedularValue.equals(schedularAlgorithm.Priority_NonPreemptive.toString())) {
                if (!isValidInputForPriority()) {
                    okFlag = false;
                    if(timeline!=null)timeline.stop();
                    alert2.show();
                } else {
                    if (Integer.parseInt(arrivalText.getText()) < running_time) {
                        okFlag = false;
                        if(timeline!=null)timeline.stop();
                        alert1.show();
                    }
                    else {
                        processes.add(new Process("P" + (processIndex + 1), Integer.parseInt(arrivalText.getText()), Integer.parseInt(cpuBurstText.getText()), Integer.parseInt(priorityText.getText()), colors.get(processIndex)));
                        processIndex++;
                        processTable.refresh();
                    }
                }
            } else {//Round Robin
                String s = quantumText.getText();
                if (!isValidInputForRoundRobin() || s.isEmpty()) {
                    okFlag = false;
                    if(timeline!=null)timeline.stop();
                    alert2.show();
                } else {
                    if (Integer.parseInt(arrivalText.getText()) < running_time) {
                        okFlag = false;
                        if(timeline!=null)timeline.stop();
                        alert1.show();
                    }
                    else {
                        timeQuantum = Integer.parseInt(quantumText.getText());
                        quantumText.setDisable(true);
                        processes.add(new Process("P" + (processIndex + 1), Integer.parseInt(arrivalText.getText()), Integer.parseInt(cpuBurstText.getText()), 0, colors.get(processIndex)));
                        processIndex++;
                        processTable.refresh();
                    }
                }
            }
            if(okFlag) {
                if (liveFlag && Integer.parseInt(arrivalText.getText()) >= running_time) {

                }
            }
        }
        clearAllText();
    }

    @FXML
    void simulationStop(ActionEvent event) {
        if(stopState){
            stopState = false;
            Image image = new Image(getClass().getResourceAsStream("/Photo/play.png"));
            ImageView playView = new ImageView(image);
            playView.setFitHeight(50);
            playView.setPreserveRatio(true);
            stopBtn.setGraphic(playView);
            if(timeline!=null)timeline.stop();
            addBtn.setDisable(false);
            String algo = schedular.getValue().toString();
            switch (algo){
                case "FCFS":
                    enableAll();
                    priorityText.setDisable(true);
                    quantumText.setDisable(true);
                    break;
                case "SJF_Preemptive":
                    enableAll();
                    priorityText.setDisable(true);
                    quantumText.setDisable(true);
                    break;
                case "SJF_NonPreemptive":
                    enableAll();
                    priorityText.setDisable(true);
                    quantumText.setDisable(true);
                    break;
                case "Priority_Preemptive":
                    enableAll();
                    quantumText.setDisable(true);
                    break;
                case "Priority_NonPreemptive":
                    enableAll();
                    quantumText.setDisable(true);
                    break;
                case "Round_Robin":
                    enableAll();
                    priorityText.setDisable(true);
                    quantumText.setDisable(true);
                    break;
            }
        }
        else{
            stopState = true;
            Image image = new Image(getClass().getResourceAsStream("/Photo/pause.png"));
            ImageView playView = new ImageView(image);
            playView.setFitHeight(50);
            playView.setPreserveRatio(true);
            stopBtn.setGraphic(playView);
            showChart(event);
        }
    }

    @FXML
    void resetState(ActionEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Photo/pause.png"));
        ImageView playView = new ImageView(image);
        playView.setFitHeight(50);
        playView.setPreserveRatio(true);
        stopBtn.setGraphic(playView);
        if(processes!=null) processes.clear();
        schedular.setValue(SCHEDULAR_DEFAULT);
        modeBox.setValue(MODE_DEFAULT);
        schedular.setDisable(false);
        modeBox.setDisable(true);
        disableAll();
        processTable.refresh();
        if(chart!=null)chart.getData().clear();
        liveFlag = false;
        timeQuantum = null;
        processIndex = 0;
        k=0;
        clearAllText();
        if(timeline!=null)
        {
            timeline.stop();
            timeline = null;
        }
        running_time=0;
        quantumText.clear();
        addBtn.setDisable(false);
        stopBtn.setDisable(true);
        removeBtn.setDisable(false);
        avgtTurnaroundTimeLbl.setText("");
        avgWaitTimeLbl.setText("");
        stopState = true;
        simulateBtn.setDisable(true);
    }

    @FXML
    void removeProcess(ActionEvent event) {
        Process selectedItem = (Process) processTable.getSelectionModel().getSelectedItem();
        if(selectedItem == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"You have to select item from the Table !");
            alert.setTitle("Wrong Operation");
            alert.show();
        }
        else{
            processTable.getItems().remove(selectedItem);
            processes=(ObservableList<Process>)processTable.getItems();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stopState = true;
        Image image = new Image(getClass().getResourceAsStream("/Photo/pause.png"));
        ImageView view = new ImageView(image);
        view.setFitHeight(50);
        view.setPreserveRatio(true);
        stopBtn.setGraphic(view);
        image = new Image(getClass().getResourceAsStream("/Photo/orangePlus.png"));
        view = new ImageView(image);
        view.setFitHeight(40);
        view.setPreserveRatio(true);
        addBtn.setGraphic(view);
        chart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
        chart.setCategoryGap(100);
        colors = new ArrayList<>();
        int count = 30;
        double goldenRatioConjugate = 0.618033988749895;
        double saturation = 0.7;
        double brightness = 1.0;
        for (int i = 0; i < count; i++) {
            double hue = (i * goldenRatioConjugate) % 1.0;
            colors.add(Color.hsb(hue * 360, saturation, brightness));
        }
        colors = colors.reversed();
        processes = FXCollections.observableArrayList();
        //set the colors of the cells
        colorCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getColor()));
        colorCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Process, Color> call(TableColumn<Process, Color> processColorTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Color color, boolean empty) {
                        super.updateItem(color, empty);
                        if (color == null || empty) {
                            setBackground(Background.EMPTY);
                            setStyle("");
                            //setStyle("-fx-background-color: #1d3557;");
                        } else {
                            setBackground(Background.EMPTY);
                            setStyle(String.format("-fx-background-color:#%h;",color));
                        }
                    }
                };
            }
        });
        remCol.setCellValueFactory(new PropertyValueFactory<Process,Integer>("RemainingTime"));
        remCol.setStyle("-fx-alignment:center;");
        arrivalCol.setCellValueFactory(new PropertyValueFactory<Process,Integer>("Arrival"));
        arrivalCol.setStyle("-fx-alignment:center;");
        processCol.setCellValueFactory(new PropertyValueFactory<Process,String>("Name"));
        processCol.setStyle("-fx-alignment:center;");
        cpuBurstCol.setCellValueFactory(new PropertyValueFactory<Process,Integer>("CpuBurst"));
        cpuBurstCol.setStyle("-fx-alignment:center;");
        priorityCol.setCellValueFactory(new PropertyValueFactory<Process,Integer>("Priority"));
        priorityCol.setStyle("-fx-alignment:center;");
        processTable.setItems(processes);
        processTable.setBorder(Border.stroke(Color.TRANSPARENT));
        /////////////////Schedular Choice Box////////////////////////
        schedular.setValue(SCHEDULAR_DEFAULT);
        modeBox.setValue(MODE_DEFAULT);
        modeBox.setDisable(true);
        simulateBtn.setDisable(true);
        stopBtn.setDisable(true);
        disableAll();
        schedular.getItems().addAll(
                schedularAlgorithm.FCFS.toString(),
                schedularAlgorithm.SJF_Preemptive.toString(),
                schedularAlgorithm.SJF_NonPreemptive.toString(),
                schedularAlgorithm.Priority_Preemptive.toString(),
                schedularAlgorithm.Priority_NonPreemptive.toString(),
                schedularAlgorithm.Round_Robin.toString());
        modeBox.getItems().addAll(
                mode.Static.toString(),
                mode.Live.toString()
        );
        schedular.setOnAction(e->{
            modeBox.setDisable(false);
            schedular.setDisable(true);
        });
        modeBox.setOnAction(e->{
            String algo = schedular.getValue().toString();
            switch (algo){
                case "FCFS":
                    enableAll();
                    priorityText.setDisable(true);
                    quantumText.setDisable(true);
                    break;
                case "SJF_Preemptive":
                    enableAll();
                    priorityText.setDisable(true);
                    quantumText.setDisable(true);
                    break;
                case "SJF_NonPreemptive":
                    enableAll();
                    priorityText.setDisable(true);
                    quantumText.setDisable(true);
                    break;
                case "Priority_Preemptive":
                    enableAll();
                    quantumText.setDisable(true);
                    break;
                case "Priority_NonPreemptive":
                    enableAll();
                    quantumText.setDisable(true);
                    break;
                case "Round_Robin":
                    enableAll();
                    priorityText.setDisable(true);
                    break;
            }
            simulateBtn.setDisable(false);
            modeBox.setDisable(true);
        });
    }
    private void enableAll(){
        priorityText.setDisable(false);
        quantumText.setDisable(false);
        cpuBurstText.setDisable(false);
        arrivalText.setDisable(false);
    }
    private void disableAll(){
        priorityText.setDisable(true);
        quantumText.setDisable(true);
        cpuBurstText.setDisable(true);
        arrivalText.setDisable(true);
    }
    private boolean isValidInputForFCFS_SJF(){
        String  arrival= arrivalText.getText();
        String burst = cpuBurstText.getText();
        if(arrival.isEmpty() || burst.isEmpty()){
            return false;
        }
        return true;
    }
    private boolean isValidInputForPriority(){
        String priority = priorityText.getText();
        return (isValidInputForFCFS_SJF() && !priority.isEmpty());
    }
    private boolean isValidInputForRoundRobin(){
        return isValidInputForFCFS_SJF();
    }
    private void clearAllText(){
        cpuBurstText.clear();
        arrivalText.clear();
        priorityText.clear();
        //quantumText.clear();
    }
}