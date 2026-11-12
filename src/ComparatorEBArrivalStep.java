import java.util.*; 

/**
 * Compares two {@link ElectricBike} objects based on their id string in ascending order.
 * This can be used for sorting lists of electric bikes.
 * @author DP Clasess
 * @version 2026.10.13
 */
public class ComparatorEBArrivalStep implements Comparator<ElectricBike>
{
    /**
     * Compares its two {@link ElectricBike} arguments for order.
     * The comparison is based on the lexicographical order of their ids.
     * @param eb1 The first electric bike to be compared.
     * @param eb2 The second electric bike to be compared.
     * @return A negative integer, zero, or a positive integer as the first
     * bike's plate is less than, equal to, or greater than the second.
     */
    public int compare(ElectricBike eb1, ElectricBike eb2){  
    if (eb1.getArrivalSteps() == -1 && eb2.getArrivalSteps() == -1)
        return 0; 
        
    if (eb1.getArrivalSteps() == -1)
        return 1;    
        
    if (eb2.getArrivalSteps() == -1)
        return -1;
        
    return Integer.compare(eb1.getArrivalSteps(), eb2.getArrivalSteps());
    } 
}
