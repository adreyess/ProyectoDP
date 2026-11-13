import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test implementation of the {@link BikeStation} class.
 * Provides unit tests for the installation of docks (and their order), the search
 * of free docks and docks by identifier, the number of bikes recharged and the
 * string representation.
 *
 * @author DP classes
 * @version 2026.11.13
 */
public class BikeStationTest
{
    /** The station under test. */
    private BikeStation station;
    /** Docks used to check the order of the station. */
    private Dock slow;
    private Dock fastAndCheap;
    private Dock fastAndEqual;
    private Dock fastAndExpensive;

    /**
     * Default constructor for test class BikeStationTest.
     */
    public BikeStationTest()
    {
    }

    /**
     * Sets up the test fixture: a station without docks and four docks with
     * different speeds and fees.
     */
    @Before
    public void setUp()
    {
        station = new BikeStation("CC01", new Location(10, 11));
        slow = new Dock("CC01_003", 20, 0.6f);
        fastAndCheap = new Dock("CC01_000", 40, 0.2f);
        fastAndEqual = new Dock("CC01_001", 40, 0.4f);
        fastAndExpensive = new Dock("CC01_002", 40, 0.4f);
    }

    /**
     * Tears down the test fixture.
     */
    @After
    public void tearDown()
    {
        station = null;
        slow = null;
        fastAndCheap = null;
        fastAndEqual = null;
        fastAndExpensive = null;
    }

    /**
     * Test that the docks are kept ordered by charging speed (descending), then
     * by charging fee (ascending) and finally by identifier (ascending),
     * regardless of the order in which they are added.
     */
    @Test
    public void testAddDock()
    {
        station.addDock(slow);
        station.addDock(fastAndExpensive);
        station.addDock(fastAndCheap);
        station.addDock(fastAndEqual);

        List<Dock> docks = station.getAllDocks();

        assertEquals("The station must have four docks", 4, docks.size());
        assertEquals("The fastest and cheapest dock must be the first one", "CC01_000", docks.get(0).getId());
        assertEquals("The same speed and fee is ordered by id", "CC01_001", docks.get(1).getId());
        assertEquals("Docks with the same speed are ordered by fee", "CC01_002", docks.get(2).getId());
        assertEquals("The slowest dock must be the last one", "CC01_003", docks.get(3).getId());
    }

    /**
     * Test that the station returns its first free dock and that the search
     * follows the order of the docks.
     */
    @Test
    public void testGetFreeDock()
    {
        station.addDock(slow);
        station.addDock(fastAndCheap);
        station.addDock(fastAndEqual);

        assertSame("The first free dock must be the fastest and cheapest one", fastAndCheap, station.getFreeDock());

        fastAndCheap.setFree(false);
        assertSame("If the first one is busy, the next one must be returned", fastAndEqual, station.getFreeDock());

        slow.setFree(false);
        fastAndEqual.setFree(false);
        assertNull("With all the docks busy, no dock must be returned", station.getFreeDock());
    }

    /**
     * Test that a dock can be found by its identifier.
     */
    @Test
    public void testGetDockById()
    {
        station.addDock(slow);
        station.addDock(fastAndCheap);

        assertSame("The dock with that identifier must be returned", slow, station.getDockById("CC01_003"));
        assertNull("A dock that is not in the station must not be returned", station.getDockById("CC09_999"));
    }

    /**
     * Test that the number of bikes recharged in the station is the sum of the
     * bikes recharged in each of its docks.
     */
    @Test
    public void testGetNumberBikesRecharged()
    {
        station.addDock(slow);
        station.addDock(fastAndCheap);

        EBCompany company = new EBCompany("Test company");
        ElectricBike bike1 = new ElectricBike(company, new Location(10, 11), new Location(10, 13), "EB0", 30);
        ElectricBike bike2 = new ElectricBike(company, new Location(10, 11), new Location(10, 13), "EB1", 30);
        ElectricBike bike3 = new ElectricBike(company, new Location(10, 11), new Location(10, 13), "EB2", 30);
        bike1.setBatteryLevel(0);
        bike2.setBatteryLevel(0);
        bike3.setBatteryLevel(0);

        slow.recharge(bike1);
        fastAndCheap.recharge(bike2);
        fastAndCheap.recharge(bike3);

        assertEquals("The recharges of all the docks must be added", 3, station.getNumberBikesRecharged());
    }

    /**
     * Test that the list of docks returned by the station is a copy, so modifying
     * it does not change the station.
     */
    @Test
    public void testGetAllDocksReturnsACopy()
    {
        station.addDock(slow);

        station.getAllDocks().clear();

        assertEquals("The station must not change", 1, station.getAllDocks().size());
    }

    /**
     * Test that the location and the identifier of the station can be changed.
     */
    @Test
    public void testSettersAndGetters()
    {
        station.setId("CC02");
        station.setLocation(new Location(14, 16));

        assertEquals("The identifier must be the new one", "CC02", station.getId());
        assertEquals("The location must be the new one", new Location(14, 16), station.getLocation());
    }

    /**
     * Test the string representation of the station.
     */
    @Test
    public void testToString()
    {
        String expected = "BikeStation: CC01, 0, 10-11";
        assertEquals("The station text must be complete", expected, station.toString());

        station.addDock(slow);
        EBCompany company = new EBCompany("Test company");
        ElectricBike bike = new ElectricBike(company, new Location(10, 11), new Location(10, 13), "EB0", 30);
        bike.setBatteryLevel(0);
        slow.recharge(bike);

        expected = "BikeStation: CC01, 1, 10-11";
        assertEquals("The recharges must be updated", expected, station.toString());
    }

    /**
     * Test that a station cannot be created with a null identifier.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullId()
    {
        new BikeStation(null, new Location(10, 11));
    }

    /**
     * Test that a station cannot be created with a null location.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullLocation()
    {
        new BikeStation("CC01", null);
    }
}
