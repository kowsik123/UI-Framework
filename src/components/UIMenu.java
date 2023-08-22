package components;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UIMenu extends UIComponent {
    JDialog menu;
    boolean isOpen=false;
    public UIMenu(String name, UIWindow window) {
        super(name);
        super.setSize(0,0);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                System.out.println("closed "+isOpen);
                if(isOpen) close();
            }
        });
        menu = new JDialog(window);
        UIComponent component = new UIComponent(null);
        component.setBorderWidth(1);
        menu.setContentPane(component);
        menu.setAlwaysOnTop(true);
        menu.setUndecorated(true);
        menu.setVisible(false);
    }

    @Override
    public void setSize(int width, int height) {
        menu.setSize(width,height);
    }
    public void open() {
        isOpen = true;
        menu.setVisible(true);
        setLocation();
    }
    public void close() {
        isOpen=false;
        menu.setVisible(false);
        menu.dispose();
    }
    public boolean isOpen(){
        return isOpen;
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x,y);
        menu.setLocation(getLocationOnScreen());
    }
}
