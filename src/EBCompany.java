import java.util.*;

/**
 * Models the operation of an Electric Bike (EB) Company. 
 * It manages a fleet of {@link ElectricBike}s and a network of {@link BikeStation}s.
 * * @author David J. Barnes and Michael Kölling
 * @author DP classes
 * @version 2026.10.13
 */
public class EBCompany  
{
    private String name;
    private List<ElectricBike> subscribedBikes;
    private List<BikeStation> stations;
    public static final int MOVING_COST = 5;

    /**
     * Constructor for objects of class EVCompany.
     * @param name The name of the company.
     */
    public EBCompany(String name)
    {
        this.name = name;
        this.subscribedBikes = new ArrayList<>();
        this.stations = new ArrayList<>();
    }

    /**
     * Adds an {@link ElectricBike} to the company's fleet.
     * @param bike The electric bike to add.
     */
    public void addElectricBike(ElectricBike bike)
    {       
        subscribedBikes.add(bike);
        
        Collections.sort(subscribedBikes, new ComparatorEBIdBike());
    }
    
    /**
     * Adds a {@link BikeStation} to the company's network.
     * @param station The bike station to add.
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
        for (int i=0; i < stations.size(); i++){
            if (stations.get(i).getId().equals(id))
                return stations.get(i);
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
        for (int i=0; i < stations.size(); i++){
            if (stations.get(i).getLocation().equals(location))
                return stations.get(i);
        }
        return null;
    }
    
    /**
     * Retrieves a {@link BikeStation} by its {@link Location}.
     * @param location The {@link Location} of the station to find.
     * @return The {@link BikeStation} at the matching location, or {@code null} if not found.
     */
    public List<BikeStation> getAllBikeStations()
    {
        return new ArrayList<>(this.stations);
    }
    
    /**
     * Retrieves a {@link BikeStation} by its {@link Location}.
     * @param location The {@link Location} of the station to find.
     * @return The {@link BikeStation} at the matching location, or {@code null} if not found.
     */
    public List<ElectricBike> getAllElectricBikes()
    {
        return new ArrayList<>(this.subscribedBikes);
    }
        
    /**
     * @return The total number of managed {@link BikeStation}s.
     */
    public int getNumberOfStations(){
        return stations.size();
    }
    
    /**
     * @return The total number of managed {@link BikeStation}s.
     */
    public String getName(){
        return this.name;
    }
    
    /**
     * 
     */
    public void setName(String name){
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