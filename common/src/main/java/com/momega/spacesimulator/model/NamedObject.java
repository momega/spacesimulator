package com.momega.spacesimulator.model;

/**
 * Created by martin on 6/30/14.
 */
public abstract class NamedObject {

    private String name;

    /**
     * Gets the name of the moving object
     * @return the moving object
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
