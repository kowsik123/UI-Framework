import components.*;
import constants.Direction;
import constants.UIType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.Calc;
import utils.Getter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class UIFramework {
    private final ArrayList<UIComponent> components = new ArrayList<>();
    private Class actions = null;
    private UIWindow window;
    private JSONObject globalStyles;

    private void addStyle(UIComponent component, Getter styleData, Getter additionalStyle) {
        if(additionalStyle!=null) styleData.data.putAll(additionalStyle.data);
        if(styleData.is("width")) component.setWidth( styleData.getString("width") );
        if(styleData.is("height")) component.setHeight( styleData.getString("height") );
        if(styleData.is("color")) component.setColor( styleData.getColor() );
        if(styleData.is("border-width")) component.setBorderWidth(Calc.lenToPixel(styleData.getString("border-width"),0));
        if(styleData.is("border-color")) component.setBorderColor(styleData.getColor("border-color"));
        if(styleData.is("border-radius")) component.setBorderRadius(Calc.lenToPixel(styleData.getString("border-radius"),0));
        Color startColor = styleData.getBackgroundColor();
        Color endColor = styleData.getEndBackgroundColor();
        if(endColor!=null) component.setBackground(startColor,endColor);
        else if(startColor!=null) component.setBackground(startColor);
        if(component instanceof UIRowContainer rowContainer) {
            if(styleData.is("align-x")) rowContainer.setAlignmentX( styleData.getAlignmentA("align-x") );
            if(styleData.is("align-y")) rowContainer.setAlignmentY( styleData.getAlignmentB("align-y") );
        }
        else if(component instanceof UIColContainer colContainer) {
            if(styleData.is("align-y")) colContainer.setAlignmentY( styleData.getAlignmentA("align-y") );
            if(styleData.is("align-x")) colContainer.setAlignmentX( styleData.getAlignmentB("align-x") );
        }
        else if(component instanceof UIContainer container) {
            if(styleData.is("direction")) container.setDirection( styleData.getDirection("direction") );
        }
        else if(component instanceof UIInput input) {
            if(styleData.is("padding")) input.setPadding(styleData.getPadding("padding", "2px"));
        } else if(component instanceof UIImage img) {
            if(styleData.is("img-size")) img.setImageSize(styleData.getImageSize());
        }
        window.repaint();
    }

    public UIComponent createComponent(String filePath) {
        try {
            JSONObject data = (JSONObject) new JSONParser().parse( new FileReader(filePath));
            return createComponent(new Getter(data));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private UIComponent createComponent(Getter componentData) {
        UIComponent uiComponent = null;
        UIType type = componentData.getType();
        String name = componentData.getName();
        Getter styleData = componentData.getStyle("style");
        if(type == UIType.ROW_CONTAINER) {
            uiComponent = new UIRowContainer(name);
        } else if(type == UIType.COL_CONTAINER) {
            uiComponent = new UIColContainer(name);
        } else if(type == UIType.RESIZE_CONTAINER) {
            uiComponent = new UIResizeContainer(name, Direction.X );
        } else if(type == UIType.INPUT) {
            uiComponent = new UIInput(name);
        } else if (type== UIType.TEXT) {
            String text = componentData.getString("text");
            if(text == null) text="";
            uiComponent = new UIText(name, text);
        } else if (type== UIType.BUTTON) {
            uiComponent = new UIButton(name, componentData.getString("text"));
        } else if(type == UIType.IMAGE) {
            uiComponent = new UIImage(name, componentData.getString("file"));
        } else if(type == UIType.FLOATER) {
            uiComponent = new UIMenu(name, window);
        } else if(type == UIType.CONTAINER) {
            uiComponent = new UIContainer(name);
        } else {
            throw new RuntimeException("unknown component type");
        }
        if(actions!=null && componentData.is("onMount")) {
            uiComponent.onMount((event)-> {
                try {
                    actions.getMethod(componentData.getString("onMount")).invoke(null);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        if(actions!=null && componentData.is("onClick")) {
            uiComponent.onClick((event)-> {
                try {
                    actions.getMethod(componentData.getString("onClick")).invoke(null);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        uiComponent.attr("hovered",false);
        uiComponent.attr("clicked",false);
        if(componentData.is("::hover") || isGlobalStyleWithName(name+"::hover")) uiComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                UIComponent component = (UIComponent) e.getComponent();
                component.attr("hovered",true);
                addStyle(component,componentData.getStyle("::hover"),getStyleForName(name+"::hover"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                UIComponent component = (UIComponent) e.getComponent();
                component.attr("hovered",false);
                if(!component.attrBool("clicked")) addStyle(component,styleData,getStyleForName(name));
            }
        });
        if(componentData.is("::click") || isGlobalStyleWithName(name+"::click")) uiComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                UIComponent component = (UIComponent) e.getComponent();
                component.attr("clicked",true);
                addStyle((UIComponent) e.getComponent(),componentData.getStyle("::click"),getStyleForName(name+"::click"));
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                UIComponent component = (UIComponent) e.getComponent();
                component.attr("clicked",false);
                addStyle((UIComponent) e.getComponent(),component.attrBool("hovered")?componentData.getStyle("::hover"):styleData,component.attrBool("hovered")?getStyleForName(name+"::hover"):getStyleForName(name));
            }
        });
        components.add(uiComponent);
        addStyle(uiComponent, styleData, getStyleForName(name));
        for (JSONObject child: componentData.getChildren() ) {
            UIComponent uiComp = createComponent( new Getter( child ) );
            if(uiComp!=null) uiComponent.add( uiComp );
        }
        return uiComponent;
    }
    private UIWindow createWindow(JSONObject data) {
        window = new UIWindow();
        Getter componentData = new Getter(data);
        UIComponent contentPane = createComponent(componentData);
        window.setContentPane( contentPane );
        window.setWidth( componentData.getString("width") );
        window.setHeight( componentData.getString("height") );
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return window;
    }
    public UIFramework(String filePath) {
        try {
            JSONObject data = (JSONObject) new JSONParser().parse( new FileReader(filePath));
            createWindow(data);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public UIFramework(String indexFile, String styleSheetPath) {
        try {
            JSONObject data = (JSONObject) new JSONParser().parse( new FileReader(indexFile));
            globalStyles = (JSONObject) new JSONParser().parse( new FileReader(styleSheetPath));
            createWindow(data);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public UIFramework(String indexFile, String styleSheetPath, Class actions) {
        try {
            this.actions = actions;
            JSONObject data = (JSONObject) new JSONParser().parse( new FileReader(indexFile));
            globalStyles = (JSONObject) new JSONParser().parse( new FileReader(styleSheetPath));
            createWindow(data);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public UIFramework(String indexFile, Class actions) {
        try {
            this.actions = actions;
            JSONObject data = (JSONObject) new JSONParser().parse( new FileReader(indexFile));
            createWindow(data);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void addStyleSheet(String filePath) {
        try {
            JSONObject data = (JSONObject) new JSONParser().parse( new FileReader(filePath));
            if(globalStyles==null) globalStyles = data;
            else globalStyles.putAll(data);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isGlobalStyleWithName(String name) {
        if (globalStyles==null) return false;
        return globalStyles.containsKey(name);
    }

    private Getter getStyleForName(String name) {
        if (globalStyles==null) return null;
        return new Getter((JSONObject) globalStyles.getOrDefault(name, new JSONObject()));
    }
    public UIComponent getComponentByName(String name) {
        for (UIComponent i:components) {
            if(i.getName().equals(name)) return i;
        }
        return null;
    }
    public ArrayList<UIComponent> getComponentsByName(String name) {
        ArrayList<UIComponent> list = new ArrayList<>();
        for (UIComponent i:components) {
            if(i.getName()==null) continue;
            if(i.getName().contains(name)) list.add(i);
        }
        return list;
    }

    public UIWindow getWindow() {
        return window;
    }

    public void setBody(UIComponent ui) {
        window.setContentPane(ui);
        window.setSize(window.getSize());
    }
    public void run() {
        window.setVisible(true);
    }
}
