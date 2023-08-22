package components;

import constants.AlignmentB;
import constants.UIImageSize;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class UIImage extends UIComponent {
    BufferedImage imageSource = null;
    UIImageSize size = UIImageSize.FIT_BOTH;
    AlignmentB alignmentX;
    AlignmentB alignmentY;
    public UIImage(String name, String filePath) {
        super(name);
        setImageSource(filePath);
    }
    public void setImageSource(String filePath) {
        if( filePath == null ) return;
        if(filePath.startsWith("http")) {
            try {
                URL fileUrl = new URL(filePath);
                imageSource = ImageIO.read(fileUrl);
            } catch (Exception e) {
                throw new RuntimeException("invalid url: "+filePath);
            }
        }
        else {
            File imgFile = new File(filePath);
            if (imgFile.exists()) {
                try {
                    imageSource = ImageIO.read(imgFile);
                } catch (IOException e) {
                    throw new RuntimeException("invalid url: " + filePath);
                }
            }
        }
    }
    public void setImageSize(UIImageSize sizeType) {
        if(sizeType == null) return;
        size = sizeType;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(imageSource==null) return;
        Insets insets = getInsets();
        float aspectRatio = (float) imageSource.getWidth() / imageSource.getHeight();
        int width = 0;
        int height = 0;
        int x=0;
        int y=0;
        // TODO: 7/27/23 do calculate for all position
        if( size == UIImageSize.ORIGINAL ) {
            width = imageSource.getWidth();
            height = imageSource.getHeight();
        } else if(size == UIImageSize.FIT_BOTH) {
            width = getWidth() - insets.left - insets.right;
            height = getHeight() - insets.top - insets.bottom;
        } else if(size == UIImageSize.FIT_WIDTH) {
            width = getWidth() - insets.left - insets.right;
            height = (int) (width * aspectRatio);
        } else if(size == UIImageSize.FIT_HEIGHT) {
            height = getHeight() - insets.top - insets.bottom;
            width = (int) (height * aspectRatio);
        } else if (size == UIImageSize.FIT_AUTO) {
            if(aspectRatio > 1) {
                width = getWidth() - insets.left - insets.right;
                height = (int) (width * aspectRatio);
            } else {
                height = getHeight() - insets.top - insets.bottom;
                width = (int) (height * aspectRatio);
            }
        }
        g.drawImage( imageSource.getScaledInstance(width, height, Image.SCALE_SMOOTH), insets.left,insets.top , null);
    }
    public void setAlignmentX(AlignmentB alignment) {
        alignmentX = alignment;
    }
    public void setAlignmentY(AlignmentB alignment) {
        alignmentY = alignment;
    }
}
