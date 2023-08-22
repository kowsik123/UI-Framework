package components;

import constants.AlignmentA;
import constants.AlignmentB;
import constants.Direction;
import layouts.UILayout;

import java.awt.*;

public class UIRowContainer extends UIComponent {
    public UIRowContainer(String name) {
        super(name);
        setDirection(Direction.X);
        setOpaque(false);
    }
    public void setAlignmentX(AlignmentA alignmentA) {
        ((UILayout) getLayout()).setAlignmentA(alignmentA);
    }
    public void setAlignmentY(AlignmentB alignmentB) {
        ((UILayout) getLayout()).setAlignmentB(alignmentB);
    }
}
