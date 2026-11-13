import java.util.*;

/**
 * Compares two {@link BikeStation} objects by the number of bikes recharged in
 * them, in descending order (the station with more recharges goes first). It is
 * used to sort the stations in the final information of the simulation.
 * @author DP classes
 * @version 2026.10.13
 */
public class ComparatorBikeStationNumberRecharged implements Comparator<BikeStation>
{
    /**
     * Compares its two {@link BikeStation} arguments for order, using the number
     * of bikes recharged in each one in descending order.
     * @param st1 The first bike station to be compared.
     * @param st2 The second bike station to be compared.
     * @return A negative integer, zero, or a positive integer as the first station
     * has more, the same, or fewer recharges than the second one.
     */
    public int compare(BikeStation st1, BikeStation st2){  
        return Integer.compare(st2.getNumberBikesRecharged(), st1.getNumberBikesRecharged());
    } 
}
