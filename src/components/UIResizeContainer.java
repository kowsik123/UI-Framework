package components;

import constants.Direction;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class UIResizeContainer extends UIComponent {
    private final UIComponent componentA;
    private final UIComponent componentB;
    Direction direction = Direction.X;
    UIDivider divider;
    public UIResizeContainer(String name, Direction direction) {
        super(name);
        this.direction = direction;
        setLayout(null);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                for (Component component : getComponents()) {
                    ((UIComponent) component).setSize();
                    ((UIComponent) component).setLocation();
                }
            }
        });
        componentA = new UIComponent(null);
        JButton jb = new JButton("+");
        jb.addActionListener((e)->{
            setLocation("100px");
        });
        componentA.add(jb);
        componentB = new UIComponent("Component B");
        divider = new UIDivider( direction==Direction.X? Direction.Y:Direction.X, 6 );
        divider.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
        divider.setBackground(new Color(0,0,0,55));
        componentA.setWidth("50%");
        componentA.setHeight("100%");
        componentA.setLocation(0,0);
        componentA.setBackground(new Color(96, 255, 147));
        componentB.setBackground(new Color(96, 170, 255));
        componentB.setWidth("50%");
        componentB.setHeight("100%");
        componentB.setLocation("50%","0px");
        divider.setLocation("50% - 3px","0px");
        add(divider);
        add(componentA);
        add(componentB);
    }
    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    @Override
    public Direction getDirection() {
        return direction;
    }
    public void setLocation(String len) {
        if(direction == Direction.X) {
            componentA.setWidth(len);
            componentB.setWidth("100% - "+len);
            componentB.setLocation(len, "0px");
            divider.setLocation(len+" - 3px","0px");
        }
        else {
            componentA.setHeight(len);
            componentB.setHeight("100% - "+len);
            componentB.setLocation("0px", len);
            divider.setLocation("0px",len+" - 3px");
        }
    }
}
