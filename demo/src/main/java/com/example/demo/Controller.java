package com.example.demo;

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
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Duration;

import java.net.URL;

import java.util.*;

public class Controller implements Initializable {
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
    @FXML
    private ChoiceBox<String> schedular;
    private final String SCHEDULAR_DEFAULT = "Algorithm";
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
    static int k = 0;// for timeline
    static int PROCESSCOUNT = 4;
    // Indicate whether adding processes while running or at the beginning
    boolean liveFlag = false;
    private Integer timeQuantum = null;
    //to provide color to the process entered
    private static int processIndex = 0;

    @FXML
    void showChart(ActionEvent event) {
        if(processes != null && processes.size()!=0){
            liveFlag = true;
            XYChart.Series<String,Double> series1= new XYChart.Series<String,Double>();
            chart.getData().add(series1);
            KeyFrame keyFrame;
            keyFrame = new KeyFrame(Duration.seconds(1), e -> {
                if(processes.get(k).getRemainingTime()>0){
                    XYChart.Data<String, Double> data = new XYChart.Data<String, Double>("Os", 1.0);//
                    series1.getData().add(data);
                    series1.getData().getLast().getNode().setStyle(String.format("-fx-bar-fill:#%h;", processes.get(k).getColor()));
                    Process process = (Process) processTable.getItems().get(k);
                    process.setRemainingTime(process.getRemainingTime() - 1);
                    processTable.refresh();
                }
                if(processes.get(k).getRemainingTime() == 0)
                    k++;
                if(k==processes.size()){
                    k=0;
                    timeline.stop();
                }
            });
            // Create a Timeline and add the KeyFrame
            timeline = new Timeline(keyFrame);
            // Set the cycle count to 1 (play the timeline once)
            timeline.setCycleCount(Timeline.INDEFINITE);
            // Start the timeline
            timeline.play();
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
        if(schedularValue.equals(SCHEDULAR_DEFAULT)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"please choose Scheduling Algorithm");
            alert.setTitle("Error");
            alert.show();
        }
        else{
            if(schedularValue.equals(schedularAlgorithm.FCFS.toString())
                    || schedularValue.equals(schedularAlgorithm.SJF_Preemptive.toString())
                    || schedularValue.equals(schedularAlgorithm.SJF_NonPreemptive.toString())){
                if(!isValidInputForFCFS_SJF()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,"please fill all the textFields");
                    alert.setTitle("Incomplete Input");
                    alert.show();
                }
                else{
                    processes.add(new Process("P"+(processIndex+1),Integer.parseInt(arrivalText.getText()),Integer.parseInt(cpuBurstText.getText()),0,colors.get(processIndex)));
                    processIndex++;
                    processTable.refresh();
                }
            }
            else if(schedularValue.equals(schedularAlgorithm.Priority_Preemptive.toString())
                    ||schedularValue.equals(schedularAlgorithm.Priority_NonPreemptive.toString())){
                if(!isValidInputForPriority()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,"please fill all the textFields");
                    alert.setTitle("Incomplete Input");
                    alert.show();
                }
                else{
                    processes.add(new Process("P"+(processIndex+1),Integer.parseInt(arrivalText.getText()),Integer.parseInt(cpuBurstText.getText()),Integer.parseInt(priorityText.getText()),colors.get(processIndex)));
                    processIndex++;
                    processTable.refresh();
                }
            }
            else{//Round Robin
                String s = quantumText.getText();
                if(!isValidInputForRoundRobin() || s.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,"please fill all the textFields");
                    alert.setTitle("Incomplete Input");
                    alert.show();
                }
                else{
                    timeQuantum = Integer.parseInt(quantumText.getText());
                    quantumText.setDisable(true);
                    processes.add(new Process("P"+(processIndex+1),Integer.parseInt(arrivalText.getText()),Integer.parseInt(cpuBurstText.getText()),0,colors.get(processIndex)));
                    processIndex++;
                    processTable.refresh();
                }
            }
        }
    }

    @FXML
    void resetState(ActionEvent event) {
        processes.clear();
        schedular.setValue(SCHEDULAR_DEFAULT);
        schedular.setDisable(false);
        disableAll();
        processTable.refresh();
        chart.getData().clear();
        liveFlag = false;
        timeQuantum = null;
        processIndex = 0;
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
            System.out.println();
            for(int i=0;i<processes.size();i++){
                System.out.println(processes.get(i).getName());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
                        } else {
                            setBackground(Background.EMPTY);
                            setStyle(String.format("-fx-background-color:#%h;",color));
                        }
                    }
                };
            }
        });
        remCol.setCellValueFactory(new PropertyValueFactory<Process,Integer>("RemainingTime"));
        arrivalCol.setCellValueFactory(new PropertyValueFactory<Process,Integer>("Arrival"));
        processCol.setCellValueFactory(new PropertyValueFactory<Process,String>("Name"));
        cpuBurstCol.setCellValueFactory(new PropertyValueFactory<Process,Integer>("CpuBurst"));
        priorityCol.setCellValueFactory(new PropertyValueFactory<Process,Integer>("Priority"));
        processTable.setItems(processes);

        /////////////////Schedular Choice Box////////////////////////
        schedular.setValue(SCHEDULAR_DEFAULT);
        disableAll();
        schedular.getItems().addAll(schedularAlgorithm.FCFS.toString(),
                                    schedularAlgorithm.SJF_Preemptive.toString(),
                                    schedularAlgorithm.SJF_NonPreemptive.toString(),
                                    schedularAlgorithm.Priority_Preemptive.toString(),
                                    schedularAlgorithm.Priority_NonPreemptive.toString(),
                                    schedularAlgorithm.Round_Robin.toString());

        schedular.setOnAction(e->{
            String algo =schedular.getValue().toString();
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
            schedular.setDisable(true);
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
}
