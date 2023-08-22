package layouts;

import components.UIComponent;
import components.UIMenu;
import constants.AlignmentA;
import constants.AlignmentB;
import constants.Direction;
import utils.Calc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

public class UILayout implements LayoutManager2 {
    private final ArrayList<Component> components = new ArrayList<>();
    AlignmentA alignmentA;
    AlignmentB alignmentB;
    Direction direction;
    Container parent = null;
    int scrollY=0;
    boolean initialRender=true;

    public UILayout() {
        this.alignmentA = AlignmentA.COMBINED_START;
        this.alignmentB = AlignmentB.START;
        this.direction = Direction.X;
    }
    @Override
    public void invalidateLayout(Container parent) {
        this.parent = parent;
        if (components.isEmpty()) return;
        setComponentsSize();
        if (direction == Direction.X) alignX();
        else if(direction == Direction.Y) alignY();
        if(initialRender && parent.isShowing()) {
            fireOnMount();
            initialRender = false;
        }
    }

    private void fireOnMount() {
        if(parent instanceof UIComponent ui) {
            for (ActionListener onMount : ui.onMountListeners()) {
                onMount.actionPerformed(null);
            }
        }
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if(comp.getSize().equals(new Dimension(0,0)) && comp.getPreferredSize()!=null ) comp.setSize(comp.getPreferredSize());
        if (comp instanceof UIMenu) return;
        components.add(comp);
        if(parent!=null && parent.isShowing()) invalidateLayout(parent);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        components.remove(comp);
    }

    public void setAlignmentA(AlignmentA alignmentA) {
        this.alignmentA = alignmentA;
        if(this.parent!=null) invalidateLayout(this.parent);
    }

    public void setAlignmentB(AlignmentB alignmentB) {
        this.alignmentB = alignmentB;
        if(this.parent!=null) invalidateLayout(this.parent);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        if(this.parent!=null) invalidateLayout(this.parent);
    }

    public Direction getDirection() {
        return direction;
    }

    private int getTotalWidth() {
        int totalWidth = 0;
        for( Component i : components ) totalWidth += i.getWidth();
        return totalWidth;
    }
    private int getTotalHeight() {
        int totalHeight = 0;
        for( Component i : components ) totalHeight += i.getHeight();
        return totalHeight;
    }
    private void setComponentsSize() {
        for( Component i : parent.getComponents() ) {
            if (i instanceof UIComponent ui) {
                ui.setSize(Calc.lenToPixel(ui.width, this.parent.getWidth()), Calc.lenToPixel(ui.height, this.parent.getHeight()));
            } else {
                i.setSize(this.parent.getWidth(), this.parent.getHeight());
            }
        }
    }

    private int getY(Component i) {
        if(alignmentB == AlignmentB.CENTER) return parent.getHeight()/2 - i.getHeight()/2;
        if(alignmentB == AlignmentB.END) return parent.getHeight() - i.getHeight();
        return 0;
    }
    private int getX(Component i) {
        if(alignmentB == AlignmentB.CENTER) return parent.getWidth()/2 - i.getWidth()/2;
        if(alignmentB == AlignmentB.END) return parent.getWidth() - i.getWidth();
        return 0;
    }
    private void alignY() {
        int y=scrollY;
        int space;
        if(alignmentA == AlignmentA.COMBINED_CENTER) {
            y += parent.getHeight()/2 - getTotalHeight()/2;
            space = 0;
        } else if(alignmentA == AlignmentA.COMBINED_END) {
            y += parent.getHeight() - getTotalHeight();
            space = 0;
        } else if(alignmentA == AlignmentA.EQUALLY_SPACED) {
            int availableSpace = parent.getHeight() - getTotalHeight();
            space = availableSpace / (components.size() * 2);
        } else if(alignmentA == AlignmentA.EVENLY_SPACED) {
            int availableSpace = parent.getHeight() - getTotalHeight();
            space = availableSpace / (components.size() -1);
        } else {
            space=0;
        }
        for ( Component i: components ) {
            if(alignmentA == AlignmentA.EQUALLY_SPACED) y+=space;
            i.setLocation(getX(i),y);
            y+=i.getHeight()+space;
        }
    }
    private void alignX() {
        int x;
        int space;
        if(alignmentA == AlignmentA.COMBINED_CENTER) {
            x = parent.getWidth()/2 - getTotalWidth()/2;
            space = 0;
        } else if(alignmentA == AlignmentA.COMBINED_END) {
            x = parent.getWidth() - getTotalWidth();
            space = 0;
        } else if(alignmentA == AlignmentA.EQUALLY_SPACED) {
            int availableSpace = parent.getWidth() - getTotalWidth();
            space = availableSpace / (components.size() * 2);
            x = 0;
        } else if(alignmentA == AlignmentA.EVENLY_SPACED) {
            int availableSpace = parent.getWidth() - getTotalWidth();
            space = availableSpace / (components.size() -1);
            x = 0;
        } else {
            x=0;
            space=0;
        }
        for ( Component i: components ) {
            if(alignmentA == AlignmentA.EQUALLY_SPACED) x+=space;
            i.setLocation(x,getY(i));
            x+=i.getWidth()+space;
        }
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {}
    @Override
    public Dimension preferredLayoutSize(Container parent) {return null;}
    @Override
    public Dimension minimumLayoutSize(Container parent) {return null;}
    @Override
    public Dimension maximumLayoutSize(Container target) {return null;}
    @Override
    public float getLayoutAlignmentX(Container target) {return 0;}
    @Override
    public float getLayoutAlignmentY(Container target) {return 0;}
    @Override
    public void layoutContainer(Container parent) {}
}
