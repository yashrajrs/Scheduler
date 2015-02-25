package scheduler;

import java.util.ArrayList;

/**
 * Object to store information regarding a lane.
 *
 * @author Yashraj R. Sontakke
 */
public class Lane {

    private Direction direction;
    private ArrayList<String> lanes = new ArrayList<String>();

    public Lane() {
        //Direction initialization using Constructor
        direction = Direction.NORTH_BOUND;

    }

    public void setDirection(final Direction direction) {
        this.direction = direction;
    }

    public void addLanes(final String lane) {
        if (this.lanes == null) {
            this.lanes = new ArrayList<String>();
        }
        this.lanes.add(lane);
    }

    public Direction getDirection() {
        return direction;
    }

    public ArrayList<String> getLanes() {
        return lanes;
    }

    public Integer getLaneSize() {
        return lanes.size();
    }

    public boolean isLaneEmpty() {
        if (lanes == null){
            return true;
        }
        return lanes.isEmpty();
    }
}
