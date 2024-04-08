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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Duration;

import java.net.URL;

import java.util.*;
import java.util.function.Predicate;

public class Controller implements Initializable {
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

    @FXML
    void showChart(ActionEvent event) {
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
            if(k==PROCESSCOUNT){
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
        processes = FXCollections.observableArrayList(
                new Process("P1",0,2,1,colors.get(0)),
                new Process("P2",0,3,1,colors.get(1)),
                new Process("P3",0,0,1,colors.get(2)),
                new Process("P4",0,5,1,colors.get(3))
        );
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
        remCol.setCellValueFactory(
                new PropertyValueFactory<Process,Integer>("RemainingTime")
        );
        arrivalCol.setCellValueFactory(
                new PropertyValueFactory<Process,Integer>("Arrival")
        );
        processCol.setCellValueFactory(
                new PropertyValueFactory<Process,String>("Name")
        );
        cpuBurstCol.setCellValueFactory(
                new PropertyValueFactory<Process,Integer>("CpuBurst")
        );
        priorityCol.setCellValueFactory(
                new PropertyValueFactory<Process,Integer>("Priority")
        );
        processTable.setItems(processes);
        //System.out.println(colors);
        schedular.getItems().addAll(schedularAlgorithm.FCFS.toString(),
                                    schedularAlgorithm.SJF_Preemptive.toString(),
                                    schedularAlgorithm.SJF_NonPreemptive.toString(),
                                    schedularAlgorithm.Priority_Preemptive.toString(),
                                    schedularAlgorithm.Priority_NonPreemptive.toString(),
                                    schedularAlgorithm.Round_Robin.toString());

    }
}
