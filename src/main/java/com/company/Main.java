package com.company;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("LoraTimeToHex-Fx-001");
        primaryStage.setScene(new Scene(root, 800, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {

//
//        String startTime = "00:00";
//        int minutes = 1440;
//        int h = minutes / 60 + Integer.parseInt(startTime.substring(0,1));
//        int m = minutes % 60 + Integer.parseInt(startTime.substring(3,4));
//        String newtime = h+":"+m;
//
//        System.out.println(newtime);


        double taktTime = 7.5;
        long timeInMilliSeconds = (long) Math.floor(taktTime * 60 * 1000);
        Date date = new Date(timeInMilliSeconds);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        System.out.println(sdf.format(date));


        int minutes = 1440;
        int h = minutes / 60;
        int m = minutes % 60;
        String newtime = h+":"+m;

        System.out.println(newtime);


        System.out.println(Duration.ofSeconds(-2L, 999999998));

        Duration dur = Duration.ofMillis(450000);

//        System.out.println(String.format("%02d:%02d:%02d", dur.get(ChronoUnit.HOURS), dur.get(ChronoUnit.MINUTES), dur.get(ChronoUnit.SECONDS)));


//        System.out.println(dur.get(ChronoUnit.MINUTES));

//        Duration d = Duration.ofSeconds(86000);


        Duration d = Duration.ofSeconds(450);


        System.out.println(d);


        String result = "";

        int hours = 0, minute = 0, seconds = 0;

        int aDuration = 86400;

        hours = aDuration / 3600;
        minutes = (aDuration - hours * 3600) / 60;
        seconds = (aDuration - (hours * 3600 + minutes * 60));

        result = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        System.out.println(result);


        launch(args);
    }
}
