package components;

import utils.Calc;
import utils.Getter;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UIWindow extends JFrame {
    public UIWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void setWidth(String width) {
        setSize(Calc.lenToPixel(width, Getter.screenWidth()), getHeight());
    }

    public void setHeight(String height) {
        setSize(getWidth(), Calc.lenToPixel(height, Getter.screenHeight()));
    }
}