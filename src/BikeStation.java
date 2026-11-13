import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Iterator;

/**
 * Models an electric bike charging station.
 * A charging station has a unique identifier, a {@link Location} in the city and
 * a collection of {@link Dock} units where the {@link ElectricBike}s are recharged.
 * The docks are always kept ordered by charging speed (descending), then by
 * charging fee (ascending) and finally by identifier (ascending), so the first
 * free dock returned by {@link #getFreeDock()} is the fastest and cheapest one.
 *
 * @author DP classes 
 * @version 2026.10.13
 */
public class BikeStation
{
    /** The unique identifier of the station. */
    private String idStation;
    /** The location of the station in the city. */
    private Location location;
    /** The docks installed in this station, ordered by speed, fee and identifier. */
    private List<Dock> docks;

    /**
     * Creates a bike station with the given identifier and location and with no
     * docks installed yet.
     * @param id The unique identifier of the station. Must not be null.
     * @param location The location of the station in the city. Must not be null.
     * @throws NullPointerException If id or location is null.
     */
    public BikeStation(String id, Location location)
    {
        if (id == null)
            throw new NullPointerException("The station id cannot be null");
        if (location == null)
            throw new NullPointerException("The station location cannot be null");
            
        this.idStation = id;
        this.location = location;
        this.docks = new ArrayList<Dock>();
    }
    
    /**
     * Retrieves the first free {@link Dock} of the station. Because the docks are
     * kept ordered, this is the fastest and cheapest free dock available.
     * @return The first free {@link Dock}, or {@code null} if all of them are busy.
     */
    public Dock getFreeDock()
    {
         return this.docks.stream()
              .filter(Dock::isFree)
              .findFirst()
              .orElse(null);
    }

    /**
     * Gets the unique identifier of the station.
     * @return The identifier of the station.
     */
    public String getId()
    {  
        return idStation;
    }

    /**
     * Sets the unique identifier of the station.
     * @param id The new identifier of the station. Must not be null.
     * @throws NullPointerException If id is null.
     */
    public void setId(String id)
    {
        if (id == null)
            throw new NullPointerException("The station id cannot be null");
            
        this.idStation = id;  
    }    
    
    /**
     * Adds a new {@link Dock} to the station and keeps the docks ordered by
     * charging speed (descending), charging fee (ascending) and identifier
     * (ascending). The sorts are chained and rely on the stability of
     * {@link java.util.Collections#sort(java.util.List, java.util.Comparator)}
     * to preserve the previous criteria as tie-breakers.
     * @param dock The new dock to install. Must not be null.
     */
    public void addDock(Dock dock)
    {
        this.docks.add(dock);
        
        Collections.sort(docks, new ComparatorDocksId());
        Collections.sort(docks, new ComparatorDockChargingFee());
        Collections.sort(docks, new ComparatorDockChargingSpeed());
    }
    
    /**
     * Sets the location of the station in the city.
     * @param location The new location. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setLocation(Location location)
    {
        if (location == null)
            throw new NullPointerException("The station location cannot be null");
            
        this.location = location;  
    }

    /**
     * Gets the location of the station in the city.
     * @return The location of the station.
     */
    public Location getLocation()
    {  
        return location;
    }
    
    /**
     * Gets a copy of the docks installed in this station, in their current order.
     * @return A new list containing the docks of the station. Modifying it does
     * not affect the station.
     */
    public List<Dock> getAllDocks()
    {
        return new ArrayList<Dock>(this.docks);
    }
    
    /**
     * Searches for a dock of this station by its identifier.
     * @param id The identifier of the dock to find.
     * @return The {@link Dock} with that identifier, or {@code null} if the
     * station has no dock with that identifier.
     */
    public Dock getDockById(String id)
    {
        Iterator<Dock> i = this.docks.iterator();
        
        while (i.hasNext()){
            Dock d = i.next();
            if(d.getId().equals(id))
                return d;
        }
        
        return null;    
    }

    /**
     * Counts how many electric bikes have been recharged in this station,
     * adding up the bikes recharged by each of its docks.
     * @return The total number of recharges performed in the station.
     */
    public int getNumberBikesRecharged()
    {
        int total = 0;
        for (int i=0; i < this.docks.size(); i++) {
            total += this.docks.get(i).getNumberEBRecharged();
        }
        return total;
    }
    
    /**
     * Returns a string representation of the station.
     * @return The station in the format
     * {@code "BikeStation: <id>, <number of recharges>, <location>"},
     * for example {@code "BikeStation: CC01, 2, 10-11"}.
     */
    @Override
    public String toString(){
        return "BikeStation: " + idStation + ", " + getNumberBikesRecharged() + ", " + location;
    }
}
