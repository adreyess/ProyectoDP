import java.util.List;
import java.util.ArrayList;

/**
 * Models an electric bike (EB) that operates within the simulation.
 * A bike has an identifier, an operating {@link EBCompany}, a current
 * {@link Location} and a target location. It moves one step per simulation turn,
 * consuming battery, and recharges at the {@link BikeStation}s of the company when
 * it does not have enough energy to reach its target. It keeps track of the turns
 * it has been idle, the recharges it has performed, the energy charged and the
 * money spent, and of the dock it used in each recharge.
 *
 * @author David J. Barnes and Michael Kölling
 * @author DP classes 
 * @version 2026.10.13
 */
public class ElectricBike 
{
    /** The identifier of the bike. */
    private String idBike;
    /** The company that operates this bike. */
    private EBCompany company;
    /** The current location of the bike. */
    private Location location;
    /** The location of the station where the bike is going to recharge, or null if none. */
    private Location rechargingLocation;
    /** The final destination of the bike. */
    private Location targetLocation;
    /** The number of turns in which the bike could not move. */
    private int idleCount;
    /** The maximum capacity of the battery, in watt-hours. */
    private int batteryCapacity;
    /** The current energy of the battery, in watt-hours. */
    private int batteryLevel;
    /** The total energy charged by the bike, in watt-hours. */
    private float wsCharged;
    /** The number of recharges performed by the bike. */
    private int chargesCount;
    /** The total cost of all the recharges, in euros. */
    private float chargestCost;
    /** The docks where the bike has recharged, in order of use. */
    private List<Dock> docks;
    /** The turn in which the bike reached its final destination, or -1 if it has not arrived. */
    private int arrivalSteps;

    /**
     * Constructor of class ElectricBike.
     * @param company The EB's operating company. Must not be null.
     * @param location The EB's starting {@link Location}. Must not be null.
     * @param targetLocation The EB's final destination {@link Location}. Must not be null.
     * @param id The identifier of the bike.
     * @param batteryCapacity The maximum capacity of the battery, in watt-hours.
     * @throws NullPointerException If company, location, or targetLocation is null.
     */
    public ElectricBike(EBCompany company, Location location, Location targetLocation, String id, int batteryCapacity)
    {
           if (company == null || location == null || targetLocation == null){
               throw new NullPointerException("Company, location and target location cannot be null");
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
     * Gets the identifier of the bike.
     * @return The identifier of the bike.
     */
    public String getIdBike()
    {
        return this.idBike;    
    }

    /**
     * Sets the identifier of the bike.
     * @param id The new identifier of the bike. Must not be null.
     * @throws NullPointerException If id is null.
     */
    public void setIdBike(String id)
    {
        if (id == null)
            throw new NullPointerException("The bike id cannot be null");
            
        this.idBike = id;
    }
    
    /**
     * Gets the company that operates this bike.
     * @return The company of the bike.
     */
    public EBCompany getCompany()
    {
        return this.company;    
    }

    /**
     * Sets the company that operates this bike.
     * @param cmpny The new company of the bike. Must not be null.
     * @throws NullPointerException If cmpny is null.
     */
    public void setCompany(EBCompany cmpny)
    {
        if (cmpny == null)
            throw new NullPointerException("The company cannot be null");
            
        this.company = cmpny;
    }
    
    /**
     * Sets the current location of the bike.
     * @param location The new location. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setLocation(Location location)
    {
        if (location == null)
            throw new NullPointerException("The location cannot be null");
        this.location = location;
    }
    
    /**
     * Gets the current location of the bike.
     * @return Where the bike is right now.
     */
    public Location getLocation()
    {
        return this.location;    
    }
    
    /**
     * Sets the final target location of the bike.
     * @param tLocation Where the bike has to go. Must not be null.
     * @throws NullPointerException If tLocation is null.
     */
    public void setTargetLocation(Location tLocation)
    {
        if (tLocation == null)
            throw new NullPointerException("The target location cannot be null");
        this.targetLocation = tLocation;
    }
    
    /**
     * Gets the final target location of the bike.
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
     * Sets the temporary recharging location of the bike.
     * @param rloc The location of the station to visit to recharge, or {@code null}
     * if no recharge is planned.
     */
    public void setRechargingLocation(Location rloc)
    {
        this.rechargingLocation = rloc;
    }

    /**
     * Sets the number of turns the bike has been idle.
     * @param idleC The new idle counter.
     */
    public void setIdleCount(int idleC)
    {
        this.idleCount = idleC;
    }
    
    /**
     * Gets the number of turns in which the bike could not move.
     * @return The idle counter of the bike.
     */
    public int getIdleCount()
    {
        return this.idleCount;    
    }

    /**
     * Sets the total energy charged by the bike.
     * @param ws The new amount of energy charged, in watt-hours.
     */
    public void setWsCharged(float ws)
    {
        this.wsCharged = ws;
    }
    
    /**
     * Gets the total energy charged by the bike.
     * @return The energy charged so far, in watt-hours.
     */
    public float getWsCharged()
    {
        return this.wsCharged;    
    }
    
    /**
     * Sets the number of recharges performed by the bike.
     * @param count The new number of recharges.
     */
    public void setChargesCount(int count)
    {
        this.chargesCount = count;
    }
    
    /**
     * Gets the number of recharges performed by the bike.
     * @return The number of recharges.
     */
    public int getChargesCount()
    {
        return this.chargesCount;    
    }
    
    /**
     * Sets the total cost of the recharges of the bike.
     * @param cost The new accumulated cost, in euros.
     */
    public void setChargestCost(float cost)
    {
        this.chargestCost = cost;
    }
    
    /**
     * Gets the total cost of the recharges of the bike.
     * @return The accumulated cost, in euros.
     */
    public float getChargestCost()
    {
        return this.chargestCost;    
    }
    
    /**
     * Gets a copy of the docks where this bike has recharged, in the order in
     * which they were used. The last element is the dock of the last recharge.
     * @return A new list containing the docks used by the bike. Modifying it does
     * not affect the bike.
     */
    public List<Dock> getAllDocks()
    {
        return new ArrayList<>(this.docks);
    }
    
    /**
     * Sets the turn in which the bike reached its final destination.
     * @param steps The step number, or -1 if the bike has not arrived yet.
     */
    public void setArrivalSteps(int steps)
    {
        this.arrivalSteps = steps;
    }
    
    /**
     * Gets the turn in which the bike reached its final destination.
     * @return The step number in which the bike arrived, or -1 if it has not
     * arrived yet.
     */
    public int getArrivalSteps()
    {
        return this.arrivalSteps;    
    }
    
    /**
     * Calculates the route of the bike. If the current battery is enough to reach
     * the final target, no recharge is planned. Otherwise, it looks for an
     * intermediate {@link BikeStation} to recharge at and stores its location in
     * {@code rechargingLocation}.
     * @see #enoughBattery(Location)
     * @see #calculateRechargingPosition()
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
     * Gets the maximum capacity of the battery.
     * @return The battery capacity, in watt-hours.
     */
    public int getBatteryCapacity()
    {
       return this.batteryCapacity;
    }
    
    /**
     * Sets the maximum capacity of the battery.
     * @param capacity The new battery capacity, in watt-hours.
     */
    public void setBatteryCapacity(int capacity)
    {
       this.batteryCapacity = capacity;
    }
    
    /**
     * Gets the current energy of the battery.
     * @return The battery level, in watt-hours.
     */
    public int getBatteryLevel()
    {
       return this.batteryLevel;
    }
    
    /**
     * Sets the current energy of the battery.
     * @param lvl The new battery level, in watt-hours.
     */
    public void setBatteryLevel(int lvl)
    {
       this.batteryLevel = lvl;
    }
    
    /**
     * Checks whether the current battery level is enough to travel from the
     * current location to the given one. Each movement step costs
     * {@link EBCompany#MOVING_COST} watt-hours and the distance is measured with
     * {@link Location#distance(Location)}.
     * @param target The location to reach. Must not be null.
     * @return {@code true} if the battery can cover the distance, {@code false} otherwise.
     */
    public boolean enoughBattery(Location target)
    {
        return this.batteryLevel >= this.location.distance(target) * EBCompany.MOVING_COST;
    }

    /**
     * Determines the optimal intermediate {@link BikeStation} to recharge at when
     * the bike cannot reach the final target directly, and sets
     * {@code rechargingLocation} to its location.
     * The station must be reachable with the current battery and must be different
     * from the one the bike is standing on; among the valid ones, the chosen
     * station minimises the total distance (current location to station plus
     * station to target). If there is no valid station, {@code rechargingLocation}
     * is set to {@code null}.
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
     * Gets the number of movement steps from the current location to the final
     * target location, using the Chebyshev distance.
     * @return The distance to the target location, in movement steps.
     */
    public int distanceToTheTargetLocation()
    {
        return this.location.distance(this.targetLocation);
    }

    /**
     * Simulates the recharging process when the bike arrives at its
     * {@code rechargingLocation}. It looks for the station at that location and
     * asks it for a free {@link Dock}; if either of them is missing, the method
     * does nothing. Otherwise the dock recharges the bike, the cost is added to the
     * accumulated cost, the recharge counter is incremented, the dock is recorded
     * and the route is recalculated.
     * @see Dock#recharge(ElectricBike)
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
     * Increments the number of recharges performed by this bike.
     */
    public void incrementCharges()
    {
        this.chargesCount++;
    }

    /**
     * Adds an amount to the total cost of the recharges of this bike.
     * @param amount The cost of the last recharge, in euros.
     */
    public void incrementChargesCost(float amount)
    {
        this.chargestCost += amount;
    }   

    /**
     * Carries out a single step of the bike's action.
     * The bike moves one step towards its current objective: the recharging
     * location if one is planned, or the final target otherwise. If the bike is
     * already at the objective or does not have enough battery to reach it, it
     * stays where it is and increments the idle counter. When it reaches the
     * recharging location it recharges and recalculates its route; when it reaches
     * the final target it records the arrival step.
     * @param step The number of the current simulation step, used to record the
     * arrival step.
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
     * Records a dock as one of the docks where this bike has recharged. The dock
     * is appended to the end of the list, so the last element is always the most
     * recent recharge.
     * @param d The dock to record. Must not be null.
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
     * Returns the current state of the bike as a single line, including the
     * recharging location only when a recharge is planned.
     * @return The bike in the format
     * {@code "ElectricBike: <id>, <capacity>wh, <level>wh, <recharges>, <cost>€, <idle>, <location>, [<rechargingLocation>, ]<target>"}.
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
     * Returns the message shown when the bike reaches its final destination.
     * @return The bike identifier followed by {@code "at target destination ********"}.
     */
    public String toStringArrival()
    {
        return "ElectricBike: " + this.idBike + " at target destination ********";
    }
    
    /**
     * Returns the final state of the bike, always with the same number of fields.
     * Unlike {@link #toString()}, the recharging location is never included.
     * @return The bike in the format
     * {@code "ElectricBike: <id>, <capacity>wh, <level>wh, <recharges>, <cost>€, <idle>, <location>, <target>"}.
     */
    public String toStringFinal()
    {
        return "ElectricBike: " + this.idBike + ", " + this.batteryCapacity + "wh, " + this.batteryLevel + "wh, " + this.chargesCount + ", " + this.chargestCost + "€, " + this.idleCount + ", " + this.location.toString() + ", " + this.targetLocation.toString();
    }
    
    /**
     * Returns the message shown when the bike recharges at a dock.
     * @param dockId The identifier of the dock where the bike has recharged.
     * @param ws The energy charged in that recharge, in watt-hours.
     * @param cost The cost of that recharge, in euros.
     * @return The bike identifier followed by the recharge details and
     * {@code "********"}.
     */
    public String toStringRecharge(String dockId, int ws, float cost)
    {
        return "ElectricBike: " + this.idBike + " recharges: " + ws + "wh at dock: " + dockId + " with cost: " + cost + "€ ********";
    } 
}
