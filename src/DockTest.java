import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test implementation of the {@link Dock} class.
 * Provides unit tests for the recharge process, the registration of the bikes
 * recharged and the money collected, the availability of the dock and the string
 * representation.
 *
 * @author DP classes
 * @version 2026.11.13
 */
public class DockTest
{
    /** Capacity of the battery of the bikes used in the tests, in watt-hours. */
    private static final int BATTERY_CAPACITY = 30;
    /** Charging fee of the dock used in most of the tests, in euros per watt-hour. */
    private static final float CHARGING_FEE = 0.8f;

    /** The company of the bike used in the tests. */
    private EBCompany company;
    /** The bike that is going to be recharged. */
    private ElectricBike bike;
    /** The dock under test. */
    private Dock dock;

    /**
     * Default constructor for test class DockTest.
     */
    public DockTest()
    {
    }

    /**
     * Sets up the test fixture: a company, a bike with a full battery and a free
     * dock with a known speed and fee.
     */
    @Before
    public void setUp()
    {
        company = new EBCompany("Test company");
        Location location = new Location(10, 10);
        Location target = new Location(10, 13);
        bike = new ElectricBike(company, location, target, "EB0", BATTERY_CAPACITY);
        dock = new Dock("CC00_003", 80, CHARGING_FEE);
    }

    /**
     * Tears down the test fixture.
     */
    @After
    public void tearDown()
    {
        company = null;
        bike = null;
        dock = null;
    }

    /**
     * Test the recharge of a bike whose battery is not full: the dock must fill
     * the battery, return the cost of the recharge, register the bike and add the
     * money collected.
     */
    @Test
    public void testRecharge()
    {
        bike.setBatteryLevel(10);

        float cost = dock.recharge(bike);

        assertEquals("The cost is the energy charged multiplied by the fee", 16.0f, cost, 0.001f);
        assertEquals("The battery must be filled up to its capacity", BATTERY_CAPACITY, bike.getBatteryLevel());
        assertEquals("The bike must keep track of the energy charged", 20.0f, bike.getWsCharged(), 0.001f);
        assertEquals("The bike must be registered in the dock", 1, dock.getNumberEBRecharged());
        assertEquals("The money collected must be updated", 16.0f, dock.getAmountCollected(), 0.001f);
        assertSame("The registered bike must be the recharged one", bike, dock.getAllBikesRecharged().get(0));
    }

    /**
     * Test that a bike with a full battery is not charged and does not pay
     * anything.
     */
    @Test
    public void testRechargeWithFullBattery()
    {
        float cost = dock.recharge(bike);

        assertEquals("A full battery must not cost anything", 0.0f, cost, 0.001f);
        assertEquals("A bike that does not need energy must not be registered", 0, dock.getNumberEBRecharged());
        assertEquals("No money must be collected", 0.0f, dock.getAmountCollected(), 0.001f);
    }

    /**
     * Test that several recharges in the same dock are accumulated, both in the
     * number of bikes registered and in the money collected.
     */
    @Test
    public void testSeveralRechargesInTheSameDock()
    {
        bike.setBatteryLevel(0);
        dock.recharge(bike);

        bike.setBatteryLevel(15);
        dock.recharge(bike);

        assertEquals("Both recharges must be registered", 2, dock.getNumberEBRecharged());
        assertEquals("The money collected must be accumulated", 36.0f, dock.getAmountCollected(), 0.001f);
    }

    /**
     * Test that a bike can be registered in a dock directly.
     */
    @Test
    public void testAddBikeRecharged()
    {
        dock.addBikeRecharged(bike);

        assertEquals("The bike must be registered", 1, dock.getNumberEBRecharged());
        assertSame("The registered bike must be the one that was added", bike, dock.getAllBikesRecharged().get(0));
    }

    /**
     * Test that the list of recharged bikes returned by the dock is a copy, so
     * modifying it does not change the dock.
     */
    @Test
    public void testGetAllBikesRechargedReturnsACopy()
    {
        dock.addBikeRecharged(bike);

        dock.getAllBikesRecharged().clear();

        assertEquals("The dock must not be affected by changes in the returned list", 1, dock.getNumberEBRecharged());
    }

    /**
     * Test that an amount of money can be added to the total collected.
     */
    @Test
    public void testUpdateAmountCollected()
    {
        dock.updateAmountCollected(2.5f);
        dock.updateAmountCollected(1.5f);

        assertEquals("The amounts must be accumulated", 4.0f, dock.getAmountCollected(), 0.001f);
    }

    /**
     * Test that a new dock is free and that its state can be changed.
     */
    @Test
    public void testFreeState()
    {
        assertTrue("A new dock must be free", dock.isFree());

        dock.setFree(false);
        assertFalse("The dock must be busy after setting it busy", dock.isFree());

        dock.setFree(true);
        assertTrue("The dock must be free after setting it free", dock.isFree());
    }

    /**
     * Test the getters and setters of the identifier, the charging speed and the
     * charging fee of the dock.
     */
    @Test
    public void testSettersAndGetters()
    {
        dock.setId("CC01_000");
        dock.setChargingSpeed(20);
        dock.setChargingFee(0.2f);
        dock.setAmountCollected(3.0f);

        assertEquals("The identifier must be the new one", "CC01_000", dock.getId());
        assertEquals("The speed must be the new one", 20, dock.getChargingSpeed());
        assertEquals("The fee must be the new one", 0.2f, dock.getChargingFee(), 0.001f);
        assertEquals("The amount collected must be the new one", 3.0f, dock.getAmountCollected(), 0.001f);
    }

    /**
     * Test the string representation of the dock.
     */
    @Test
    public void testToString()
    {
        bike.setBatteryLevel(10);
        dock.recharge(bike);

        String expected = "Dock: CC00_003, 80wh, 0.8€, 1, 16.0€";
        assertEquals("The dock must show all its information", expected, dock.toString());
    }

    /**
     * Test that a dock cannot be created with a null identifier.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullId()
    {
        new Dock(null, 80, CHARGING_FEE);
    }
}
