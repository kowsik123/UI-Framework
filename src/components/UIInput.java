package components;

import constants.Direction;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class UIInput extends UIComponent {
    JTextField input;
    public UIInput(String name) {
        super(name);
        setLayout(null);
        input = new JTextField();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                input.setSize(getWidth()-input.getX(), getHeight());
            }
        });
        input.setOpaque(false);
        input.setBorder(null);
        add(input);
    }

    public void setPadding(int padding) {
        input.setLocation(padding,0);
    }

    @Override
    public Direction getDirection() {
        return null;
    }

    @Override
    public void setDirection(Direction direction) {
    }
    public void onEnter(ActionListener listener) {
        input.addActionListener(listener);
    }
    public void removeOnEnter(ActionListener listener) {
        input.removeActionListener(listener);
    }
    public String getText() {
        return input.getText();
    }
}
