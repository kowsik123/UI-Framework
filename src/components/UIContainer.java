package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class UIContainer extends UIComponent {
    int scrollY=0;
    UIComponent view;
    public UIContainer(String name) {
        super(name);
        view = new UIComponent("xxx");
        view.setBackground(new Color(255, 115, 115));
        view.setWidth("100%");
        view.setHeight("0px");
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(e.getUnitsToScroll()<0 && view.getY()-e.getUnitsToScroll()>0) {
                    view.setLocation(view.getX(),0);
                    return;
                }
                if(e.getUnitsToScroll()>0 && view.getY()-e.getUnitsToScroll()<getHeight()-view.getHeight()) {
                    view.setLocation(view.getX(),getHeight()-view.getHeight());
                    return;
                }
                view.setLocation(view.getX(),view.getY()-e.getUnitsToScroll());
            }
        });
        super.add(view);
    }
    public UIContainer(String name, UIComponent view) {
        super(name);
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(e.getUnitsToScroll()<0 && view.getY()-e.getUnitsToScroll()>0) {
                    view.setLocation(view.getX(),0);
                    return;
                }
                if(e.getUnitsToScroll()>0 && view.getY()-e.getUnitsToScroll()<getHeight()-view.getHeight()) {
                    view.setLocation(view.getX(),getHeight()-view.getHeight());
                    return;
                }
                view.setLocation(view.getX(),view.getY()-e.getUnitsToScroll());
            }
        });
    }

    @Override
    public Component add(Component comp) {
        return view.add(comp);
    }

    public void setViewHeight(String height) {
        this.view.setHeight(height);
    }

    public UIComponent getView() {
        return view;
    }
}
