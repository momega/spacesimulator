/**
 * 
 */
package com.momega.spacesimulator.model;

/**
 * @author martin
 *
 */
public class ReferenceFrame extends NamedObject implements PositionProvider {

    private CartesianState cartesianState;
    private Timestamp timestamp;

    public CartesianState getCartesianState() {
        return cartesianState;
    }

    public void setCartesianState(CartesianState cartesianState) {
        this.cartesianState = cartesianState;
    }
    
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public Vector3d getPosition() {
        if (getCartesianState() == null) {
            return null;
        } else {
            return getCartesianState().getPosition();
        }
    }    

}
