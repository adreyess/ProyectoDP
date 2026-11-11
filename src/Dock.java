import java.util.ArrayList;
import java.util.List;
/**
 * Model a dock unit within a {@link BikeStation}.
 * It tracks its charging capabilities, fee, and the electric bikes it has recharged.
 * @author David J. Barnes and Michael Kölling
 * @author DP classes 
 * @version 2026.10.13
 */
public class Dock
{
    private String idDock;
    private int chargingSpeed;
    private float chargingFee;
    private List<ElectricBike> bikesRecharged;
    private float amountCollected;
    private boolean free;

    /**
     * Constructor for objects of class Dock.
     * //TODO complete the JavaDoc comments
    */
    public Dock(String id, int speed, float fee)
    {
        this.idDock = id;
        this.chargingSpeed = speed;
        this.chargingFee = fee;
        this.bikesRecharged = new ArrayList<ElectricBike>();
        this.amountCollected = 0;
        this.free = true;
    }
    
    /**
     * Returns a string representation of the dock, including its ID, speed, fee, and the number of EVs recharged.
     * @return A string representation of the dock.
     */
    public String toString()
    {
        return "(Dock: " + this.idDock + ", " + this.chargingSpeed + "wh, " + this.chargingFee + "€, " + this.bikesRecharged.size() + ", " + this.amountCollected + "€)";
    }    

    
    /**
     * @return The total number of {@link ElectricBike}s that have been recharged by this dock.
     */
    public int getNumberEBRecharged(){
        return this.bikesRecharged.size();
    }
    
    public List<ElectricBike> getAllBikesRecharged(){
        return new ArrayList<ElectricBike>(this.bikesRecharged);
    }
    
    /**
     * Simulates the charging process for an {@link ElectricBike}.
     * Increases the amount collected and registers the bike as recharged.
     * //TODO complete the JavaDoc comments
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
     * Updates the amount collected
     * //TODO complete the JavaDoc comments
     */
    public void updateAmountCollected(float amount){
        this.amountCollected += amount;
    }

    /**
     * Adds a new bike to bike
     * //TODO complete the JavaDoc comments
     */
    public void addBikeRecharged(ElectricBike bike){
        this.bikesRecharged.add(bike);
    }
    
    public boolean isFree(){
        return this.free;
    }
    
    public int getChargingSpeed(){
        return this.chargingSpeed;
    }
    
    public float getChargingFee(){
        return this.chargingFee;
    }
    
    public String getId(){
        return this.idDock;
    }
    
    public float getAmountCollected(){
        return this.amountCollected;
    }
    
    public void setFree(boolean free){
        this.free = free;
    }
    
    public void setChargingSpeed(int speed){
        this.chargingSpeed = speed;
    }
    
    public void setChargingFee(float fee){
        this.chargingFee = fee;
    }
    
    public void setId(String id){
        this.idDock = id;
    }
    
    public void setAmountCollected(float amount){
        this.amountCollected = amount;
    }
}
