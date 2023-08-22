package utils;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Draw {
    public static void linearGradient(Graphics g, Color startColor, Color endColor, int start, int end) {
        int mr = Math.max(startColor.getRed(), endColor.getRed()),
                mg = Math.max(startColor.getGreen(), endColor.getGreen()),
                mb = Math.max(startColor.getBlue(), endColor.getBlue()),
                sr = Math.min(startColor.getRed(), endColor.getRed()),
                sg = Math.min(startColor.getGreen(), endColor.getGreen()),
                sb = Math.min(startColor.getBlue(), endColor.getBlue());
        for (int i = start; i <= end; i++) {
            float percent = ((float) i) / (end+1);
            int cr = (int) ((mr - sr) * percent),
                    cg = (int) ((mg - sg) * percent),
                    cb = (int) ((mb - sb) * percent);
            g.setColor(new Color(startColor.getRed() + (startColor.getRed() < endColor.getRed() ? +cr : -cr), startColor.getGreen() + (startColor.getGreen() < endColor.getGreen() ? +cg : -cg), startColor.getBlue() + (startColor.getBlue() < endColor.getBlue() ? +cb : -cb)));
            g.drawLine(i, 0, i, g.getClipBounds().height);
        }
    }
    public static void border(Graphics graphics, Color borderColor, int borderWidth, int borderRadius) {
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(borderColor);
        Rectangle rect = g.getClipBounds();
        for(int i=0; i< borderWidth; i++) g.drawRoundRect( i, i, rect.width-(i*2)-1, rect.height-(i*2)-1, borderRadius, borderRadius);
    }
    public static void background(Graphics g, Color background) {
        g.setColor(background);
        g.fillRect(0,0,g.getClipBounds().width,g.getClipBounds().height);
    }
    public static void background(Graphics graphics, Color background, int borderRadius) {
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(background);
        g.fillRoundRect(0,0,g.getClipBounds().width,g.getClipBounds().height,borderRadius, borderRadius);
    }
}
