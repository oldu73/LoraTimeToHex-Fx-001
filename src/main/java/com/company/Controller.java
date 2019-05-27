package com.company;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.*;

public class Controller {

    @FXML
    private ToggleGroup rb;

    private Label hx, mx;

    @FXML
    private Label h1, m1, h2, m2;

    private List<Label> hmLabels;
    private Map<Integer, List<Label>> hmLabelsByIndex;

    @FXML
    private CheckBox cbEnable;

    @FXML
    private Slider hs, ms;

    @FXML
    private Label outLabel;

    private void initHMLabels() {
        hmLabelsByIndex = new HashMap<>();

        hmLabels = Arrays.asList(new Label[]{h1, m1});
        hmLabelsByIndex.put(0, hmLabels);

        hmLabels = Arrays.asList(new Label[]{h2, m2});
        hmLabelsByIndex.put(1, hmLabels);

        hmLabels = hmLabelsByIndex.get(0);
        hx = hmLabels.get(0);
        mx = hmLabels.get(1);
    }

    public void initialize() {
        initHMLabels();

        rb.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
                List<Label> hmLabelsSel = hmLabelsByIndex.get(rb.getToggles().indexOf(rb.getSelectedToggle()));

                hx = hmLabelsSel.get(0);
                mx = hmLabelsSel.get(1);

                hs.setValue(hx.getText().equals("-") ? 0.0 : Double.parseDouble(hx.getText()));
                ms.setValue(mx.getText().equals("-") ? 0.0 : Double.parseDouble(mx.getText()));

               cbEnable.setSelected(!(hx.getText().equals("-") || mx.getText().equals("-")));
        });

        cbEnable.selectedProperty().addListener((observable, oldValue, newValue) -> {
            hs.setDisable(oldValue);
            ms.setDisable(oldValue);

            if (newValue && hx.getText().equals("-") && mx.getText().equals("-")) {
                hx.setText("00");
                mx.setText("00");
            } else {
                hx.setText("-");
                hs.setValue(0.0);
                mx.setText("-");
                ms.setValue(0.0);
            }

            //            System.out.println(newValue);

//            hs.setValue(0.0);
//            hx.setText(oldValue ? "-" : String.format("%02d", (int)hs.getValue()));

//            ms.setValue(0.0);
//            mx.setText(oldValue ? "-" : String.format("%02d", (int)ms.getValue()));
//            updateOut();
        });

        hs.valueProperty().addListener((obs, ov, nv) -> { if (cbEnable.isSelected()) hx.setText(String.format("%02d", nv.intValue())); updateOut(); });
        ms.valueProperty().addListener((obs, ov, nv) -> { if (cbEnable.isSelected()) mx.setText(String.format("%02d", nv.intValue())); updateOut(); });
    }

    private void updateOut() {
        outLabel.setText(timeStringToHexStringConcat(h1.getText() + m1.getText(),
                h2.getText() + m2.getText()));
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
