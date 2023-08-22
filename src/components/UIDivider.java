package components;

import constants.Direction;

public class UIDivider extends UIComponent {
    Direction direction=Direction.X;
    int length=6;

    public UIDivider(Direction direction, int len) {
        super(null);
        this.direction = direction;
        setDividerSize();
        this.length = len;
    }
    public UIDivider() {
        super(null);
        setDividerSize();
    }
    private void setDividerSize() {
        if (direction==Direction.X) {
            setWidth("100%");
            setHeight(length+"px");
        } else {
            setWidth(length+"px");
            setHeight("100%");
        }
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        setDividerSize();
    }
    public Direction getDirection() {
        return direction;
    }
}
