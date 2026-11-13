import java.util.Comparator;

/**
 * Compares two {@link Dock} objects by their charging speed in descending order
 * (the fastest dock goes first). It is the main criterion used to order the docks
 * of a station.
 * @author DP classes
 * @version 2026.10.13
 */
public class ComparatorDockChargingSpeed implements Comparator<Dock>
{
    /**
     * Compares its two {@link Dock} arguments for order, using their charging speed
     * in descending order.
     * @param c1 The first dock to be compared.
     * @param c2 The second dock to be compared.
     * @return A negative integer, zero, or a positive integer as the first dock is
     * faster, equally fast, or slower than the second one.
     */
    public int compare(Dock c1, Dock c2){  
        return Integer.compare(c2.getChargingSpeed(), c1.getChargingSpeed());
    } 
}
