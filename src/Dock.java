import java.util.ArrayList;
import java.util.List;
/**
 * Models a dock unit within a {@link BikeStation}.
 * A dock has a unique identifier, a charging speed (in watt-hours per recharge),
 * a charging fee (in euros per watt-hour) and the list of {@link ElectricBike}s it
 * has recharged. It also keeps the total amount of money collected and whether it
 * is free or busy.
 *
 * @author David J. Barnes and Michael Kölling
 * @author DP classes 
 * @version 2026.10.13
 */
public class Dock
{
    /** The unique identifier of the dock. */
    private String idDock;
    /** The charging speed of the dock, in watt-hours. */
    private int chargingSpeed;
    /** The charging fee of the dock, in euros per watt-hour. */
    private float chargingFee;
    /** The bikes that have been recharged in this dock, in order of arrival. */
    private List<ElectricBike> bikesRecharged;
    /** The total amount of money collected by this dock, in euros. */
    private float amountCollected;
    /** Whether this dock is available ({@code true}) or occupied ({@code false}). */
    private boolean free;

    /**
     * Creates a dock with the given identifier, charging speed and charging fee.
     * The dock starts free, with no bikes recharged and no money collected.
     * @param id The unique identifier of the dock. Must not be null.
     * @param speed The charging speed of the dock, in watt-hours.
     * @param fee The charging fee of the dock, in euros per watt-hour.
     * @throws NullPointerException If id is null.
     */
    public Dock(String id, int speed, float fee)
    {
        if (id == null)
            throw new NullPointerException("The dock id cannot be null");
            
        this.idDock = id;
        this.chargingSpeed = speed;
        this.chargingFee = fee;
        this.bikesRecharged = new ArrayList<ElectricBike>();
        this.amountCollected = 0;
        this.free = true;
    }
    
    /**
     * Returns a string representation of the dock.
     * @return The dock in the format
     * {@code "Dock: <id>, <speed>wh, <fee>€, <number of bikes>, <amount>€"},
     * for example {@code "Dock: CC01_003, 80wh, 0.8€, 2, 32.0€"}.
     */
    public String toString()
    {
        return "Dock: " + this.idDock + ", " + this.chargingSpeed + "wh, " + this.chargingFee + "€, " + this.bikesRecharged.size() + ", " + this.amountCollected + "€";
    }    

    
    /**
     * Gets the total number of {@link ElectricBike}s that have been recharged
     * by this dock.
     * @return The number of bikes recharged here.
     */
    public int getNumberEBRecharged(){
        return this.bikesRecharged.size();
    }
    
    /**
     * Gets a copy of the bikes recharged by this dock, in the order in which they
     * were recharged.
     * @return A new list containing the recharged bikes. Modifying it does not
     * affect the dock.
     */
    public List<ElectricBike> getAllBikesRecharged(){
        return new ArrayList<ElectricBike>(this.bikesRecharged);
    }
    
    /**
     * Simulates the charging process of an {@link ElectricBike} in this dock.
     * The number of watt-hours needed is the difference between the capacity and
     * the current level of the bike, and the cost is that amount multiplied by the
     * charging fee. If the bike actually needs energy, it is registered as
     * recharged, the amount collected is updated and the battery is filled up to
     * its capacity.
     * @param bike The bike to recharge. Must not be null.
     * @return The cost of the recharge in euros ({@code 0} if the battery was already full).
     */
    public float recharge(ElectricBike bike){
        int wh = bike.getBatteryCapacity() - bike.getBatteryLevel();
        float cost = this.chargingFee * wh;
        if (wh > 0){
            addBikeRecharged(bike);
            updateAmountCollected(cost);
            bike.setBatteryLevel(bike.getBatteryCapacity());
            bike.setWsCharged(bike.getWsCharged() + wh);
        }
        return cost;
    }
    
    /**
     * Adds an amount of money to the total collected by this dock.
     * @param amount The amount to add, in euros.
     */
    public void updateAmountCollected(float amount){
        this.amountCollected += amount;
    }

    /**
     * Registers a bike as recharged by this dock. The bike is appended to the end
     * of the list, so the last element is always the most recent recharge.
     * @param bike The bike to register. Must not be null.
     */
    public void addBikeRecharged(ElectricBike bike){
        this.bikesRecharged.add(bike);
    }
    
    /**
     * Tells whether this dock is available to recharge a bike.
     * @return {@code true} if the dock is free, {@code false} if it is busy.
     */
    public boolean isFree(){
        return this.free;
    }
    
    /**
     * Gets the charging speed of this dock.
     * @return The charging speed, in watt-hours.
     */
    public int getChargingSpeed(){
        return this.chargingSpeed;
    }
    
    /**
     * Gets the charging fee of this dock.
     * @return The charging fee, in euros per watt-hour.
     */
    public float getChargingFee(){
        return this.chargingFee;
    }
    
    /**
     * Gets the unique identifier of this dock.
     * @return The identifier of the dock.
     */
    public String getId(){
        return this.idDock;
    }
    
    /**
     * Gets the total amount of money collected by this dock.
     * @return The amount collected, in euros.
     */
    public float getAmountCollected(){
        return this.amountCollected;
    }
    
    /**
     * Sets whether this dock is free or busy.
     * @param free {@code true} to mark it as free, {@code false} to mark it as busy.
     */
    public void setFree(boolean free){
        this.free = free;
    }
    
    /**
     * Sets the charging speed of this dock.
     * @param speed The new charging speed, in watt-hours.
     */
    public void setChargingSpeed(int speed){
        this.chargingSpeed = speed;
    }
    
    /**
     * Sets the charging fee of this dock.
     * @param fee The new charging fee, in euros per watt-hour.
     */
    public void setChargingFee(float fee){
        this.chargingFee = fee;
    }
    
    /**
     * Sets the unique identifier of this dock.
     * @param id The new identifier of the dock. Must not be null.
     * @throws NullPointerException If id is null.
     */
    public void setId(String id){
        if (id == null)
            throw new NullPointerException("The dock id cannot be null");
            
        this.idDock = id;
    }
    
    /**
     * Sets the total amount of money collected by this dock.
     * @param amount The new amount collected, in euros.
     */
    public void setAmountCollected(float amount){
        this.amountCollected = amount;
    }
}
