package components;

import constants.AlignmentA;
import constants.AlignmentB;
import layouts.UILayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class UIText extends UIComponent {
    private final JLabel label;

    public UIText(String name, String text) {
        super(name);
        label = new JLabel(text);
        setOpaque(false);
        label.setOpaque(false);
        label.setHorizontalAlignment(JLabel.CENTER);
        add(label);
    }
    public void setText(String text) {
        label.setText(text);
    }
    @Override
    public void setColor(Color color) {
        label.setForeground(color);
    }
}
