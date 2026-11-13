import java.util.Comparator;

/**
 * Compares two {@link Dock} objects by their charging fee in ascending order (the
 * cheapest dock goes first). It is used as the second criterion, after the
 * charging speed, when the docks of a station are ordered.
 * @author DP classes
 * @version 2026.10.13
 */
public class ComparatorDockChargingFee implements Comparator<Dock>
{
    /**
     * Compares its two {@link Dock} arguments for order, using their charging fee.
     * @param c1 The first dock to be compared.
     * @param c2 The second dock to be compared.
     * @return A negative integer, zero, or a positive integer as the charging fee
     * of the first dock is less than, equal to, or greater than the second one.
     */
    public int compare(Dock c1, Dock c2)
    {  
        return Float.compare(c1.getChargingFee(), c2.getChargingFee());
    } 
}
