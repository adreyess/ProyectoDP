import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test implementation of the comparator classes of the project.
 * Provides unit tests for the four comparators required by the statement (bikes
 * by identifier and by arrival step, stations by number of recharges and by
 * identifier) and for the three comparators used to order the docks. It also
 * checks that the chain of stable sorts used in the project resolves the
 * tie-breaks required by the statement.
 *
 * @author DP classes
 * @version 2026.11.13
 */
public class ComparatorsTest
{
    /** The company of the bikes used in the tests. */
    private EBCompany company;

    /**
     * Default constructor for test class ComparatorsTest.
     */
    public ComparatorsTest()
    {
    }

    /**
     * Sets up the test fixture: a company to create bikes.
     */
    @Before
    public void setUp()
    {
        company = new EBCompany("Test company");
    }

    /**
     * Tears down the test fixture.
     */
    @After
    public void tearDown()
    {
        company = null;
    }

    /**
     * Test the comparator of bikes by identifier: the identifiers must be compared
     * in ascending lexicographical order.
     */
    @Test
    public void testComparatorEBIdBike()
    {
        ComparatorEBIdBike comparator = new ComparatorEBIdBike();
        ElectricBike eb0 = createBike("EB0");
        ElectricBike eb1 = createBike("EB1");
        ElectricBike otherEb0 = createBike("EB0");

        assertTrue("EB0 must go before EB1", comparator.compare(eb0, eb1) < 0);
        assertTrue("EB1 must go after EB0", comparator.compare(eb1, eb0) > 0);
        assertEquals("Two bikes with the same id must be equal", 0, comparator.compare(eb0, otherEb0));
    }

    /**
     * Test the comparator of bikes by arrival step: the bikes that arrived first
     * go first and the bikes that have not arrived ({@code -1}) go last.
     */
    @Test
    public void testComparatorEBArrivalStep()
    {
        ComparatorEBArrivalStep comparator = new ComparatorEBArrivalStep();
        ElectricBike first = createBike("EB0");
        ElectricBike second = createBike("EB1");
        ElectricBike pending = createBike("EB2");
        ElectricBike alsoFirst = createBike("EB3");
        first.setArrivalSteps(2);
        second.setArrivalSteps(5);
        alsoFirst.setArrivalSteps(2);

        assertTrue("The bike that arrived first must go first", comparator.compare(first, second) < 0);
        assertTrue("The bike that arrived later must go last", comparator.compare(second, first) > 0);
        assertEquals("Two bikes with the same step must be equal", 0, comparator.compare(first, alsoFirst));
        assertTrue("A bike that has not arrived must go last", comparator.compare(pending, first) > 0);
        assertTrue("An arrived bike must go before a pending one", comparator.compare(first, pending) < 0);
        assertEquals("Two pending bikes must be equal", 0, comparator.compare(pending, createBike("EB4")));
    }

    /**
     * Test the comparator of stations by identifier.
     */
    @Test
    public void testComparatorBikeStationId()
    {
        ComparatorBikeStationId comparator = new ComparatorBikeStationId();
        BikeStation cc00 = createStation("CC00");
        BikeStation cc01 = createStation("CC01");
        BikeStation otherCc00 = createStation("CC00");

        assertTrue("CC00 must go before CC01", comparator.compare(cc00, cc01) < 0);
        assertTrue("CC01 must go after CC00", comparator.compare(cc01, cc00) > 0);
        assertEquals("Two stations with the same id must be equal", 0, comparator.compare(cc00, otherCc00));
    }

    /**
     * Test the comparator of stations by number of recharged bikes: the station
     * with more recharges must go first. The tie-break by identifier is not part
     * of the comparator, it is done by the stable sort of the project.
     */
    @Test
    public void testComparatorBikeStationNumberRecharged()
    {
        ComparatorBikeStationNumberRecharged comparator = new ComparatorBikeStationNumberRecharged();
        BikeStation one = createStation("CC00");
        BikeStation two = createStation("CC01");
        Dock oneDock = new Dock("CC00_000", 20, 0.2f);
        Dock twoDock = new Dock("CC01_000", 20, 0.2f);
        one.addDock(oneDock);
        two.addDock(twoDock);
        oneDock.recharge(createBikeWithEmptyBattery("EB0"));
        twoDock.recharge(createBikeWithEmptyBattery("EB1"));
        twoDock.recharge(createBikeWithEmptyBattery("EB2"));

        assertTrue("The station with more recharges must go first", comparator.compare(two, one) < 0);
        assertTrue("The station with fewer recharges must go last", comparator.compare(one, two) > 0);

        BikeStation alsoOne = createStation("CC02");
        Dock alsoDock = new Dock("CC02_000", 20, 0.2f);
        alsoOne.addDock(alsoDock);
        alsoDock.recharge(createBikeWithEmptyBattery("EB3"));

        assertEquals("Same recharges must be equal", 0, comparator.compare(one, alsoOne));
    }

    /**
     * Test the comparator of docks by charging speed: the fastest dock must go
     * first. The tie-breaks by fee and by identifier are not part of the
     * comparator, they are done by the stable sort of the project.
     */
    @Test
    public void testComparatorDockChargingSpeed()
    {
        ComparatorDockChargingSpeed comparator = new ComparatorDockChargingSpeed();
        Dock fast = new Dock("CC00_001", 80, 0.8f);
        Dock slow = new Dock("CC00_000", 20, 0.2f);
        Dock alsoFast = new Dock("CC00_002", 80, 0.4f);

        assertTrue("The fastest dock must go first", comparator.compare(fast, slow) < 0);
        assertTrue("The slowest dock must go last", comparator.compare(slow, fast) > 0);
        assertEquals("Two docks with the same speed must be equal", 0, comparator.compare(fast, alsoFast));
    }

    /**
     * Test the comparator of docks by charging fee: the cheapest dock must go
     * first.
     */
    @Test
    public void testComparatorDockChargingFee()
    {
        ComparatorDockChargingFee comparator = new ComparatorDockChargingFee();
        Dock cheap = new Dock("CC00_000", 20, 0.2f);
        Dock expensive = new Dock("CC00_001", 20, 0.8f);
        Dock alsoCheap = new Dock("CC00_002", 40, 0.2f);

        assertTrue("The cheapest dock must go first", comparator.compare(cheap, expensive) < 0);
        assertTrue("The most expensive dock must go last", comparator.compare(expensive, cheap) > 0);
        assertEquals("Two docks with the same fee must be equal", 0, comparator.compare(cheap, alsoCheap));
    }

    /**
     * Test the comparator of docks by identifier.
     */
    @Test
    public void testComparatorDocksId()
    {
        ComparatorDocksId comparator = new ComparatorDocksId();
        Dock first = new Dock("CC00_000", 20, 0.2f);
        Dock second = new Dock("CC00_001", 80, 0.8f);
        Dock alsoFirst = new Dock("CC00_000", 40, 0.4f);

        assertTrue("CC00_000 must go before CC00_001", comparator.compare(first, second) < 0);
        assertTrue("CC00_001 must go after CC00_000", comparator.compare(second, first) > 0);
        assertEquals("Two docks with the same id must be equal", 0, comparator.compare(first, alsoFirst));
    }

    /**
     * Test that the comparators can be used to sort real lists with the methods of
     * {@link Collections}, which is the use they have in the project.
     */
    @Test
    public void testSortingWithCollections()
    {
        List<ElectricBike> bikes = new ArrayList<>();
        bikes.add(createBike("EB2"));
        bikes.add(createBike("EB0"));
        bikes.add(createBike("EB1"));

        Collections.sort(bikes, new ComparatorEBIdBike());

        assertEquals("The list must be ordered by identifier", "EB0", bikes.get(0).getIdBike());
        assertEquals("The list must be ordered by identifier", "EB1", bikes.get(1).getIdBike());
        assertEquals("The list must be ordered by identifier", "EB2", bikes.get(2).getIdBike());
    }

    /**
     * Test that the chain of stable sorts used by {@link BikeStation} orders the
     * docks by speed, then by fee and then by identifier, as the statement
     * requires. The tie-breaks are resolved by the stable sort and not by the
     * comparators, which only compare their main criterion.
     */
    @Test
    public void testDockTieBreaksWithStableSort()
    {
        List<Dock> docks = new ArrayList<>();
        docks.add(new Dock("CC00_003", 20, 0.6f));
        docks.add(new Dock("CC00_001", 40, 0.4f));
        docks.add(new Dock("CC00_002", 40, 0.4f));
        docks.add(new Dock("CC00_000", 20, 0.2f));

        Collections.sort(docks, new ComparatorDocksId());
        Collections.sort(docks, new ComparatorDockChargingFee());
        Collections.sort(docks, new ComparatorDockChargingSpeed());

        assertEquals("The fastest and cheapest dock must go first", "CC00_001", docks.get(0).getId());
        assertEquals("The tie in speed and fee is kept by id", "CC00_002", docks.get(1).getId());
        assertEquals("The slowest and cheapest dock must go next", "CC00_000", docks.get(2).getId());
        assertEquals("The slowest and most expensive must go last", "CC00_003", docks.get(3).getId());
    }

    /**
     * Test that the chain of stable sorts used by {@link EBCompany} orders the
     * stations by number of recharges and, in case of a tie, by identifier.
     */
    @Test
    public void testStationTieBreaksWithStableSort()
    {
        BikeStation cc02 = createStation("CC02");
        BikeStation cc00 = createStation("CC00");
        Dock dock02 = new Dock("CC02_000", 20, 0.2f);
        Dock dock00 = new Dock("CC00_000", 20, 0.2f);
        cc02.addDock(dock02);
        cc00.addDock(dock00);
        dock02.recharge(createBikeWithEmptyBattery("EB0"));
        dock00.recharge(createBikeWithEmptyBattery("EB1"));

        List<BikeStation> stations = new ArrayList<>();
        stations.add(cc02);
        stations.add(cc00);

        Collections.sort(stations, new ComparatorBikeStationId());
        Collections.sort(stations, new ComparatorBikeStationNumberRecharged());

        assertSame("The tie in recharges must be kept by identifier", cc00, stations.get(0));
        assertSame("The tie in recharges must be kept by identifier", cc02, stations.get(1));
    }

    /**
     * Creates a bike of the company of the fixture.
     * @param id The identifier of the bike.
     * @return A new bike with a full battery.
     */
    private ElectricBike createBike(String id)
    {
        return new ElectricBike(company, new Location(0, 0), new Location(1, 1), id, 30);
    }

    /**
     * Creates a bike of the company of the fixture with an empty battery.
     * @param id The identifier of the bike.
     * @return A new bike with an empty battery.
     */
    private ElectricBike createBikeWithEmptyBattery(String id)
    {
        ElectricBike bike = createBike(id);
        bike.setBatteryLevel(0);
        return bike;
    }

    /**
     * Creates a station at the origin of the grid.
     * @param id The identifier of the station.
     * @return A new station without docks.
     */
    private BikeStation createStation(String id)
    {
        return new BikeStation(id, new Location(0, 0));
    }
}
