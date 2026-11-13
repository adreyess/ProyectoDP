import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test implementation of the {@link EBCompany} class.
 * Provides unit tests for the registration of bikes and stations, the search of
 * stations by identifier and by location, the number of stations, the copies of
 * the lists and the reset of the company.
 *
 * @author DP classes
 * @version 2026.11.13
 */
public class EBCompanyTest
{
    /** The company under test. */
    private EBCompany company;
    /** Bikes added to the company in a different order from their identifiers. */
    private ElectricBike bike2;
    private ElectricBike bike0;
    private ElectricBike bike1;
    /** Stations added to the company in a different order from their identifiers. */
    private BikeStation station2;
    private BikeStation station0;
    private BikeStation station1;

    /**
     * Default constructor for test class EBCompanyTest.
     */
    public EBCompanyTest()
    {
    }

    /**
     * Sets up the test fixture: a company, three bikes and three stations.
     */
    @Before
    public void setUp()
    {
        company = new EBCompany("Test company");
        Location target = new Location(5, 5);

        bike2 = new ElectricBike(company, new Location(2, 2), target, "EB2", 30);
        bike0 = new ElectricBike(company, new Location(0, 0), target, "EB0", 30);
        bike1 = new ElectricBike(company, new Location(1, 1), target, "EB1", 30);

        station2 = new BikeStation("CC02", new Location(14, 16));
        station0 = new BikeStation("CC00", new Location(10, 5));
        station1 = new BikeStation("CC01", new Location(10, 11));
    }

    /**
     * Tears down the test fixture.
     */
    @After
    public void tearDown()
    {
        company = null;
        bike0 = null;
        bike1 = null;
        bike2 = null;
        station0 = null;
        station1 = null;
        station2 = null;
    }

    /**
     * Test that the fleet is kept ordered by identifier, no matter the order in
     * which the bikes are added.
     */
    @Test
    public void testAddElectricBikeKeepsTheFleetOrdered()
    {
        company.addElectricBike(bike2);
        company.addElectricBike(bike0);
        company.addElectricBike(bike1);

        List<ElectricBike> bikes = company.getAllElectricBikes();

        assertEquals("All the bikes must be registered", 3, bikes.size());
        assertEquals("The bikes must be ordered by identifier", "EB0", bikes.get(0).getIdBike());
        assertEquals("The bikes must be ordered by identifier", "EB1", bikes.get(1).getIdBike());
        assertEquals("The bikes must be ordered by identifier", "EB2", bikes.get(2).getIdBike());
    }

    /**
     * Test that the stations are kept ordered by identifier, no matter the order
     * in which they are added.
     */
    @Test
    public void testAddBikeStationKeepsTheNetworkOrdered()
    {
        company.addBikeStation(station2);
        company.addBikeStation(station0);
        company.addBikeStation(station1);

        List<BikeStation> stations = company.getAllBikeStations();

        assertEquals("All the stations must be registered", 3, stations.size());
        assertEquals("The stations must be ordered by identifier", "CC00", stations.get(0).getId());
        assertEquals("The stations must be ordered by identifier", "CC01", stations.get(1).getId());
        assertEquals("The stations must be ordered by identifier", "CC02", stations.get(2).getId());
    }

    /**
     * Test the search of a station by its identifier, including the case in which
     * the identifier does not exist.
     */
    @Test
    public void testGetBikeStationById()
    {
        company.addBikeStation(station0);
        company.addBikeStation(station1);

        assertSame("The station with that identifier must be returned", station1, company.getBikeStation("CC01"));
        assertNull("A station that does not exist must return null", company.getBikeStation("CC99"));
    }

    /**
     * Test the search of a station by its location, including the case in which
     * there is no station at that location.
     */
    @Test
    public void testGetBikeStationByLocation()
    {
        company.addBikeStation(station0);
        company.addBikeStation(station2);

        assertSame("The station must be found by location", station2, company.getBikeStation(new Location(14, 16)));
        assertNull("A location without station must return null", company.getBikeStation(new Location(0, 0)));
    }

    /**
     * Test the number of stations of the company.
     */
    @Test
    public void testGetNumberOfStations()
    {
        assertEquals("A new company must have no stations", 0, company.getNumberOfStations());

        company.addBikeStation(station0);
        company.addBikeStation(station1);

        assertEquals("The number of stations must be updated", 2, company.getNumberOfStations());
    }

    /**
     * Test that the list of bikes returned by the company is a copy, so modifying
     * it does not change the company.
     */
    @Test
    public void testGetAllElectricBikesReturnsACopy()
    {
        company.addElectricBike(bike0);

        company.getAllElectricBikes().clear();

        assertEquals("The company must not change", 1, company.getAllElectricBikes().size());
    }

    /**
     * Test that the list of stations returned by the company is a copy, so
     * modifying it does not change the company.
     */
    @Test
    public void testGetAllBikeStationsReturnsACopy()
    {
        company.addBikeStation(station0);

        company.getAllBikeStations().clear();

        assertEquals("The company must not change", 1, company.getAllBikeStations().size());
    }

    /**
     * Test that the reset of the company removes all its bikes and stations.
     */
    @Test
    public void testReset()
    {
        company.addElectricBike(bike0);
        company.addBikeStation(station0);

        company.reset();

        assertEquals("The fleet must be empty", 0, company.getAllElectricBikes().size());
        assertEquals("The network must be empty", 0, company.getAllBikeStations().size());
        assertEquals("The number of stations must be zero", 0, company.getNumberOfStations());
    }

    /**
     * Test the name of the company and the control of a null name.
     */
    @Test
    public void testName()
    {
        assertEquals("The name must be the given one", "Test company", company.getName());

        company.setName("EBBike Cáceres");
        assertEquals("The name must be the new one", "EBBike Cáceres", company.getName());

        try {
            company.setName(null);
            fail("A null name must not be accepted");
        }
        catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Test that a company cannot be created with a null name.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullName()
    {
        new EBCompany(null);
    }
}
