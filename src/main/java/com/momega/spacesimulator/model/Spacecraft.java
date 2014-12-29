package com.momega.spacesimulator.model;

import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The spacecraft class is the representation of the any artificial object.
 * The spacecraft can contain several {@link SpacecraftSubsystem}s.
 * 
 * Created by martin on 5/5/14.
 */
public class Spacecraft extends PhysicalBody implements IconProvider {

    private final static double CLOSEST_POINT_ERROR = Math.pow(10, 1);

    private List<SpacecraftSubsystem> subsystems = new ArrayList<>();
    private List<Maneuver> maneuvers = new ArrayList<>();
    private transient Maneuver currentManeuver;
    private Target target;
    private Vector3d thrust;
    private List<HistoryPoint> namedHistoryPoints = new ArrayList<>();

    public List<HistoryPoint> getNamedHistoryPoints() {
        return namedHistoryPoints;
    }

    public void setNamedHistoryPoints(List<HistoryPoint> namedHistoryPoints) {
        this.namedHistoryPoints = namedHistoryPoints;
    }

    public List<SpacecraftSubsystem> getSubsystems() {
        return subsystems;
    }

    public void setSubsystems(List<SpacecraftSubsystem> subsystems) {
        this.subsystems = subsystems;
    }

    public List<Maneuver> getManeuvers() {
        return maneuvers;
    }

    public void setManeuvers(List<Maneuver> maneuvers) {
        this.maneuvers = maneuvers;
    }

    public Maneuver getCurrentManeuver() {
        return currentManeuver;
    }

    public void setCurrentManeuver(Maneuver currentManeuver) {
        this.currentManeuver = currentManeuver;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Target getTarget() {
        return target;
    }

    public void setThrust(Vector3d thrust) {
		this.thrust = thrust;
	}
    
    public Vector3d getThrust() {
		return thrust;
	}

    public String getIcon() {
		return "/images/Number-" + index + "-icon.png";
	}

    /**
     * Computes the distances along two keplerian orbits within the given interval. The method uses following algorithm:
     * <code>
     * </code>
     * @param other the other keplerian
     * @param newTimestamp the current timestamp
     * @return new instance of the closest point
     * @see #findClosestPoint(TargetClosestPoint, MovingObject, TimeInterval)
     */
    public TargetClosestPoint findClosestPoint(MovingObject other, Timestamp newTimestamp) {
        Assert.notNull(other);

        TargetClosestPoint closestPoint = getTarget().getClosestPoint();
        if (closestPoint == null) {
            closestPoint = new TargetClosestPoint();
            closestPoint.setMovingObject(this);
            closestPoint.setTargetObject(other);
            closestPoint.setName("Closest Point to " + other.getName());
        }

        double period = getKeplerianElements().getKeplerianOrbit().getPeriod().doubleValue();
        TimeInterval interval = new DefaultTimeInterval(newTimestamp, newTimestamp.add(period));

        findClosestPoint(closestPoint, other, interval);

        return closestPoint;
    }

    /**
     * The method recursively tries to find the closes point. If the error is too high the method reduces the interval
     * als run the algorithm again
     * @param closestPoint
     * @param other the other moving body
     * @param interval the given interval to check
     * @see #solveClosestPoint(TargetClosestPoint, MovingObject, TimeInterval, java.math.BigDecimal)
     */
    private void findClosestPoint(TargetClosestPoint closestPoint, MovingObject other, TimeInterval interval) {
        BigDecimal dT = TimeUtils.getDuration(interval).divide(BigDecimal.valueOf(100));
        solveClosestPoint(closestPoint, other, interval, dT);
        if (closestPoint.getError() > CLOSEST_POINT_ERROR && dT.doubleValue()>1) {
            Timestamp start = closestPoint.getTimestamp().subtract(dT);
            Timestamp end = closestPoint.getTimestamp().add(dT);
            TimeInterval newInterval = new DefaultTimeInterval(start, end);
            findClosestPoint(closestPoint, other, newInterval);
        }
    }

    /**
     * For the given interval and step the method searches for the closes point. The method also evaluates the error
     * as a distance between two closest points.
     * @param closestPoint the instance of the closes point. The method populates the attribute of this instance
     * @param other the other moving body
     * @param interval the interval of search
     * @param dT the step of the search
     */
    private void solveClosestPoint(TargetClosestPoint closestPoint, MovingObject other, TimeInterval interval, BigDecimal dT) {
        Timestamp t = interval.getStartTime();

        // initializing the value
        double bestDistance = MathUtils.UNIVERSE_SIZE; // max possible
        KeplerianElements bestKe = getKeplerianElements();
        Vector3d bestPosition = bestKe.getCartesianPosition();
        Timestamp bestTime = t;
        double error = MathUtils.UNIVERSE_SIZE;

        while(!t.after(interval.getEndTime())) {
            KeplerianElements ke = KeplerianElements.fromTimestamp(getKeplerianElements().getKeplerianOrbit(), t);
            Vector3d thisPosition = ke.getCartesianPosition();
            Vector3d otherObj = KeplerianElements.fromTimestamp(other.getKeplerianElements().getKeplerianOrbit(), t).getCartesianPosition();
            double dist = thisPosition.subtract(otherObj).length();
            if (dist< bestDistance) {
                error = Math.abs(dist-bestDistance);
                bestDistance = dist;
                bestKe = ke;
                bestTime = t;
                bestPosition = thisPosition;
            }
            t = t.add(dT);
        }

        closestPoint.setDistance(bestDistance);
        closestPoint.setTimestamp(bestTime);
        closestPoint.setKeplerianElements(bestKe);
        closestPoint.setPosition(bestPosition);
        closestPoint.setVisible(true);
        closestPoint.setError(error);
    }
    
}
