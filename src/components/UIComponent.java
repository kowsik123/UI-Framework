package components;

import constants.Direction;
import layouts.UILayout;
import utils.Calc;
import utils.Draw;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

public class UIComponent extends JPanel {
    public static final int UNDEFINED=0;
    private final String name;
    private Color backgroundStart;
    private Color backgroundEnd;
    public String height;
    public String width;
    String x;
    String y;
    int borderRadius = 0;
    private Color borderColor;
    ArrayList<ActionListener> onMountListeners = new ArrayList<>();
    HashMap<String,Object> data = new HashMap<>();

    public UIComponent(String name) {
        this.name = name;
        setLayout(new UILayout());
        setBorder(new LineBorder(new Color(176, 176, 176, 255),0));
    }
    @Override
    public String getName() {
        return name;
    }
    public void setWidth(String width) {
        this.width = width;
        if(getParent()!=null) setSize();
    }
    public void setHeight(String height) {
        this.height = height;
        if(getParent()!=null) setSize();
    }

    public void setBackground(Color start, Color end ) {
        if(start==null || end == null) return;
        this.backgroundStart= start;
        this.backgroundEnd = end;
        repaint();
    }

    @Override
    public void setBackground(Color bg) {
        this.backgroundStart = bg;
        this.backgroundEnd = null;
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(backgroundStart==null) return;
        if(backgroundEnd!=null) {
            Draw.linearGradient(g,backgroundStart, backgroundEnd,0,getWidth()-1);
        } else {
            if(borderRadius>0) {
                Draw.background(g, backgroundStart, borderRadius);
            } else {
                Draw.background(g, backgroundStart);
            }
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        super.paintBorder(g);
        if(borderRadius>0 && backgroundEnd==null) {
            Draw.border(g, getBorderColor(),getBorderWidth(),borderRadius);
        }
    }

    public void setBorderWidth(int width) {
        setBorder(new LineBorder(((LineBorder) getBorder()).getLineColor(),width));
    }
    public void setBorderColor(Color color) {
        borderColor = color;
        if(borderRadius==0) {
            setBorder(new LineBorder(color,getBorderWidth()));
        }
    }

    public void setBorderRadius(int borderRadius) {
        if(borderRadius>0) {
            borderColor = getBorderColor();
            setBorder(new LineBorder(new Color(0,0,0,0),getBorderWidth()));
            super.setBackground(new Color(0,0,0,0));
        }
        this.borderRadius = borderRadius;
    }

    public int getBorderRadius() {
        return borderRadius;
    }
    public int getBorderWidth() {
        return ((LineBorder) getBorder()).getThickness();
    }
    public Color getBorderColor() {
        return borderColor;
    }
    public static void setBackground(ArrayList<UIComponent> arrayList, Color start, Color end) {
        for(UIComponent i:arrayList) i.setBackground(start, end);
    }

    public static void setHeight(ArrayList<UIComponent> arrayList, String height) {
        for(UIComponent i:arrayList) i.setHeight(height);
    }

    public static void setWidth(ArrayList<UIComponent> arrayList, String width) {
        for(UIComponent i:arrayList) i.setWidth(width);
    }

    public static void setBackground(ArrayList<UIComponent> arrayList, Color bg) {
        for(UIComponent i:arrayList) i.setBackground(bg);
    }

    public void setLocation(String x, String y) {
        this.x = x;
        this.y = y;
        if(getParent()!=null) setLocation();
    }
    public void setCenterLocation(String x, String y) {
        this.x = x+" - "+(getWidth()/2)+"px";
        this.y = y+" - "+(getHeight()/2)+"px";
        if(getParent()!=null) setLocation();
    }
    public void setLocation() {
        setLocation(Calc.lenToPixel(x,getParent().getWidth()), Calc.lenToPixel(y,getParent().getHeight()) );
    }
    public void setSize() {
        setSize(Calc.lenToPixel(width, getParent().getWidth()), Calc.lenToPixel(height, getParent().getHeight()));
    }
    public void setDirection(Direction direction) {
        ((UILayout) getLayout()).setDirection(direction);
    }
    public Direction getDirection() {
        return ((UILayout) getLayout()).getDirection();
    }

    public void setColor(Color color) {
        setForeground(color);
    }
    public void onClick(ActionListener listener) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.actionPerformed(null);
            }
        });
    }
    public void attr(String name, Object value) {
        data.put(name, value);
    }
    public Object attr(String name) {
        return data.get(name);
    }
    public boolean attrBool(String name) {
        return (boolean) data.getOrDefault(name,false);
    }
    public String attrString(String name) {
        return (String) data.getOrDefault(name,"");
    }
    public boolean isAttr(String name) {
        return data.containsKey(name);
    }
    public void onMount(ActionListener listener) {
        onMountListeners.add(listener);
    }
    public void removeOnMount(ActionListener listener) {
        onMountListeners.remove(listener);
    }

    public ArrayList<ActionListener> onMountListeners() {
        return onMountListeners;
    }
}