import java.util.*;


/**
 * Models a location in the two-dimensional grid that represents the city.
 * A location is defined by an (x, y) pair of integer coordinates, where the
 * origin {@code 0-0} is the top-left corner of the grid. The valid range for
 * both coordinates is {@code 0} to {@link EBikeDemo#MAX_X} - 1 and
 * {@code 0} to {@link EBikeDemo#MAX_Y} - 1 respectively.
 *
 * @author David J. Barnes and Michael Kölling
 * @author DP classes 
 * @version 2026.10.13
 */
public class Location
{
    /** The x coordinate (column) of this location. */
    private int x;
    /** The y coordinate (row) of this location. */
    private int y;

    /**
     * Creates a location in the city with the given coordinates.
     * @param x The x coordinate. Must be between 0 and {@link EBikeDemo#MAX_X} - 1.
     * @param y The y coordinate. Must be between 0 and {@link EBikeDemo#MAX_Y} - 1.
     * @throws IllegalArgumentException If a coordinate is negative or outside the grid.
     */
    public Location(int x, int y)
    {
        if(x < 0) {
            throw new IllegalArgumentException(
                        "Negative x-coordinate: " + x);
        }
        else if (x >= EBikeDemo.MAX_X){
            throw new IllegalArgumentException("X coordinate out of range: " + x);
        }
        if(y < 0) {
            throw new IllegalArgumentException(
                        "Negative y-coordinate: " + y);
        }
        else if (y >= EBikeDemo.MAX_Y){
            throw new IllegalArgumentException("Y coordinate out of range: " + y);
        }
        
        this.x = x;
        this.y = y;
    }
    
    /**
     * Calculates the next location on the way from this location to the destination.
     * The movement is diagonal: the returned location gets at most one unit closer
     * in each axis, so both coordinates may change at the same time.
     * @param destination The {@link Location} we want to get to. Must not be null.
     * @return A new {@link Location} one step closer to the destination. If this
     * location is already the destination, an equal location is returned.
     */
    public Location nextLocation(Location destination)
    {
        int auxX = this.getX();
        int auxY = this.getY();
        
        if (auxX > destination.getX()){
            auxX--;
        }
        else if (auxX < destination.getX()){
            auxX++;
        }
        if (auxY > destination.getY()){
            auxY--;
        }
        else if (auxY < destination.getY()){
            auxY++;
        }
        
        return new Location(auxX, auxY);
    }

    /**
     * Determines the number of movements required to get from here to the destination,
     * using the Chebyshev distance: the maximum of the absolute differences of the
     * coordinates. This matches the diagonal movement of {@link #nextLocation(Location)}.
     * @param destination The required destination {@link Location}. Must not be null.
     * @return The number of movement steps needed to reach the destination.
     */
    public int distance(Location destination)
    {
        int difX = Math.abs(this.getX() - destination.getX());
        int difY = Math.abs(this.getY() - destination.getY());
        
        return Math.max(difX, difY);
    }
    
    /**
     * Implements content equality for locations: two locations are equal when
     * they are both {@link Location} objects with the same x and y coordinates.
     * @param other The object to compare with.
     * @return {@code true} if the other object is a {@link Location} with the same
     * coordinates, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof Location)) {
            return false;
        }
        
        Location otherL = (Location) other;
        return otherL.getX() == this.getX() && otherL.getY() == this.getY();
    }
    
    /**
     * Returns a string representation of the location.
     * @return The location in the format {@code "x-y"}, for example {@code "10-5"}.
     */
    @Override
    public String toString()
    {
        return x + "-" + y;
    }

    /**
     * Generates a hash code for the location, consistent with {@link #equals(Object)}.
     * The y coordinate is stored in the top 16 bits and the x coordinate in the
     * bottom 16 bits, which gives a different hash code for each location of a grid
     * of up to 65536 x 65536 cells.
     * @return A hash code for the location.
     */
    @Override
    public int hashCode()
    {
        return (y << 16) + x;
    }

    /**
     * Gets the x coordinate of this location.
     * @return The x coordinate.
     */
    public int getX()
    {
        return x;
    }

    /**
     * Gets the y coordinate of this location.
     * @return The y coordinate.
     */
    public int getY()
    {
        return y;
    }
    
    /**
     * Sets the x coordinate of this location.
     * @param x The new x coordinate. Must be between 0 and {@link EBikeDemo#MAX_X} - 1.
     * @throws IllegalArgumentException If x is outside the valid range.
     */
    public void setX(int x){
        if (x < 0 || x >= EBikeDemo.MAX_X)
            throw new IllegalArgumentException("X coordinate out of range: " + x);
        this.x = x;
    }
    
    /**
     * Sets the y coordinate of this location.
     * @param y The new y coordinate. Must be between 0 and {@link EBikeDemo#MAX_Y} - 1.
     * @throws IllegalArgumentException If y is outside the valid range.
     */
    public void setY(int y){
        if (y < 0 || y >= EBikeDemo.MAX_Y)
            throw new IllegalArgumentException("Y coordinate out of range: " + y);
        this.y = y;
    }
}