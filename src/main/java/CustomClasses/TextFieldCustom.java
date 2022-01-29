package CustomClasses;

import javafx.scene.control.TextField;

public class TextFieldCustom extends TextField {
    private double timeValue;

    public TextFieldCustom() {
    }

    public TextFieldCustom(String s) {
        super(s);
    }

    /**
     * Precise time value
     * @return precise time value held by a particular TextView
     */
    public double getTimeValue() {
        return timeValue;
    }

    /**
     * Precise time value. For lengthTextView call only once per file, when it is initialized.
     * @param timeValue
     */
    public void setTimeValue(double timeValue) {
        this.timeValue = timeValue;
    }
}
