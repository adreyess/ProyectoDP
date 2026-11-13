import java.util.Comparator;

/**
 * Compares two {@link ElectricBike} objects by the step in which they reached
 * their final destination, in ascending order (the bike that arrived first goes
 * first). A bike that has not arrived yet has an arrival step of -1 and is always
 * placed after the bikes that did arrive.
 * @author DP classes
 * @version 2026.10.13
 */
public class ComparatorEBArrivalStep implements Comparator<ElectricBike>
{
    /**
     * Compares its two {@link ElectricBike} arguments for order, using the step in
     * which each one arrived at its final destination. The bikes that have not
     * arrived ({@code -1}) are considered later than the ones that did arrive.
     * @param eb1 The first electric bike to be compared.
     * @param eb2 The second electric bike to be compared.
     * @return A negative integer, zero, or a positive integer as the first bike
     * arrived earlier, at the same step, or later than the second one. If neither
     * of them has arrived, {@code 0} is returned.
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
