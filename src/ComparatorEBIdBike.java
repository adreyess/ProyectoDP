import java.util.Comparator;

/**
 * Compares two {@link ElectricBike} objects by their identifier in ascending order.
 * It is used to sort lists of electric bikes and as a tie-breaker in the final
 * information of the simulation.
 * @author DP classes
 * @version 2026.10.13
 */
public class ComparatorEBIdBike implements Comparator<ElectricBike>
{
    /**
     * Compares its two {@link ElectricBike} arguments for order, using the
     * lexicographical order of their identifiers.
     * @param eb1 The first electric bike to be compared.
     * @param eb2 The second electric bike to be compared.
     * @return A negative integer, zero, or a positive integer as the identifier of
     * the first bike is less than, equal to, or greater than the second one.
     */
    public int compare(ElectricBike eb1, ElectricBike eb2){  
        return eb1.getIdBike().compareTo(eb2.getIdBike());
    } 
}