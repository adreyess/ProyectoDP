import java.util.*;

/**
 * Models the operation of an electric bike (EB) company.
 * It manages a fleet of {@link ElectricBike}s and a network of
 * {@link BikeStation}s. The bikes are kept ordered by identifier and the stations
 * by identifier as well.
 *
 * @author David J. Barnes and Michael Kölling
 * @author DP classes
 * @version 2026.10.13
 */
public class EBCompany  
{
    /** The name of the company. */
    private String name;
    /** The bikes managed by the company, ordered by identifier. */
    private List<ElectricBike> subscribedBikes;
    /** The stations managed by the company, ordered by identifier. */
    private List<BikeStation> stations;
    /** The battery cost, in watt-hours, of moving one step. */
    public static final int MOVING_COST = 5;

    /**
     * Creates a company with the given name and with no bikes and no stations.
     * @param name The name of the company. Must not be null.
     * @throws NullPointerException If name is null.
     */
    public EBCompany(String name)
    {
        if (name == null)
            throw new NullPointerException("The company name cannot be null");
            
        this.name = name;
        this.subscribedBikes = new ArrayList<>();
        this.stations = new ArrayList<>();
    }

    /**
     * Adds an {@link ElectricBike} to the company's fleet and keeps the fleet
     * ordered by identifier.
     * @param bike The electric bike to add. Must not be null.
     */
    public void addElectricBike(ElectricBike bike)
    {       
        subscribedBikes.add(bike);
        
        Collections.sort(subscribedBikes, new ComparatorEBIdBike());
    }
    
    /**
     * Adds a {@link BikeStation} to the company's network and keeps the network
     * ordered by identifier.
     * @param station The bike station to add. Must not be null.
     */
    public void addBikeStation(BikeStation station)
    {       
        stations.add(station);
        
        Collections.sort(stations, new ComparatorBikeStationId());
    }
          
    /**
     * Retrieves a {@link BikeStation} by its unique ID.
     * @param id The ID of the station to find.
     * @return The {@link BikeStation} with the matching ID, or {@code null} if not found.
     */
    public BikeStation getBikeStation(String id)
    {
        Iterator<BikeStation> i = this.stations.iterator();
        
        while (i.hasNext()){
            BikeStation bs = i.next();
            if(bs.getId().equals(id))
                return bs;
        }
        
        return null;
    }
    
    /**
     * Retrieves a {@link BikeStation} by its {@link Location}.
     * @param location The {@link Location} of the station to find.
     * @return The {@link BikeStation} at the matching location, or {@code null} if not found.
     */
    public BikeStation getBikeStation(Location location)
    {
        Iterator<BikeStation> i = this.stations.iterator();
        
        while (i.hasNext()){
            BikeStation bs = i.next();
            if(bs.getLocation().equals(location))
                return bs;
        }
        
        return null;
    }
    
    /**
     * Gets a copy of the stations managed by the company, in identifier order.
     * @return A new list containing the stations. Modifying it does not affect
     * the company.
     */
    public List<BikeStation> getAllBikeStations()
    {
        return new ArrayList<>(this.stations);
    }
    
    /**
     * Gets a copy of the bikes managed by the company, in identifier order.
     * @return A new list containing the bikes. Modifying it does not affect the
     * company.
     */
    public List<ElectricBike> getAllElectricBikes()
    {
        return new ArrayList<>(this.subscribedBikes);
    }
        
    /**
     * Gets the number of stations managed by the company.
     * @return The number of {@link BikeStation}s in the network.
     */
    public int getNumberOfStations(){
        return stations.size();
    }
    
    /**
     * Gets the name of the company.
     * @return The name of the company.
     */
    public String getName(){
        return this.name;
    }
    
    /**
     * Sets the name of the company.
     * @param name The new name of the company. Must not be null.
     * @throws NullPointerException If name is null.
     */
    public void setName(String name){
        if (name == null)
            throw new NullPointerException("The company name cannot be null");
            
        this.name = name;
    }
    
    /**
     * Clears all managed bikes and stations, resetting the company to an empty state.
     */
    public void reset(){
        this.subscribedBikes.clear();
        this.stations.clear();
    }
    
}