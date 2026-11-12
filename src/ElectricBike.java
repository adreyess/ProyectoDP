import java.util.List;
import java.util.ArrayList;

/**
 * Model the common elements of an Electric bike (EB) that operates 
 * within the simulation, moving towards a target and potentially recharging.
 * * @author David J. Barnes and Michael Kölling
 * @author DP classes 
 * @version 2026.10.13
 */
public class ElectricBike 
{
    private String idBike;
    private EBCompany company;
    private Location location;
    private Location rechargingLocation;
    private Location targetLocation;
    private int idleCount;
    private int batteryCapacity;
    private int batteryLevel;
    private float wsCharged;
    private int chargesCount;
    private float chargestCost;
    private List<Dock> docks;
    private int arrivalSteps;

    /**
     * Constructor of class ElectricBike.
     * @param company The EB's operating company. Must not be null.
     * @param location The EB's starting {@link Location}. Must not be null.
     * @param targetLocation The EB's final destination {@link Location}. Must not be null.
     * @param id The identificator of the bike.
     * @param batteryCapacity The maximum capacity of the battery.
     * @throws NullPointerException If company, location, or targetLocation is null.
     */
    public ElectricBike(EBCompany company, Location location, Location targetLocation, String id, int batteryCapacity)
    {
           if (company == null || location == null || targetLocation == null){
               throw new NullPointerException("Error, la compañia, localizacion o la posicion destino no pueden ser nulas."); 
           }
           this.company = company;
           this.location = location;
           this.targetLocation = targetLocation;
           this.idBike = id;
           this.batteryCapacity = batteryCapacity;
           this.batteryLevel = batteryCapacity;
           this.rechargingLocation = null;
           this.idleCount = 0;
           this.wsCharged = 0;
           this.chargesCount = 0;
           this.chargestCost = 0;
           this.docks = new ArrayList<>();
           this.arrivalSteps = -1;
    }

    /**
     * @return The id of the electric bike.
     */
    public String getIdBike()
    {
        return this.idBike;    
    }

    /**
     * Set the current location.
     * @param location Where it is. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setIdBike(String id)
    {
        this.idBike = id;
    }
    
    /**
     * @return The id of the electric bike.
     */
    public EBCompany getCompany()
    {
        return this.company;    
    }

    /**
     * Set the current location.
     * @param location Where it is. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setCompany(EBCompany cmpny)
    {
        this.company = cmpny;
    }
    
    /**
     * Set the current location.
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
     * Get the final target location.
     * @return Where this bike is ultimately headed.
     */
    public Location getLocation()
    {
        return this.location;    
    }
    
    /**
     * Set the required final target location.
     * @param location Where to go. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setTargetLocation(Location tLocation)
    {
        if (tLocation == null)
            throw new NullPointerException("El destino debe ser NO nulo");
        this.targetLocation = tLocation;
    }
    
    /**
     * Get the final target location.
     * @return Where this bike is ultimately headed.
     */
    public Location getTargetLocation()
    {
        return this.targetLocation;    
    }

    /**
     * Get the temporary recharging location.
     * @return The {@link Location} of the next {@link BikeStation} to visit, or null if no recharge is planned.
     */
    public Location getRechargingLocation()
    {
        return this.rechargingLocation;
    }
    
    /**
     * Get the temporary recharging location.
     * @return The {@link Location} of the next {@link BikeStation} to visit, or null if no recharge is planned.
     */
    public void setRechargingLocation(Location rloc)
    {
        this.rechargingLocation = rloc;
    }

    /**
     * Set the required final target location.
     * @param location Where to go. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setIdleCount(int idleC)
    {
        this.idleCount = idleC;
    }
    
    /**
     * Get the final target location.
     * @return Where this bike is ultimately headed.
     */
    public int getIdleCount()
    {
        return this.idleCount;    
    }

    /**
     * Set the required final target location.
     * @param location Where to go. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setWsCharged(float ws)
    {
        this.wsCharged = ws;
    }
    
    /**
     * Get the final target location.
     * @return Where this bike is ultimately headed.
     */
    public float getWsCharged()
    {
        return this.wsCharged;    
    }
    
    /**
     * Set the required final target location.
     * @param location Where to go. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setChargesCount(int count)
    {
        this.chargesCount = count;
    }
    
    /**
     * Get the final target location.
     * @return Where this bike is ultimately headed.
     */
    public int getChargesCount()
    {
        return this.chargesCount;    
    }
    
    /**
     * Set the required final target location.
     * @param location Where to go. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setChargestCost(float cost)
    {
        this.chargestCost = cost;
    }
    
    /**
     * Get the final target location.
     * @return Where this bike is ultimately headed.
     */
    public float getChargestCost()
    {
        return this.chargestCost;    
    }
    
    public List<Dock> getAllDocks()
    {
        return new ArrayList<>(this.docks);
    }
    
    /**
     * Set the required final target location.
     * @param location Where to go. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setArrivalSteps(int steps)
    {
        this.arrivalSteps = steps;
    }
    
    /**
     * Get the final target location.
     * @return Where this bike is ultimately headed.
     */
    public int getArrivalSteps()
    {
        return this.arrivalSteps;    
    }
    
    /**
     * Calculates the optimal route for the bike. 
     * If there isn't enough battery to reach the target, it attempts to find an intermediate 
     * {@link BikeStation} and sets it as the {@code rechargingLocation}.
     */
    public void calculateRoute()
    {
        if(!enoughBattery(this.targetLocation))
            calculateRechargingPosition();
        else
            this.rechargingLocation = null;
    }

    /**
     * Gets a string representation of the planned route, including the recharging stop if one exists.
     * @return A string showing the route: {@code currentLocation -> [rechargingLocation ->] targetLocation}.
     */
    public String getStringRoute()
    {
        if (rechargingLocation != null)
            return this.location + " -> " + this.rechargingLocation + " -> " + this.targetLocation;
        return this.location + " -> " + this.targetLocation;
    }
    
    /**
     * Gets a string representation of the planned route, including the recharging stop if one exists.
     * @return A string showing the route: {@code currentLocation -> [rechargingLocation ->] targetLocation}.
     */
    public int getBatteryCapacity()
    {
       return this.batteryCapacity;
    }
    
    /**
     * Gets a string representation of the planned route, including the recharging stop if one exists.
     * @return A string showing the route: {@code currentLocation -> [rechargingLocation ->] targetLocation}.
     */
    public void setBatteryCapacity(int capacity)
    {
       this.batteryCapacity = capacity;
    }
    
    /**
     * Gets a string representation of the planned route, including the recharging stop if one exists.
     * @return A string showing the route: {@code currentLocation -> [rechargingLocation ->] targetLocation}.
     */
    public int getBatteryLevel()
    {
       return this.batteryLevel;
    }
    
    /**
     * Gets a string representation of the planned route, including the recharging stop if one exists.
     * @return A string showing the route: {@code currentLocation -> [rechargingLocation ->] targetLocation}.
     */
    public void setBatteryLevel(int lvl)
    {
       this.batteryLevel = lvl;
    }
    
    /**
     * Checks if the current battery level is sufficient to cover a given distance.
     * //TODO complete the JavaDoc comments
     */
    public boolean enoughBattery(Location target)
    {
        return this.batteryLevel >= this.location.distance(target) * EBCompany.MOVING_COST;
    }

    /**
     * Determines the optimal intermediate {@link BikeStation} to visit for recharging
     * if the bike cannot reach the final target directly.
     * Sets {@code rechargingLocation} to the chosen station's location.
     */
    public void calculateRechargingPosition()
    {
        Location nearest = null;
        int cheapest = EBikeDemo.MAX_X + EBikeDemo.MAX_Y; 
        List<BikeStation> bs = this.company.getAllBikeStations();
        for (int i = 0; i < this.company.getNumberOfStations(); i++){
            if (bs.get(i).getLocation().equals(this.location))
                continue;
            int d1 = this.location.distance(bs.get(i).getLocation());
            int d2 = bs.get(i).getLocation().distance(this.targetLocation);
            if (enoughBattery(bs.get(i).getLocation()) && (d1 + d2) < cheapest){
                cheapest = d1 + d2;
                nearest = bs.get(i).getLocation();
            }
        }
        
        if (nearest == null)
            this.rechargingLocation = null;
        else
            this.rechargingLocation = nearest;      
    }

    /**
     * Get the Manhattan-like distance to the final target location from the current location.
     * @return The distance to the target location.
     */
    public int distanceToTheTargetLocation()
    {
        return this.location.distance(this.targetLocation);
    }

    /**
     * Simulates the recharging process when the bike arrives at a {@code rechargingLocation}.
     * The battery is charged to full capacity, the cost is calculated, and the route is recalculated.
     * //TODO complete the JavaDoc comments
     */
    public void recharge()
    {
        BikeStation bs = this.company.getBikeStation(this.rechargingLocation);
        if(bs == null)
            return;
            
        Dock free = bs.getFreeDock();
        if(free == null)
            return;
        
        incrementChargesCost(free.recharge(this));
        incrementCharges();
        addDock(free);
        calculateRoute();
    } 

    /**
     * Increments the count of recharges performed by this bike.
     * //TODO complete the JavaDoc comments
     */
    public void incrementCharges()
    {
        this.chargesCount++;
    }

    /**
     * Adds a cost amount to the total charges cost.
     * //TODO complete the JavaDoc comments
     */
    public void incrementChargesCost(float amount)
    {
        this.chargestCost += amount;
    }   

    /**
     * Carries out a single step of the bike's actions.
     * Moves one step towards the target (recharging or final) or stays idle.
     * //TODO complete the JavaDoc comments
     */
    public void act(int step)
    {
       Location target = this.targetLocation;
       
       if (this.rechargingLocation != null)
           target = this.rechargingLocation;
           
        if (this.location.equals(target) || !enoughBattery(target)){
           this.idleCount++;
           return;
       }
       
       this.location = this.location.nextLocation(target);
       reduceBatteryLevel();
       
       if (this.location.equals(target)){
           if (rechargingLocation != null){
               recharge();
               return;
           }
           else{
               this.arrivalSteps = step;
               return;
           }
       }
    }

    /**
     * Carries out a single step of the bike's actions.
     * Moves one step towards the target (recharging or final) or stays idle.
     * //TODO complete the JavaDoc comments
     */
    public void addDock(Dock d)
    {
       this.docks.add(d);
    }
    
    /**
     * Reduces the battery level by the cost of one movement step (defined in {@link EBCompany#MOVING_COST}).
     * Ensures the battery level does not go below zero.
     */
    public void reduceBatteryLevel()
    {
       this.batteryLevel -= EBCompany.MOVING_COST;
           
       if (this.batteryLevel <= 0)
           this.batteryLevel = 0;
    }

    /**
     * Returns a detailed string representation of the electric bike.
     * @return A string containing the bike's name, plate, battery info, charge counts, costs, idle count, and route.
     */
    @Override
    public String toString()
    {
        String s = "ElectricBike: " + this.idBike + ", " + this.batteryCapacity + "wh, " + this.batteryLevel + "wh, " + this.chargesCount + ", " + this.chargestCost + "€, " + this.idleCount + ", " + this.location.toString() + ", ";
        if (this.rechargingLocation != null)
            s = s + this.rechargingLocation.toString() + ", ";
        return s + this.targetLocation.toString();
    }

    /**
     * Returns a detailed string representation of the electric bike.
     * @return A string containing the bike's name, plate, battery info, charge counts, costs, idle count, and route.
     */
    public String toStringArrival()
    {
        return "ElectricBike: " + this.idBike + " at target destination ********";
    }
    
    /**
     * Returns a detailed string representation of the electric bike.
     * @return A string containing the bike's name, plate, battery info, charge counts, costs, idle count, and route.
     */
    public String toStringFinal()
    {
        return "ElectricBike: " + this.idBike + ", " + this.batteryCapacity + "wh, " + this.batteryLevel + "wh, " + this.chargesCount + ", " + this.chargestCost + "€, " + this.idleCount + ", " + this.location.toString() + ", " + this.targetLocation.toString();
    }
    
    /**
     * Returns a detailed string representation of the electric bike.
     * @return A string containing the bike's name, plate, battery info, charge counts, costs, idle count, and route.
     */
    public String toStringRecharge(String dockId, int ws, float cost)
    {
        return "ElectricBike: " + this.idBike + " recharges: " + ws + "wh at dock: " + dockId + " with cost: " + cost + "€ ********";
    } 
}
