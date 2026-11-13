import java.util.*;

/**
 * Compares two {@link BikeStation} objects by their identifier in ascending order.
 * It is used to sort lists of bike stations.
 * @author DP classes
 * @version 2026.10.13
 */
public class ComparatorBikeStationId implements Comparator<BikeStation>
{
    /**
     * Compares its two {@link BikeStation} arguments for order, using the
     * lexicographical order of their identifiers.
     * @param st1 The first bike station to be compared.
     * @param st2 The second bike station to be compared.
     * @return A negative integer, zero, or a positive integer as the identifier of
     * the first station is less than, equal to, or greater than the identifier of
     * the second one.
     */
    public int compare(BikeStation st1, BikeStation st2){  
        return st1.getId().compareTo(st2.getId());
    } 
}