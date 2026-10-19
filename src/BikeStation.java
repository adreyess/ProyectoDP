import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Models an Electric Vehicle Charging Station.
 * A charging station contains multiple {@link Dock} units.
 * @author DP classes 
 * @version 2026.10.13
 */
public class BikeStation
{
    private String idStation;
    private Location location;
    private List<Dock> docks;

    /**
     * Constructor for objects of class BikeStation.
     * //TODO complete the JavaDoc comments
     */
    public BikeStation(String id, Location location, List<Dock> docks)
    {
        this.idStation = id;
        this.location = location;
        this.docks = new ArrayList<>(docks);
    }

    /**
     * Constructor for objects of class BikeStation.
     * //TODO complete the JavaDoc comments
     */
    public BikeStation(String id, Location location)
    {
        this.idStation = id;
        this.location = location;
        this.docks = new ArrayList<>();
    }
    
    /**
     * Retrieves the first free {@link Dock} found at the station.
     * @return A free {@link Dock}, or possibly throws an exception if none are found (depending on stream implementation details).
     * **Note: The implementation assumes at least one free dock exists if called.**
     */
    public Dock getFreeDock()
    {
         return this.docks.stream()
              .filter(Dock::isFree)
              .findFirst()
              .get();
    }

    /**
     * @return The unique identifier of the charging station.
     */
    public String getId()
    {  
        return idStation;
    }

    /**
     * Set the current location of the charging station.
     * @param location Where it is. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setId(String id)
    {
        this.idStation = id;  
    }    
    
    /**
     * Adds a new {@link Dock} to the station.
     * @param dock The new Dock unit.
     */
    public void addDock(Dock dock)
    {
        this.docks.add(dock);
        
        Collections.sort(docks, (d1, d2) -> Float.compare(d1.getChargingSpeed(), d2.getChargingSpeed()));
    }
    
    /**
     * Set the current location of the charging station.
     * @param location Where it is. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setLocation(Location location)
    {
        if (location == null)
            throw new NullPointerException("La localizacion debe ser NO nula");
            
        this.location = location;  
    }

    /**
     * @return The location of the bike station.
     */
    public Location getLocation()
    {  
        return location;
    }
    
    public List<Dock> getAllDocks(){
        return this.docks;
    }
    
    public Dock getDockById(String id){
        for (int i=0; i < docks.size(); i++){
            if (docks.get(i).getId().compareTo(id) == 0)
                return docks.get(i);
        }
        return null;
    }
    //TODO complete the rest of the code

}
