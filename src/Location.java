import java.util.*;


/**
 * Model a location in a two-dimensional grid city using (x, y) coordinates.
 * @author David J. Barnes and Michael Kölling
 * @author DP classes 
 * @version 2026.10.13
 */
public class Location
{
    private int x;
    private int y;

    /**
     * Model a location in the city.
     * @param x The x coordinate. Must be non-negative.
     * @param y The y coordinate. Must be non-negative.
     * @throws IllegalArgumentException If a coordinate is negative.
     */
    public Location(int x, int y)
    {
        if(x < 0) {
            throw new IllegalArgumentException(
                        "Negative x-coordinate: " + x);
        }
        else if (x > 19){
            throw new IllegalArgumentException("La coordenada X no puede ser mayor que 19");
        }
        if(y < 0) {
            throw new IllegalArgumentException(
                        "Negative y-coordinate: " + y);
        }
        else if (y > 19){
            throw new IllegalArgumentException("La coordenada Y no puede ser mayor que 19");
        }
        
        this.x = x;
        this.y = y;
    }
    
    /**
     * Generates the next location to visit in a direct line (Manhattan-style diagonal/straight move) 
     * from this location to the destination.
     * @param destination The {@link Location} we want to get to.
     * @return The next {@link Location} one step closer to the destination. Returns the destination
     * if this location is already the destination.
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
     * using the Chebyshev distance (maximum of the absolute differences of the coordinates).
     * @param destination The required destination {@link Location}.
     * @return The number of movement steps.
     */
    public int distance(Location destination)
    {
        int difX = Math.abs(this.getX() - destination.getX());
        int difY = Math.abs(this.getX() - destination.getY());
        
        return Math.max(difX, difY);
    }
    
    /**
     * Implement content equality for locations.
     * @param other The object to compare with.
     * @return {@code true} if the other object is a {@link Location} and has the same x and y coordinates, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object other)
    {
        if (other instanceof Location otherL && otherL.getX() == this.getX() && otherL.getY() == this.getY()) 
            return true;
        return false;
    }
    
    /**
     * @return A string representation of the location in the format "x-y".
     */
    @Override
    public String toString()
    {
        return x + "-" + y;
    }

    /**
     * Generates a hash code for the location.
     * It uses the top 16 bits for the y value and the bottom 16 bits for the x value
     * to ensure a unique hash code for most grid sizes.
     * @return A hashcode for the location.
     */
    @Override
    public int hashCode()
    {
        return (y << 16) + x;
    }

    /**
     * @return The x coordinate.
     */
    public int getX()
    {
        return x;
    }

    /**
     * @return The y coordinate.
     */
    public int getY()
    {
        return y;
    }
    
    /**
     * Set the current X-coordinate of the location.
     * @param x X-coordinate. Must be between 0 and 19.
     * @throws IllegalArgumentException If x is less than 0 or more than 19.
     */
    public void setX(int x){
        if (x < 0 || x > 19)
            throw new IllegalArgumentException("La coordenada X debe estar entre 0 y 19");
        this.x = x;
    }
    
    /**
     * Set the current Y-coordinate of the location.
     * @param y Y-coordinate. Must be between 0 and 19.
     * @throws IllegalArgumentException If y is less than 0 or more than 19.
     */
    public void setY(int y){
        if (y < 0 || y > 19)
            throw new IllegalArgumentException("La coordenada Y debe estar entre 0 y 19");
        this.y = y;
    }
}