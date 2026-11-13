import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test implementation of the {@link Location} class.
 * Provides unit tests for methods like {@code distance()}, {@code nextLocation()},
 * {@code equals()}, {@code hashCode()} and the control of errors of the constructor.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2026.10.13 DP classes
 */
public class LocationTest
{
    /** A location in the middle of the grid, used as the starting point of the tests. */
    private Location center;
    /** A location on the upper-left area of the grid, used as an extra reference. */
    private Location other;

    /**
     * Default constructor for test class LocationTest.
     */
    public LocationTest()
    {
    }

    /**
     * Sets up the test fixture.
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        center = new Location(10, 10);
        other = new Location(4, 7);
    }

    /**
     * Tears down the test fixture.
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
        center = null;
        other = null;
    }

    /**
     * Tests the {@code distance} method of the {@link Location} class.
     * The distance is the Chebyshev distance: the maximum of the differences of
     * each coordinate, so diagonal movements count as a single step.
     */
    @Test
    public void testDistance()
    {
        assertEquals(0, center.distance(new Location(10, 10)));
        assertEquals(3, center.distance(new Location(13, 7)));
        assertEquals(4, center.distance(new Location(14, 6)));
        assertEquals(5, center.distance(new Location(15, 12)));
        assertEquals(2, center.distance(new Location(8, 9)));
        assertEquals(9, center.distance(new Location(1, 19)));
        assertEquals(19, new Location(0, 19).distance(new Location(19, 0)));
        assertEquals(6, other.distance(new Location(10, 1)));
    }

    /**
     * Tests that the distance is symmetric: it does not matter which location is
     * the origin and which one is the destination.
     */
    @Test
    public void testDistanceIsSymmetric()
    {
        Location a = new Location(2, 3);
        Location b = new Location(15, 9);

        assertEquals(a.distance(b), b.distance(a));
        assertEquals(13, a.distance(b));
    }

    /**
     * Tests the {@code nextLocation} method when the destination is adjacent
     * (one step away in any direction). The returned location must be the
     * destination itself, seen from the eight possible directions.
     */
    @Test
    public void testAdjacentLocations()
    {
        assertEquals(new Location(9, 9), center.nextLocation(new Location(9, 9)));
        assertEquals(new Location(10, 9), center.nextLocation(new Location(10, 9)));
        assertEquals(new Location(11, 9), center.nextLocation(new Location(11, 9)));
        assertEquals(new Location(9, 10), center.nextLocation(new Location(9, 10)));
        assertEquals(new Location(11, 10), center.nextLocation(new Location(11, 10)));
        assertEquals(new Location(9, 11), center.nextLocation(new Location(9, 11)));
        assertEquals(new Location(10, 11), center.nextLocation(new Location(10, 11)));
        assertEquals(new Location(11, 11), center.nextLocation(new Location(11, 11)));
    }

    /**
     * Tests the {@code nextLocation} method when the destination is not adjacent
     * (more than one step away). The movement is diagonal, so both coordinates
     * can change at the same time; when one coordinate is already aligned with
     * the destination, only the other one moves.
     */
    @Test
    public void testNonAdjacentLocations()
    {
        assertEquals(new Location(11, 10), center.nextLocation(new Location(19, 10)));
        assertEquals(new Location(10, 9), center.nextLocation(new Location(10, 1)));
        assertEquals(new Location(11, 11), center.nextLocation(new Location(19, 19)));
        assertEquals(new Location(9, 9), center.nextLocation(new Location(1, 1)));
        assertEquals(new Location(5, 8), other.nextLocation(new Location(10, 19)));
    }

    /**
     * Tests the {@code nextLocation} method when the destination is the location
     * itself: the returned location must be equal, although it is a new object.
     */
    @Test
    public void testNextLocationAtDestination()
    {
        Location next = center.nextLocation(new Location(10, 10));

        assertEquals(center, next);
        assertNotSame(center, next);
    }

    /**
     * Tests the {@code equals} method: two locations are equal only when they are
     * both {@link Location} objects with the same coordinates.
     */
    @Test
    public void testEquals()
    {
        assertTrue(center.equals(new Location(10, 10)));
        assertFalse(center.equals(new Location(10, 11)));
        assertFalse(center.equals(null));
        assertFalse(center.equals("10-10"));
        assertFalse(center.equals(other));
    }

    /**
     * Tests that {@code hashCode} is consistent with {@code equals}: equal
     * locations have the same hash code.
     */
    @Test
    public void testHashCode()
    {
        assertEquals(center.hashCode(), new Location(10, 10).hashCode());
        assertNotEquals(center.hashCode(), other.hashCode());
    }

    /**
     * Tests the string representation of a location, which is the format
     * {@code "x-y"}.
     */
    @Test
    public void testToString()
    {
        assertEquals("10-10", center.toString());
        assertEquals("0-19", new Location(0, 19).toString());
        assertEquals("4-7", other.toString());
    }

    /**
     * Tests that a negative x coordinate is rejected by the constructor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeX()
    {
        new Location(-1, 5);
    }

    /**
     * Tests that a negative y coordinate is rejected by the constructor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeY()
    {
        new Location(5, -1);
    }

    /**
     * Tests that an x coordinate outside the grid is rejected by the constructor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testXOutOfRange()
    {
        new Location(EBikeDemo.MAX_X, 5);
    }

    /**
     * Tests that a y coordinate outside the grid is rejected by the constructor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testYOutOfRange()
    {
        new Location(5, EBikeDemo.MAX_Y);
    }
}
