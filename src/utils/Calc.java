package utils;

import components.UIComponent;

public class Calc {
    private static final int MINUS = -32;
    private static final int PLUS = -52;
    private static final int NONE = -1;
    public static int lenToPixel(String len, int parentLen) {
        try {
            if (len == null) return UIComponent.UNDEFINED;

            String[] args = len.split(" ");
            if (args.length>1) {
                int total=0;
                int operator = NONE;
                for(String i:args) {
                    int pixel = lenToPixel(i,parentLen);
                    if(pixel>=0) {
                        if(operator == NONE || operator == PLUS) total += pixel;
                        else if(operator == MINUS) total -= pixel;
                        operator = NONE;
                    }
                    else operator = pixel;
                }
                return total;
            }
            else if (len.endsWith("%")) {
                float percent = Float.parseFloat(len.replace("%", "")) / 100;
                return Math.round(percent * parentLen);
            }
            else if (len.endsWith("px")) return Integer.parseInt(len.replace("px", ""));
            else if (len.equals("+")) return PLUS;
            else if (len.equals("-")) return MINUS;
            return UIComponent.UNDEFINED;
        } catch (Exception e) {
            throw new RuntimeException(String.format("invalid style value: %s", len));
        }
    }
}
