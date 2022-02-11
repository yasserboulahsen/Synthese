package com.example.linecharttest;

import ESP.EspData;
import ESP.EspSerialPort;
import ESP.xyGraph;
import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class Controller {
    @FXML
    private Label batteryLabel;
    @FXML
    private ProgressBar battery;
    @FXML
    private Label yValue2;
    @FXML
    private Label xValue2;
    @FXML
    private Button curseur;
    @FXML
    private Label xValue;
    @FXML
    private Button connect;
    @FXML
    private Label yValue;
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox vbox;
    @FXML
    private Button autosize;

    @FXML
    private Button start;

    private boolean sizeAuto = false;

    private final NumberAxis xaxis = new NumberAxis();
    private final NumberAxis yaxis = new NumberAxis(0, 12, 1);
    private final NumberAxis xaxis1 = new NumberAxis();
    private final NumberAxis yaxis1 = new NumberAxis(0, 100, 1);
    //    private LineChart<Number,Number> newchart = new LineChart<>(xaxis,yaxis);
    private final Label xvalueLabel = new Label();
    private final Label yvalueLabel = new Label();
    private final Label label2 = new Label();
    private final Label xvalue2 = new Label();

    // progressBar
    private ProgressBar progressBar = new ProgressBar();

    //    private Rectangle rec =
    private final XYChart.Series<Number, Number> forceSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> speedSeries = new XYChart.Series<>();
    private final Chart<Number, Number> forceChart = new Chart<>(xaxis, yaxis, yvalueLabel, xvalueLabel, forceSeries);
    private final Chart<Number, Number> speedChart = new Chart<>(xaxis1, yaxis1, label2, xvalue2, speedSeries);
    private xyGraph graph;
    private final SerialPort[] esp32 = {null};
    public static SerialPort closedesp32;

    private SimpleDoubleProperty batteryLevel;



    public void initialize() throws IOException, InterruptedException {

//     borderPane.setLeft(newchart);
//     chart.setAnimated(false);
//     newchart.setAnimated(false);
//     rec.setFill(Color.rgb(0,0,255,0.72));
//    borderPane.getChildren().add(chartTest);

//     xaxis.setLabel("X");
        start.setDisable(true);
        borderPane.setCenter(vbox);
        vbox.getChildren().addAll(forceChart, speedChart);

//    chartTest.showRecTangle(false);
        forceSeries.setName("Force");
        speedSeries.setName("Speed");
        forceChart.setAnimated(false);
        speedChart.setAnimated(false);
        speedChart.autoResize(true);
        forceChart.autoResize(true);
        forceChart.setCreateSymbols(true);
        speedChart.setCreateSymbols(true);
        speedChart.setId("chart1");
        speedChart.setId("chart");
        battery.progressProperty().setValue(1);

    }


    public void onStart(ActionEvent actionEvent) throws IOException, InterruptedException {
        speedChart.autoResize(true);
        forceChart.autoResize(true);


        esp32[0].openPort();

        forceSeries.getData().removeAll(forceSeries.getData());
        speedSeries.getData().removeAll(speedSeries.getData());
        forceChart.getData().clear();
        speedChart.getData().clear();
        graph = new xyGraph(esp32, speedSeries, forceSeries,progressBar);
        graph.chart(1, 120);


//        System.out.println(batteryLevel.getValue());
        forceChart.getDataSeries();
        speedChart.getDataSeries();

        battery.progressProperty().bind(progressBar.progressProperty());
        battery.styleProperty().bind(progressBar.styleProperty());



//        series1.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: blue;");
//        series1.getNode().lookup(".chart-line-symbol").setStyle("-fx-background-color: blue");


//         chartTest.getData().add(series1);
//         chart.getData().add(series);


//        label.setText("Starting");


    }

    public void showRectangle(ActionEvent actionEvent) {
        forceChart.addnewRectangle(borderPane);
        speedChart.addnewRectangle(borderPane);
//     chartTest1.showRecTangle(true);
//     chartTest.showRecTangle(true);



    }

    public void onAutoSize(ActionEvent actionEvent) {
        sizeAuto = !sizeAuto;

        speedChart.autoResize(sizeAuto);
        forceChart.autoResize(sizeAuto);


    }

    public void onStop(ActionEvent actionEvent) {
        speedChart.autoResize(false);
        forceChart.autoResize(false);
        speedChart.getLine().toFront();
        forceChart.getLine().toFront();

//        chart1.setOnMouseEntered(e -> {
//            chart1.setStyle("-fx-background-color:rgba(0, 0, 0, 0.05)");
//
//        });
//        chart1.setOnMouseExited(e -> {
//
//            chart1.setStyle(null);
//        });
//        chart.setOnMouseEntered(e -> {
//            chart.setStyle("-fx-background-color:rgba(0, 0, 0, 0.05)");
//        });
//        chart.setOnMouseExited(e -> {
//            chart.setStyle(null);
//        });


        try {
//             String command = "test";
//             esp32[0].writeBytes(command.getBytes(),command.length());
//            esp32[0].closePort();
            graph.shutDownService();
        } catch (Exception e) {
            System.out.println("not cennected");
        }
    }

    public void onConnexion(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
//        for(double i = 0.0 ;i <= 1.0; i += 0.1){
//            progress.setProgress(i);
//        }

        Runnable runnable = () -> {
            try {

                esp32[0] = EspSerialPort.getSerialPort();

                closedesp32 = esp32[0];
                System.out.println(closedesp32.isOpen());
                start.setDisable(false);

                connect.setStyle("-fx-background-color: #008000");
                connect.setDisable(true);

                //Platform.runLater(() -> progress.setProgress(1.0));
            } catch (Exception e) {
                Platform.runLater(() -> {
                    try {
                        esp32[0] = EspSerialPort.getSerialPort();
                        start.setDisable(false);
                    } catch (Exception e1) {
                        //e.printStackTrace();


                        alert.setTitle("Erreur");
                        alert.setHeaderText("Erreur de connexion");
                        String s = "La connexion a échoué verifier si le bleutooth est activer !!!";
                        alert.setContentText(s);

                        alert.show();
                    }
                });
            }

        };


        new Thread(runnable).start();
    }

    public void onCursor(ActionEvent actionEvent) {
        forceChart.crosshair();
        speedChart.crosshair();
        xValue.textProperty().bind(forceChart.getxValue().textProperty());
        yValue.textProperty().bind(forceChart.getyVlaue().textProperty());
        xValue2.textProperty().bind(speedChart.getxValue().textProperty());
        yValue2.textProperty().bind(speedChart.getyVlaue().textProperty());
    }
}