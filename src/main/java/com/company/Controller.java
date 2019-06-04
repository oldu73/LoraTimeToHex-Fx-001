package com.company;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {

    // TODO code cleaning and refactoring, naming, GUI layout improvement
    // TODO WARNING: Resource "/main.css" not found.

    private static final String DISABLED_TEXT = "disabled!",
            DISABLED_HHMMSS = "00:00:00",
            MIDNIGHT_TEXT = "midnight",
            MIDNIGHT_HHMMSS = "24:00:00",
            EMPTY_STR = "";

    @FXML
    private Label sequenceNumberLabel,
            selectedHhMmSsRecordIndexLabel,
            hhMmSsXCopyLabel,
            hhMmSsXStatus,
            outputLabel,
            hhMmSs1, hhMmSs2, hhMmSs3, hhMmSs4;

    @FXML
    private Slider sequenceNumberSlider,
            hhSlider,
            mmSlider;

    @FXML
    private ToggleGroup hhMmSsRecordSelectToggleGroup;

    private Label hhMmSsxLabel;

    private Map<Integer, Label> hhMmSsLabelsByIndex;

    private ChangeListener<? super Number> hhSliderListener;
    private ChangeListener<? super Number> mmSliderListener;

    @FXML
    private void handleResetButtonAction(ActionEvent event) {
        hhSlider.setValue(0.0);
        mmSlider.setValue(0.0);
    }

    public void initialize() {

        hhMmSsLabelsByIndex = Stream.of(
                new AbstractMap.SimpleEntry<>(0, hhMmSs1),
                new AbstractMap.SimpleEntry<>(1, hhMmSs2))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        selectedHhMmSsRecordIndexLabel.setText(iSelTogglePlusOneStr());

        hhMmSsxLabel = hhMmSsLabelsByIndex.get(0);

        hhMmSsXCopyLabel.textProperty().bind(hhMmSsxLabel.textProperty());
        hhMmSsXCopyLabel.setDisable(true);

        hhMmSsXStatus.setText(DISABLED_TEXT);
        hhMmSsXStatus.setDisable(true);

        sequenceNumberSlider.valueProperty().addListener((obs, ov, nv) -> sequenceNumberLabel.setText(String.format("%02d", nv.intValue())));

        hhMmSsRecordSelectToggleGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            hhMmSsxLabel = hhMmSsLabelsByIndex.get(iSelToggleInt());
            hhMmSsXCopyLabel.textProperty().bind(hhMmSsxLabel.textProperty());
            selectedHhMmSsRecordIndexLabel.setText(iSelTogglePlusOneStr());

            hhSlider.valueProperty().removeListener(hhSliderListener);
            mmSlider.valueProperty().removeListener(mmSliderListener);

            hhSlider.setValue(Double.parseDouble(hhMmSsxLabel.getText().substring(0, 2)));
            mmSlider.setValue(Double.parseDouble(hhMmSsxLabel.getText().substring(3, 5)) + (Double.parseDouble(hhMmSsxLabel.getText().substring(6, 8)) / 60.0));

            updateHhMmSsXStatusAndCopyLabel();
            updateMmSlider();

            hhSlider.valueProperty().addListener(hhSliderListener);
            mmSlider.valueProperty().addListener(mmSliderListener);
        });

        hhSliderListener = (obs, ov, nv) -> {
            updateMmSlider();
            hhSlider.setValue(nv.intValue());
            timeSelToString();
        };

        hhSlider.valueProperty().addListener(hhSliderListener);

        mmSliderListener = (obs, ov, nv) -> {
            mmSlider.setValue(nv.doubleValue() - (nv.doubleValue() % 7.5));
            timeSelToString();
        };

        mmSlider.valueProperty().addListener(mmSliderListener);
    }

    private void timeSelToString() {
        int hours = 0, minutes = 0, seconds = 0;

        int aDuration = ((int) ((int) (hhSlider.getValue()) * 8 * 60 * 7.5)) + ((int) (mmSlider.getValue() * 60));

        hours = aDuration / 3600;
        minutes = (aDuration - hours * 3600) / 60;
        seconds = (aDuration - (hours * 3600 + minutes * 60));

        hhMmSsxLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));

        updateHhMmSsXStatusAndCopyLabel();

        double hh = hhSlider.getValue();
        double mm = mmSlider.getValue();

        outputLabel.setText(Integer.toHexString((int) (((hh * 60.0) + mm) / 7.5)).toUpperCase());
    }

    private void updateOut() {
        outputLabel.setText(timeStringToHexStringConcat(hhMmSs1.getText(),
                hhMmSs2.getText()));
    }

//    Dark side ;-) ####################################################################################################

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

    // TODO convert and/or refactoring of methods below to function (lambda)... for now, it's not clean :-( !!

    private int iSelToggleInt() { return hhMmSsRecordSelectToggleGroup.getToggles().indexOf(hhMmSsRecordSelectToggleGroup.getSelectedToggle()); }

    private String iSelTogglePlusOneStr() { return String.valueOf(iSelToggleInt() + 1); }

    private void hhMmSsXStatusAndCopyLabel(String text, Boolean disable) {
        hhMmSsXStatus.setText(text);
        hhMmSsXStatus.setDisable(disable);
        hhMmSsXCopyLabel.setDisable(disable);
    }

    private void updateHhMmSsXStatusAndCopyLabel() {
        String out = hhMmSsxLabel.getText();

        if (out.equals(DISABLED_HHMMSS)) hhMmSsXStatusAndCopyLabel(DISABLED_TEXT, true);
        else if (out.equals(MIDNIGHT_HHMMSS)) hhMmSsXStatusAndCopyLabel(MIDNIGHT_TEXT, false);
        else hhMmSsXStatusAndCopyLabel(EMPTY_STR, false);
    }

    private void updateMmSlider() {
        if ((int) hhSlider.getValue() == 24) {
            mmSlider.setDisable(true);
            mmSlider.setValue(0.0);
        } else mmSlider.setDisable(false);
    }

//    ##################################################################################################################

}
