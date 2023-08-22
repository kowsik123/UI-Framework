package components;

import constants.AlignmentA;
import constants.AlignmentB;
import constants.Direction;
import layouts.UILayout;

import java.awt.*;

public class UIColContainer extends UIComponent {
    public UIColContainer(String name) {
        super(name);
        setDirection(Direction.Y);
        setOpaque(false);
    }
    public void setAlignmentY(AlignmentA alignmentA) {
        ((UILayout) getLayout()).setAlignmentA(alignmentA);
    }
    public void setAlignmentX(AlignmentB alignmentB) {
        ((UILayout) getLayout()).setAlignmentB(alignmentB);
    }
}
