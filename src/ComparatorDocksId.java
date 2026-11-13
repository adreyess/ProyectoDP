import java.util.Comparator;

/**
 * Compares two {@link Dock} objects by their identifier in ascending order. It is
 * used as the last tie-breaker when the docks of a station are ordered.
 * @author DP classes
 * @version 2026.10.13
 */
public class ComparatorDocksId implements Comparator<Dock>
{
    /**
     * Compares its two {@link Dock} arguments for order, using the lexicographical
     * order of their identifiers.
     * @param c1 The first dock to be compared.
     * @param c2 The second dock to be compared.
     * @return A negative integer, zero, or a positive integer as the identifier of
     * the first dock is less than, equal to, or greater than the second one.
     */
    public int compare(Dock c1, Dock c2){  
        return c1.getId().compareTo(c2.getId());
    } 
}
