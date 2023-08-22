package components;

import constants.AlignmentA;
import constants.AlignmentB;

import java.awt.*;
import java.awt.event.ActionListener;

public class UIButton extends UIComponent {
    UIText label;
    public UIButton(String name, String text) {
        super(name);
        label = new UIText(null, text);
        label.setWidth("100%");
        label.setHeight("100%");
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(label);
        label.setOpaque(false);
        setOpaque(false);
    }

    @Override
    public void setColor(Color color) {
        label.setColor(color);
    }

    @Override
    public void setBackground(Color bg) {
        if(label==null) return;
        label.setBackground(bg);
    }

    @Override
    public void setBorderRadius(int borderRadius) {
        label.setBorderRadius(borderRadius);
    }
}
