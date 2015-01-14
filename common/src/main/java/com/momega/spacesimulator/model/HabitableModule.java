package com.momega.spacesimulator.model;

/**
 * Created by martin on 8/16/14.
 */
public class HabitableModule extends Payload {

    private int crewCapacity;

    public int getCrewCapacity() {
        return crewCapacity;
    }

    public void setCrewCapacity(int crewCapacity) {
        this.crewCapacity = crewCapacity;
    }
}
