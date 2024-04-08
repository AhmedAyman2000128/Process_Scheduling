package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;

import java.util.*;

public class Controller implements Initializable {
    private Timeline timeline;

    private Vector<Color>color = new Vector<>();
    @FXML
    private StackedBarChart<String , Double> chart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private CategoryAxis yAxis;
    static int k = 1;
    @FXML
    private ListView<String> list1;
    private static final List<Color> COLORS = Arrays.asList(
            Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.PURPLE);

    @FXML
    void showChart(ActionEvent event) {
        XYChart.Series<String,Double> series1= new XYChart.Series<String,Double>();
        chart.getData().add(series1);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), e -> {
            // Perform actions after the delay
            XYChart.Data<String,Double>data = new XYChart.Data<String,Double>("Os",1.0);
            series1.getData().add(data);
            int x = new Random().nextInt(256);
            int y = new Random().nextInt(256);
            int z = new Random().nextInt(256);
            color.add(Color.rgb(x,y,z));
            System.out.println(String.format("-fx-bar-fill:%s;",color.get(color.size()-1)));
            series1.getData().getLast().getNode().setStyle(String.format("-fx-bar-fill:rgb(%d,%d,%d);",x,y,z));
            list1.getItems().add("P"+k);
            System.out.println(k);
            k++;
            System.out.println("Delay finished!");
            if(k==3){
                timeline.stop();
            }
        });
        // Create a Timeline and add the KeyFrame
        timeline = new Timeline(keyFrame);
        // Set the cycle count to 1 (play the timeline once)
        timeline.setCycleCount(5);
        // Start the timeline
        timeline.play();

        int i = 0;
        list1.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(null);
                } else {
                    setText(item);
                    int index = getIndex() % color.size();
                    setTextFill(color.get(index));
                }
            }
        });
        // Set a custom cell factory
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chart.setCategoryGap(100);
    }
}
