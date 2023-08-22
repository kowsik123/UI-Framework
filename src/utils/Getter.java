package utils;

import constants.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.ArrayList;

public class Getter {
    public JSONObject data;

    public static Dimension screenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }
    public static int screenWidth() {
        return Toolkit.getDefaultToolkit().getScreenSize().width;
    }

    public static int screenHeight() {
        return Toolkit.getDefaultToolkit().getScreenSize().height;
    }
    public static Color randomColor() {
        int r = (int) (Math.random()*255);
        int g = (int) (Math.random()*255);
        int b = (int) (Math.random()*255);
        return new Color(r,g,b);
    }
    public Getter(JSONObject data) {
        this.data = data;
    }
    public ArrayList<JSONObject> getChildren() {
        return (ArrayList<JSONObject>) data.getOrDefault("child", new ArrayList<JSONObject>());
    }
    public Getter getStyle(String type) {
        return new Getter((JSONObject) data.getOrDefault(type,new JSONObject()));
    }
    public Getter set(JSONObject data) {
        this.data = data;
        return this;
    }
    public String getString(String attr) {
        return (String) this.data.get(attr);
    }
    public JSONObject get(String attr) {
        return (JSONObject) this.data.get(attr);
    }
    public JSONArray getArray(String attr) {
        return (JSONArray) this.data.get(attr);
    }
    public String getName() {
        return (String) data.get("name");
    }
    public static Color hexToColor(String hex) {
        hex = hex.replace("#", "");
        switch (hex.length()) {
            case 6:
                return new Color(
                        Integer.valueOf(hex.substring(0, 2), 16),
                        Integer.valueOf(hex.substring(2, 4), 16),
                        Integer.valueOf(hex.substring(4, 6), 16));
            case 8:
                return new Color(
                        Integer.valueOf(hex.substring(0, 2), 16),
                        Integer.valueOf(hex.substring(2, 4), 16),
                        Integer.valueOf(hex.substring(4, 6), 16),
                        Integer.valueOf(hex.substring(6, 8), 16));
        }
        return null;
    }
    public Color getBackgroundColor() {
        String colors = (String) data.get("background");
        if(colors==null) return null;
        return hexToColor(colors.split(" ")[0]);
    }
    public Color getColor() {
        String color = (String) data.get("color");
        if(color==null) return null;
        return hexToColor(color);
    }
    public Color getColor(String attr) {
        String color = (String) data.getOrDefault(attr, "#EEEEEE");
        return hexToColor(color);
    }
    public Color getEndBackgroundColor() {
        String colors = (String) data.get("background");
        if(colors==null) return null;
        String[] array = colors.split(" ");
        if(array.length<2) return null;
        return hexToColor( array[1] );
    }
    public UIType getType() throws RuntimeException {
        String type = (String) data.get("type");
        switch (type) {
            case "row-cont": return UIType.ROW_CONTAINER;
            case "col-cont": return UIType.COL_CONTAINER;
            case "resize-cont": return UIType.RESIZE_CONTAINER;
            case "text": return UIType.TEXT;
            case "input": return UIType.INPUT;
            case "dropdown": return UIType.DROPDOWN;
            case "button": return UIType.BUTTON;
            case "number": return UIType.NUMBER;
            case "img": return UIType.IMAGE;
            case "floater": return UIType.FLOATER;
            case "cont": return UIType.CONTAINER;
            default: throw new RuntimeException(String.format("Unknown Type: %s", type));
        }
    }

    public Integer getPadding(String attr, String defaultValue) {
        String str = (String) data.getOrDefault(attr,defaultValue);
        if(str==null) return null;
        return Integer.parseInt( str.replace("px", "") );
    }

    public AlignmentA getAlignmentA(String attr) {
        String alignment = (String) data.get(attr);
        if(alignment==null) return null;
        if(alignment.equals("center")) return AlignmentA.COMBINED_CENTER;
        else if(alignment.equals("start")) return AlignmentA.COMBINED_START;
        else if(alignment.equals("end")) return AlignmentA.COMBINED_END;
        else if(alignment.equals("space-between")) return AlignmentA.EVENLY_SPACED;
        else if(alignment.equals("space-equally")) return AlignmentA.EQUALLY_SPACED;
        else throw new RuntimeException("unknown alignment");
    }
    public AlignmentB getAlignmentB(String attr) {
        String alignment = (String) data.get(attr);
        if(alignment==null) return null;
        if(alignment.equals("center")) return AlignmentB.CENTER;
        else if(alignment.equals("start")) return AlignmentB.START;
        else if(alignment.equals("end")) return AlignmentB.END;
        else throw new RuntimeException("unknown alignment");
    }
    public boolean is(String attr) {
        return data.containsKey(attr);
    }

    public Direction getDirection(String attr) {
        String value = (String) data.get(attr);
        if (value.equals("x")) return Direction.X;
        else if(value.equals("y")) return Direction.Y;
        else return null;
    }

    public UIImageSize getImageSize() {
        String value = (String) data.get("img-size");
        if(value.equals("fit-width")) return UIImageSize.FIT_WIDTH;
        else if(value.equals("fit-height")) return UIImageSize.FIT_HEIGHT;
        else if(value.equals("fit-auto")) return UIImageSize.FIT_AUTO;
        else if(value.equals("fit-both")) return UIImageSize.FIT_HEIGHT;
        else if(value.equals("original")) return UIImageSize.ORIGINAL;
        else throw new RuntimeException("unknown image size");
    }
}
