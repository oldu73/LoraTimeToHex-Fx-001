package com.company;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.*;

public class Controller {

    @FXML
    private Label seqL;

    @FXML
    private Slider seqS;

    @FXML
    private ToggleGroup rb;

    private Label hhmmx;

    @FXML
    private Label hhmm1, hhmm2, hhmm3, hhmm4;

    private Map<Integer, Label> hhmmLabelsByIndex;

    @FXML
    private Slider hhmmS;

    @FXML
    private Label outL;

    public void initialize() {
        hhmmLabelsByIndex = new HashMap<>();

        hhmmLabelsByIndex.put(0, hhmm1);
        hhmmLabelsByIndex.put(1, hhmm2);

        hhmmx = hhmmLabelsByIndex.get(0);

        rb.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            hhmmx = hhmmLabelsByIndex.get(rb.getToggles().indexOf(rb.getSelectedToggle()));

//                hs.setValue(hx.getText().equals("-") ? 0.0 : Double.parseDouble(hx.getText()));
        });



            //            System.out.println(newValue);

//            hs.setValue(0.0);
//            hx.setText(oldValue ? "-" : String.format("%02d", (int)hs.getValue()));

//            ms.setValue(0.0);
//            mx.setText(oldValue ? "-" : String.format("%02d", (int)ms.getValue()));
//            updateOut();


//        hhmmS.valueProperty().addListener((obs, ov, nv) -> { if (cbEnable.isSelected()) hx.setText(String.format("%02d", nv.intValue())); updateOut(); });

        hhmmS.valueProperty().addListener((obs, ov, nv) -> {

            Double hh = (nv.intValue() * 7.5) / 60;
            Double mm = (nv.intValue() * 7.5) % 60;

            hhmmx.setText(String.format("%02d", hh) + mm.toString());
        });
    }

    private void updateOut() {
        outL.setText(timeStringToHexStringConcat(hhmm1.getText(),
                hhmm2.getText()));
    }

    public static String timeStringToHexStringConcat(String ...strings) {

        // strings e.g. "1730", "1800", "1830", "1930", "2015", ...
        // midnight could be "0000" either "2400", it doesn't matter!

        // TODO get sequence number from a field (allowed values int:0..127)
        String hexString = Integer.toHexString(123).toUpperCase();    // 123 = sequence number

        // TODO unit res. (LSB) = 7min 30sec

        for (String s: strings)
            hexString = hexString.concat(s.equals("0") || s.contains("-") ? "00" :
                    String.format("%02X", (int)((((double)Integer.parseInt(s.substring(0, 2).equals("00") ? "24" :
                            s.substring(0, 2)) + (Integer.parseInt(s.substring(2,4)) / 60.0)) * 60.0)/7.5)));

        // hexString e.g. "7B8C90949CA2..."

        return hexString;

    }

}
