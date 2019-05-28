package com.company;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.*;

public class Controller {

    // TODO code cleaning and refactoring, naming, GUI layout improvement
    // TODO put

    @FXML
    private Label seqL;

    @FXML
    private Slider seqS;

    @FXML
    private ToggleGroup rb;

    @FXML
    private Label selHhMmIndex;

    private Label hhmmx;

    @FXML
    private Label hhmmxCp;

    @FXML
    Label hhmmxStatus;

    @FXML
    private Label hhmm1, hhmm2, hhmm3, hhmm4;

    private Map<Integer, Label> hhmmLabelsByIndex;

    @FXML
    private Slider hhS;

    @FXML
    private Slider mmS;

    @FXML
    private Label outL;

    public void initialize() {
        hhmmLabelsByIndex = new HashMap<>();

        hhmmLabelsByIndex.put(0, hhmm1);
        hhmmLabelsByIndex.put(1, hhmm2);

        selHhMmIndex.setText(String.valueOf(rb.getToggles().indexOf(rb.getSelectedToggle()) + 1));
        hhmmx = hhmmLabelsByIndex.get(0);
        hhmmxCp.textProperty().bind(hhmmx.textProperty());
        hhmmxStatus.setText("disabled!");
        hhmmxStatus.setDisable(true);
        hhmmxCp.setDisable(true);

        seqS.valueProperty().addListener((obs, ov, nv) -> seqL.setText(String.format("%02d", nv.intValue())));

        rb.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {

            int selectedToggle = rb.getToggles().indexOf(rb.getSelectedToggle());

            hhmmx = hhmmLabelsByIndex.get(selectedToggle);
            hhmmxCp.textProperty().bind(hhmmx.textProperty());
            selHhMmIndex.setText(String.valueOf(selectedToggle + 1));

            double hhSDouble = Double.parseDouble(hhmmx.getText().substring(0, 2));
            hhS.setValue(hhSDouble);
            double mmSDouble = Double.parseDouble(hhmmx.getText().substring(3, 5)) + (Double.parseDouble(hhmmx.getText().substring(6, 8)) / 60.0);
            mmS.setValue(mmSDouble);
        });



            //            System.out.println(newValue);

//            hs.setValue(0.0);
//            hx.setText(oldValue ? "-" : String.format("%02d", (int)hs.getValue()));

//            ms.setValue(0.0);
//            mx.setText(oldValue ? "-" : String.format("%02d", (int)ms.getValue()));
//            updateOut();


//        hhmmS.valueProperty().addListener((obs, ov, nv) -> { if (cbEnable.isSelected()) hx.setText(String.format("%02d", nv.intValue())); updateOut(); });

        hhS.valueProperty().addListener((obs, ov, nv) -> {

//            Double hh = (nv.intValue() * 7.5) / 60;
//            Double mm = (nv.intValue() * 7.5) % 60;

//            hhmmx.setText(String.format("%02d", hh) + mm.toString());

            if (nv.intValue() == 24) {
                mmS.setDisable(true);
                mmS.setValue(0.0);
            } else mmS.setDisable(false);

            /*
            int hours = 0, minutes = 0, seconds = 0;

            int aDuration = (int)(nv.intValue() * 8 * 60 * 7.5) + ((int) (mmS.getValue() * 60));

            hours = aDuration / 3600;
            minutes = (aDuration - hours * 3600) / 60;
            seconds = (aDuration - (hours * 3600 + minutes * 60));

            hhmmx.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
             */

            hhS.setValue(nv.intValue());

            timeSelToString();
        });

        mmS.valueProperty().addListener((obs, ov, nv) -> {
            mmS.setValue(nv.doubleValue() - (nv.doubleValue() % 7.5));
            timeSelToString();
        });

    }

    private void timeSelToString() {
        int hours = 0, minutes = 0, seconds = 0;

        int aDuration = ((int) ((int) (hhS.getValue()) * 8 * 60 * 7.5)) + ((int) (mmS.getValue() * 60));

        hours = aDuration / 3600;
        minutes = (aDuration - hours * 3600) / 60;
        seconds = (aDuration - (hours * 3600 + minutes * 60));

        String out = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        hhmmx.setText(out);

        if (out.equals("00:00:00")) {
            hhmmxStatus.setText("disabled!");
            hhmmxStatus.setDisable(true);
            hhmmxCp.setDisable(true);
        } else if (out.equals("24:00:00")) {
            hhmmxStatus.setText("midnight");
            hhmmxStatus.setDisable(false);
            hhmmxCp.setDisable(false);
        } else {
            hhmmxStatus.setText("");
            hhmmxStatus.setDisable(false);
            hhmmxCp.setDisable(false);
        }

        double hh = hhS.getValue();
        double mm = mmS.getValue();

        outL.setText(Integer.toHexString((int) (((hh * 60.0) + mm) / 7.5)).toUpperCase());
    }

    private void updateOut() {
        outL.setText(timeStringToHexStringConcat(hhmm1.getText(),
                hhmm2.getText()));
    }

    public static String timeStringToHexStringConcat(String ...strings) {

        // TODO use (all conversion process here, not scattered somewhere in code above) and put this method in a Converter class of an util package
        // TODO Unit test this method (c.f. https://github.com/oldu73/LoraTimeToHex)

        // strings e.g. "1730", "1800", "1830", "1930", "2015", ...
        // midnight could be "0000" either "2400", it doesn't matter!

        // TODO get sequence number from a field (allowed values int:0..127)
        String hexString = Integer.toHexString(123).toUpperCase();    // 123 = sequence number

        // TODO unit res. (LSB) = 7min 30sec

        for (String s: strings)
            hexString = hexString.concat(s.equals("0") || s.contains("-") ? "00" :
                    String.format("%02X", (int)((((double)Integer.parseInt(s.substring(0, 2).equals("00") ? "24" :
                            s.substring(0, 2)) + (Integer.parseInt(s.substring(2,4)) / 60.0)) * 60.0)/7.5)));

        // TODO complete output string missing values with '00'

        // hexString e.g. "7B8C90949CA2..."

        return hexString;
    }

}
