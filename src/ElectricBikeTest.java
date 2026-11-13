import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test implementation of the {@link ElectricBike} class.
 * Provides unit tests for the creation of the bike, the battery control, the
 * calculation of the route, the recharge in a station, the simulation of a step
 * and the different string representations.
 *
 * @author DP classes
 * @version 2026.11.13
 */
public class ElectricBikeTest
{
    /** Capacity of the battery used in most of the tests, in watt-hours. */
    private static final int BATTERY_CAPACITY = 30;
    /** Battery cost of one movement step, in watt-hours. */
    private static final int MOVING_COST = EBCompany.MOVING_COST;

    /** The company with the stations and docks used in the tests. */
    private EBCompany company;

    /**
     * Default constructor for test class ElectricBikeTest.
     */
    public ElectricBikeTest()
    {
    }

    /**
     * Sets up the test fixture: a company with the same four stations as the demo,
     * each one with four docks whose speed and fee grow with their index.
     */
    @Before
    public void setUp()
    {
        company = new EBCompany("Test company");

        BikeStation station0 = new BikeStation("CC00", new Location(10, 5));
        BikeStation station1 = new BikeStation("CC01", new Location(10, 11));
        BikeStation station2 = new BikeStation("CC02", new Location(14, 16));
        BikeStation station3 = new BikeStation("CC03", new Location(8, 4));

        company.addBikeStation(station0);
        company.addBikeStation(station1);
        company.addBikeStation(station2);
        company.addBikeStation(station3);

        addDocks(station0);
        addDocks(station1);
        addDocks(station2);
        addDocks(station3);
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
     * Test that a new bike is created with a full battery, no recharges, no
     * money spent, no idle turns, no planned recharge and no dock used.
     */
    @Test
    public void testCreation()
    {
        Location start = new Location(10, 13);
        Location target = new Location(1, 1);
        ElectricBike bike = new ElectricBike(company, start, target, "EB0", BATTERY_CAPACITY);

        assertEquals("The identifier must be the one given", "EB0", bike.getIdBike());
        assertSame("The company must be the one given", company, bike.getCompany());
        assertSame("The starting location must be the one given", start, bike.getLocation());
        assertSame("The target location must be the one given", target, bike.getTargetLocation());
        assertEquals("The capacity must be the one given", BATTERY_CAPACITY, bike.getBatteryCapacity());
        assertEquals("A new bike must start with a full battery", BATTERY_CAPACITY, bike.getBatteryLevel());
        assertEquals("A new bike has not recharged yet", 0, bike.getChargesCount());
        assertEquals("A new bike has not spent money yet", 0.0f, bike.getChargestCost(), 0.001f);
        assertEquals("A new bike has not been idle yet", 0, bike.getIdleCount());
        assertEquals("A new bike has not charged any energy yet", 0.0f, bike.getWsCharged(), 0.001f);
        assertEquals("A new bike has not arrived anywhere yet", -1, bike.getArrivalSteps());
        assertNull("A new bike has no planned recharge", bike.getRechargingLocation());
        assertEquals("A new bike has not used any dock", 0, bike.getAllDocks().size());
    }

    /**
     * Test the battery control: the bike has enough battery when the energy of the
     * battery is at least the distance multiplied by the cost of one step.
     */
    @Test
    public void testEnoughBattery()
    {
        ElectricBike bike = createBike(new Location(10, 13), new Location(1, 1), BATTERY_CAPACITY);

        assertTrue("Two steps (10wh) must be reachable", bike.enoughBattery(new Location(10, 11)));
        assertFalse("Twelve steps (60wh) must not be reachable", bike.enoughBattery(new Location(1, 1)));
        assertTrue("A distance of zero steps must always be reachable", bike.enoughBattery(new Location(10, 13)));

        bike.setBatteryLevel(2 * MOVING_COST);
        assertTrue("The energy must be enough when it is exactly the cost", bike.enoughBattery(new Location(10, 11)));

        bike.setBatteryLevel(2 * MOVING_COST - 1);
        assertFalse("One wh less than the cost must not be enough", bike.enoughBattery(new Location(10, 11)));
    }

    /**
     * Test the {@code calculateRoute} method with the two situations of the
     * statement: when the battery is enough to reach the target, no recharge is
     * planned; when it is not, the bike plans a stop in the best reachable
     * station (the one that minimises the distance from the bike to the station
     * plus the distance from the station to the target).
     */
    @Test
    public void testCalculateRoute()
    {
        ElectricBike withEnoughBattery = createBike(new Location(10, 13), new Location(10, 11), BATTERY_CAPACITY);
        withEnoughBattery.calculateRoute();
        assertNull("No recharge if the target is reachable", withEnoughBattery.getRechargingLocation());

        ElectricBike needingRecharge = createBike(new Location(10, 13), new Location(1, 1), BATTERY_CAPACITY);
        needingRecharge.calculateRoute();
        Location expected = new Location(10, 11);
        assertEquals("The best station must be planned", expected, needingRecharge.getRechargingLocation());
    }

    /**
     * Test that no recharge is planned when the battery is enough to reach the
     * final target.
     */
    @Test
    public void testCalculateRouteWithEnoughBattery()
    {
        ElectricBike bike = createBike(new Location(10, 13), new Location(10, 11), BATTERY_CAPACITY);

        bike.calculateRoute();

        assertNull("No recharge must be planned if the target is reachable", bike.getRechargingLocation());
    }

    /**
     * Test that the station that minimises the total distance is chosen when the
     * battery is not enough to reach the final target.
     */
    @Test
    public void testCalculateRouteNeedsRecharge()
    {
        ElectricBike bike = createBike(new Location(10, 13), new Location(1, 1), BATTERY_CAPACITY);

        bike.calculateRoute();

        assertEquals("The best reachable station must be chosen", new Location(10, 11), bike.getRechargingLocation());
    }

    /**
     * Test that no recharge is planned when no station is reachable with the
     * current battery.
     */
    @Test
    public void testCalculateRouteWithoutReachableStation()
    {
        ElectricBike bike = createBike(new Location(0, 0), new Location(19, 19), 5);

        bike.calculateRoute();

        assertNull("No recharge must be planned if no station is reachable", bike.getRechargingLocation());
    }

    /**
     * Test that the station where the bike is standing is not chosen as the
     * recharging station, even if it is the closest one.
     */
    @Test
    public void testCalculateRouteSkipsItsOwnStation()
    {
        EBCompany smallCompany = new EBCompany("Small company");
        smallCompany.addBikeStation(new BikeStation("AA00", new Location(5, 5)));
        smallCompany.addBikeStation(new BikeStation("AA01", new Location(8, 5)));

        ElectricBike bike = new ElectricBike(smallCompany, new Location(5, 5), new Location(0, 5), "EB0", 20);
        bike.calculateRoute();

        assertEquals("The current station must be skipped", new Location(8, 5), bike.getRechargingLocation());
    }

    /**
     * Test the recharge in the first free dock of the station: the battery must be
     * filled, the cost, the energy and the number of recharges must be updated and
     * the dock must be recorded.
     */
    @Test
    public void testRecharge()
    {
        ElectricBike bike = createBike(new Location(10, 11), new Location(10, 13), BATTERY_CAPACITY);
        bike.setBatteryLevel(5);
        bike.setRechargingLocation(new Location(10, 11));

        bike.recharge();

        assertEquals("One recharge must be registered", 1, bike.getChargesCount());
        assertEquals("The cost is the energy times the fee", 20.0f, bike.getChargestCost(), 0.001f);
        assertEquals("The energy charged must be accumulated", 25.0f, bike.getWsCharged(), 0.001f);
        assertEquals("The battery must be filled", BATTERY_CAPACITY, bike.getBatteryLevel());
        assertEquals("The dock must be recorded", 1, bike.getAllDocks().size());
        Dock usedDock = bike.getAllDocks().get(0);
        assertEquals("The fastest dock must be used", "CC01_003", usedDock.getId());
        assertEquals("The dock must collect the money", 20.0f, usedDock.getAmountCollected(), 0.001f);
        assertNull("The route must be recalculated after the recharge", bike.getRechargingLocation());
    }

    /**
     * Test that nothing happens when all the docks of the station are busy.
     */
    @Test
    public void testRechargeWhenAllDocksAreBusy()
    {
        BikeStation station = company.getBikeStation("CC01");
        for (Dock dock : station.getAllDocks()) {
            dock.setFree(false);
        }

        ElectricBike bike = createBike(new Location(10, 11), new Location(10, 13), BATTERY_CAPACITY);
        bike.setBatteryLevel(5);
        bike.setRechargingLocation(new Location(10, 11));

        bike.recharge();

        assertEquals("No recharge must be registered", 0, bike.getChargesCount());
        assertEquals("The battery must not change", 5, bike.getBatteryLevel());
        assertEquals("No dock must be recorded", 0, bike.getAllDocks().size());
    }

    /**
     * Test that nothing happens when the recharging location does not belong to
     * any station of the company.
     */
    @Test
    public void testRechargeWithoutStation()
    {
        ElectricBike bike = createBike(new Location(10, 11), new Location(10, 13), BATTERY_CAPACITY);
        bike.setBatteryLevel(5);
        bike.setRechargingLocation(new Location(0, 0));

        bike.recharge();

        assertEquals("No recharge must be registered", 0, bike.getChargesCount());
        assertEquals("The battery must not change", 5, bike.getBatteryLevel());
    }

    /**
     * Test that a step moves the bike one location towards its target and spends
     * the energy of one movement.
     */
    @Test
    public void testActMovesTowardsTheTarget()
    {
        ElectricBike bike = createBike(new Location(10, 13), new Location(10, 11), BATTERY_CAPACITY);
        bike.calculateRoute();

        bike.act(1);

        assertEquals("The bike must move one step towards the target", new Location(10, 12), bike.getLocation());
        assertEquals("The movement must spend 5wh", BATTERY_CAPACITY - MOVING_COST, bike.getBatteryLevel());
        assertEquals("The bike must not be idle", 0, bike.getIdleCount());
        assertEquals("The bike has not arrived yet", -1, bike.getArrivalSteps());
    }

    /**
     * Test that the arrival step is recorded when the bike reaches its final
     * target.
     */
    @Test
    public void testActArrivesAtTheTarget()
    {
        ElectricBike bike = createBike(new Location(10, 13), new Location(10, 11), BATTERY_CAPACITY);
        bike.calculateRoute();

        bike.act(1);
        bike.act(2);

        assertEquals("The bike must be at its target", new Location(10, 11), bike.getLocation());
        assertEquals("The arrival step must be recorded", 2, bike.getArrivalSteps());
        int expectedLevel = BATTERY_CAPACITY - 2 * MOVING_COST;
        assertEquals("Two movements must spend 10wh", expectedLevel, bike.getBatteryLevel());
    }

    /**
     * Test that a bike that does not have enough battery to reach its objective
     * stays where it is and increases its idle counter.
     */
    @Test
    public void testActStaysIdleWhenTheBatteryIsNotEnough()
    {
        ElectricBike bike = createBike(new Location(0, 0), new Location(19, 19), 5);
        bike.calculateRoute();

        bike.act(1);

        assertEquals("The bike must not move", new Location(0, 0), bike.getLocation());
        assertEquals("The battery must not change", 5, bike.getBatteryLevel());
        assertEquals("The idle counter must be increased", 1, bike.getIdleCount());
    }

    /**
     * Test that a bike that is already standing on its objective stays where it is
     * and increases its idle counter.
     */
    @Test
    public void testActStaysIdleWhenItIsAtTheTarget()
    {
        ElectricBike bike = createBike(new Location(10, 11), new Location(10, 11), BATTERY_CAPACITY);
        bike.calculateRoute();

        bike.act(1);

        assertEquals("The bike must not move", new Location(10, 11), bike.getLocation());
        assertEquals("The battery must not change", BATTERY_CAPACITY, bike.getBatteryLevel());
        assertEquals("The idle counter must be increased", 1, bike.getIdleCount());
    }

    /**
     * Test that the bike recharges when it reaches a recharging location and that
     * the route is calculated again afterwards.
     */
    @Test
    public void testActRechargesAtTheStation()
    {
        ElectricBike bike = createBike(new Location(10, 12), new Location(1, 1), BATTERY_CAPACITY);
        bike.calculateRoute();
        assertEquals("The nearest station must be planned", new Location(10, 11), bike.getRechargingLocation());

        bike.act(1);

        assertEquals("The bike must be at the station", new Location(10, 11), bike.getLocation());
        assertEquals("The recharge must be registered", 1, bike.getChargesCount());
        assertEquals("The energy charged after the movement must be paid", 4.0f, bike.getChargestCost(), 0.001f);
        assertEquals("The battery must be filled", BATTERY_CAPACITY, bike.getBatteryLevel());
        assertEquals("The bike must not arrive at the target yet", -1, bike.getArrivalSteps());
        assertEquals("The route must be recalculated", new Location(10, 5), bike.getRechargingLocation());
    }

    /**
     * Test that the battery level never goes below zero.
     */
    @Test
    public void testReduceBatteryLevel()
    {
        ElectricBike bike = createBike(new Location(10, 13), new Location(10, 11), BATTERY_CAPACITY);

        bike.reduceBatteryLevel();
        assertEquals("One movement must spend 5wh", BATTERY_CAPACITY - MOVING_COST, bike.getBatteryLevel());

        bike.setBatteryLevel(3);
        bike.reduceBatteryLevel();
        assertEquals("The battery level must not be negative", 0, bike.getBatteryLevel());
    }

    /**
     * Test that the number of recharges can be increased.
     */
    @Test
    public void testIncrementCharges()
    {
        ElectricBike bike = createBike(new Location(10, 12), new Location(10, 11), BATTERY_CAPACITY);

        bike.incrementCharges();
        bike.incrementCharges();

        assertEquals("Both increments must be counted", 2, bike.getChargesCount());
    }

    /**
     * Test that the cost of the recharges is accumulated.
     */
    @Test
    public void testIncrementChargesCost()
    {
        ElectricBike bike = createBike(new Location(10, 12), new Location(10, 11), BATTERY_CAPACITY);

        bike.incrementChargesCost(3.5f);
        bike.incrementChargesCost(1.5f);

        assertEquals("The costs must be accumulated", 5.0f, bike.getChargestCost(), 0.001f);
    }

    /**
     * Test that a dock used by the bike is recorded.
     */
    @Test
    public void testAddDock()
    {
        ElectricBike bike = createBike(new Location(10, 12), new Location(10, 11), BATTERY_CAPACITY);
        Dock dock = new Dock("CC00_003", 80, 0.8f);

        bike.addDock(dock);

        assertEquals("The dock must be recorded", 1, bike.getAllDocks().size());
        assertSame("The recorded dock must be the one that was added", dock, bike.getAllDocks().get(0));
    }

    /**
     * Test that the list of docks returned by the bike is a copy.
     */
    @Test
    public void testGetAllDocksReturnsACopy()
    {
        ElectricBike bike = createBike(new Location(10, 12), new Location(10, 11), BATTERY_CAPACITY);
        bike.addDock(new Dock("CC00_003", 80, 0.8f));

        bike.getAllDocks().clear();

        assertEquals("The bike must not be affected by changes in the returned list", 1, bike.getAllDocks().size());
    }

    /**
     * Test the distance between the current location and the final target.
     */
    @Test
    public void testDistanceToTheTargetLocation()
    {
        ElectricBike bike = createBike(new Location(10, 13), new Location(1, 1), BATTERY_CAPACITY);

        assertEquals("The distance must be the Chebyshev one", 12, bike.distanceToTheTargetLocation());
    }

    /**
     * Test the text of the route when no recharge is planned.
     */
    @Test
    public void testGetStringRouteWithoutRecharge()
    {
        ElectricBike bike = createBike(new Location(10, 13), new Location(10, 11), BATTERY_CAPACITY);
        bike.calculateRoute();

        assertEquals("The route must show both locations", "10-13 -> 10-11", bike.getStringRoute());
    }

    /**
     * Test the text of the route when a recharge is planned.
     */
    @Test
    public void testGetStringRouteWithRecharge()
    {
        ElectricBike bike = createBike(new Location(10, 13), new Location(1, 1), BATTERY_CAPACITY);
        bike.calculateRoute();

        assertEquals("The route must include the stop", "10-13 -> 10-11 -> 1-1", bike.getStringRoute());
    }

    /**
     * Test the string representation of a bike that has a planned recharge.
     */
    @Test
    public void testToStringWithRecharge()
    {
        ElectricBike bike = createBike(new Location(10, 13), new Location(1, 1), BATTERY_CAPACITY);
        bike.calculateRoute();

        String expected = "ElectricBike: EB0, 30wh, 30wh, 0, 0.0€, 0, 10-13, 10-11, 1-1";
        assertEquals("The bike must show the planned recharge", expected, bike.toString());
    }

    /**
     * Test the string representation of a bike that has no planned recharge.
     */
    @Test
    public void testToStringWithoutRecharge()
    {
        ElectricBike bike = createBike(new Location(10, 13), new Location(10, 11), BATTERY_CAPACITY);
        bike.calculateRoute();

        String expected = "ElectricBike: EB0, 30wh, 30wh, 0, 0.0€, 0, 10-13, 10-11";
        assertEquals("The bike must not show a recharge", expected, bike.toString());
    }

    /**
     * Test the final representation of a bike, which never includes the
     * recharging location.
     */
    @Test
    public void testToStringFinal()
    {
        ElectricBike bike = createBike(new Location(10, 13), new Location(1, 1), BATTERY_CAPACITY);
        bike.calculateRoute();

        String expected = "ElectricBike: EB0, 30wh, 30wh, 0, 0.0€, 0, 10-13, 1-1";
        assertEquals("The final text must have eight fields", expected, bike.toStringFinal());
    }

    /**
     * Test the message shown when the bike reaches its final destination.
     */
    @Test
    public void testToStringArrival()
    {
        ElectricBike bike = createBike(new Location(10, 13), new Location(1, 1), BATTERY_CAPACITY);

        String expected = "ElectricBike: EB0 at target destination ********";
        assertEquals("The arrival message must be the expected one", expected, bike.toStringArrival());
    }

    /**
     * Test the message shown when the bike recharges at a dock.
     */
    @Test
    public void testToStringRecharge()
    {
        ElectricBike bike = createBike(new Location(10, 13), new Location(1, 1), BATTERY_CAPACITY);

        String expected = "ElectricBike: EB0 recharges: 25wh at dock: CC01_003 with cost: 20.0€ ********";
        String message = bike.toStringRecharge("CC01_003", 25, 20.0f);
        assertEquals("The recharge message must be the expected one", expected, message);
    }

    /**
     * Test that a bike cannot be created with a null company, a null location or
     * a null target location.
     */
    @Test
    public void testConstructorWithNullArguments()
    {
        Location location = new Location(0, 0);
        Location target = new Location(1, 1);

        try {
            new ElectricBike(null, location, target, "EB0", BATTERY_CAPACITY);
            fail("A null company must not be accepted");
        }
        catch (NullPointerException e) {
            // expected
        }

        try {
            new ElectricBike(company, null, target, "EB0", BATTERY_CAPACITY);
            fail("A null location must not be accepted");
        }
        catch (NullPointerException e) {
            // expected
        }

        try {
            new ElectricBike(company, location, null, "EB0", BATTERY_CAPACITY);
            fail("A null target location must not be accepted");
        }
        catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Creates a bike that belongs to the company of the fixture.
     * @param location The starting location of the bike.
     * @param target The final target location of the bike.
     * @param capacity The capacity of the battery, in watt-hours.
     * @return A new bike with a full battery.
     */
    private ElectricBike createBike(Location location, Location target, int capacity)
    {
        return new ElectricBike(company, location, target, "EB0", capacity);
    }

    /**
     * Installs four docks in the given station, with a speed and a fee that grow
     * with their index, as the demo does.
     * @param station The station where the docks are installed.
     */
    private void addDocks(BikeStation station)
    {
        for (int i = 0; i < 4; i++) {
            station.addDock(new Dock(station.getId() + "_00" + i, (i + 1) * 20, (i + 1) * 0.20f));
        }
    }
}
