import java.util.*;
/**
 * Provides a simple demonstration and simulation environment for the
 * electric bike (EB) and bike station model.
 * It sets up the company, the bikes, the stations and the docks, runs the
 * simulation for a fixed number of steps and shows the initial and final state of
 * the system.
 *
 * Scenarios, selected with the {@code DEMO} constant:
 * <ul>
 * <li>SIMPLE ({@link DemoType#SIMPLE}): two bikes are created.</li>
 * <li>MEDIUM ({@link DemoType#MEDIUM}): five bikes are created.</li>
 * <li>ADVANCED ({@link DemoType#ADVANCED}): eight bikes are created.</li>
 * </ul>
 * In every scenario four stations are created, each one with four docks.
 *
 * @author DP classes
 * @version 2026
 */
public class EBikeDemo
{
    
    /** The maximum X coordinate for the grid (number of rows). */
    public static final int MAX_X = 20; 
    /** The maximum Y coordinate for the grid (number of columns). */
    public static final int MAX_Y = 20; 
    
    /** The maximum number of turns. */
    public static final int MAX_STEPS = 50;
    
    /** The company managing the EBs and Bike stations. */
    private EBCompany company;
    
    /** The bikes of the simulation; they are the electric bikes of the company.
     * @see ElectricBike
     */
    private List<ElectricBike> bikes; 
    
    /** The bike stations available in the city.
     * @see BikeStation
     */
    private List<BikeStation> stations;
    
    /** Constant for selecting the demo scenario, using the {@link DemoType} enumeration. */
    private static final DemoType DEMO=DemoType.SIMPLE;
    
    /** Name of the company that manages the fleet of bikes and the stations. */
    private static final String COMPANY_NAME = "EBBike Cáceres";
    
    /**
     * Creates the demo. It creates the company with the name given by
     * {@link #COMPANY_NAME} and the lists of bikes and stations, and then calls
     * {@link #reset()} to create the elements of the scenario selected by the
     * {@code DEMO} constant.
     */
    public EBikeDemo()
    {
        this.company = new EBCompany(COMPANY_NAME);
        this.bikes = new ArrayList<>();
        this.stations = new ArrayList<>();
        reset();
    }

    /**
     * Runs the whole demo: shows the initial information, performs
     * {@link #MAX_STEPS} simulation steps and shows the final information.
     */
    public void run()
    {        
        showInitialInfo();
        
        for(int step = 0; step < MAX_STEPS; step++) {
            step(step);
        }
        
        showFinalInfo();
    }

    /**
     * Runs one step of the simulation by requesting all the bikes to act.
     * Before each bike acts, its state is saved; afterwards the new state is
     * compared with the old one to detect a recharge (the recharge counter has
     * increased) or an arrival (the arrival step has changed from -1 to a number),
     * and the corresponding line is shown. The state of the bike after acting is
     * always shown as well.
     * @param step The current step number of the simulation.
     */
    public void step(int step)
    {
        for (int i = 0; i < this.bikes.size(); i++){
            ElectricBike eb = this.bikes.get(i); 
            int chargesBefore = eb.getChargesCount();
            int arrivalBefore = eb.getArrivalSteps();
            float wsBefore    = eb.getWsCharged();
            float costBefore  = eb.getChargestCost();
    
            eb.act(step);
    
            if (eb.getChargesCount() > chargesBefore) {
                List<Dock> ds = eb.getAllDocks();
                Dock last = ds.get(ds.size() - 1);          
                showStep(step, eb.toStringRecharge(last.getId(), Math.round(eb.getWsCharged() - wsBefore), eb.getChargestCost() - costBefore));
            }
                
            if (arrivalBefore == -1 && eb.getArrivalSteps() != -1) {
                showStep(step, eb.toStringArrival());
            }
    
            showStep(step, eb.toString());
        }
    }

    /**
     * Resets the demo to its starting point. It clears the bikes and the stations,
     * resets the company and creates again the bikes, stations, docks and routes
     * of the scenario selected by the {@code DEMO} constant.
     */
    public void reset()
    {
        bikes.clear();
        stations.clear();
        this.company.reset();
        createElectricBikes();
        createStations(); 
        createDocks();
        configureRoutes();
    }

    
    /**
     * Creates the {@link ElectricBike}s of the scenario, assigns them their starting
     * and target {@link Location}s, and adds them both to the company and to the
     * local list of bikes. The number of bikes is given by
     * {@link DemoType#getNumBikesToCreate()}.
     */
    private void createElectricBikes() 
    {
        Location [] locations = {new Location(10,13), new Location(8,4), new Location(8,4), new Location(15,10), 
                                new Location(1,1), new Location(2,2), new Location(11,13), new Location(14,16)};
        Location [] targetLocations = {new Location(1,1), new Location(19,19), new Location(12,17), new Location(4,4), 
                                        new Location(1,10), new Location(5,5), new Location(8,7), new Location(19,19)};
                                        
        for (int i=0;i < DEMO.getNumBikesToCreate();i++){
            ElectricBike eb = new ElectricBike(company, locations[i],targetLocations[i],("EBCC"+i),(i+1)*15);
            company.addElectricBike(eb);
            this.bikes.add(eb);
        }
    }
    

    /**
     * Creates the predefined {@link BikeStation}s of the scenario and adds them
     * both to the company and to the local list of stations. The number of stations
     * is given by {@link DemoType#getNumStationsToCreate()}.
     */
    private void createStations() 
    {  
        Location [] locations = {new Location(10,5), new Location(10,11), new Location(14,16), new Location(8,4)};
                                
        for (int i=0;i<DEMO.getNumStationsToCreate();i++){
            BikeStation bs = new BikeStation("CC0" + i,locations[i]);
            company.addBikeStation(bs);
            this.stations.add(bs);
        }
    }

    /**
     * Creates the {@link Dock} units of every {@link BikeStation}. The identifier of
     * each dock is the station identifier followed by {@code "_00"} and its index,
     * and its charging speed and fee grow with that index. The number of docks per
     * station is given by {@link DemoType#getNumDocksToCreate()}; the station keeps
     * them ordered.
     */
    private void createDocks() 
    {  
        for (BikeStation station : stations){
            for (int i=0;i<DEMO.getNumDocksToCreate();i++){
                Dock d = new Dock(station.getId() + "_00" + i,((i+1)*20),((i+1)*0.20f));
                station.addDock(d);
            }
        }    
    }
    
    
     /**
      * Instructs each {@link ElectricBike} to calculate its initial route, which
      * determines whether an intermediate recharging stop is necessary.
      */
     private void configureRoutes() 
     {
        for (int i = 0; i < this.bikes.size(); i++){
            this.bikes.get(i).calculateRoute();
        }
     }

    /**
     * Displays the initial information of the simulation: the name of the company,
     * the state of every {@link ElectricBike} and, for every {@link BikeStation},
     * the station and its docks.
     */
    private void showInitialInfo() 
    {
        System.out.println("( Compañía " + this.company.getName() + " )");
        
        System.out.println("(-------------------)");
        System.out.println("( Electric Bikes )");
        System.out.println("(-------------------)");

        for (int i = 0; i < this.bikes.size(); i++)
            System.out.println("(" + this.bikes.get(i).toString() + ")");
        
        System.out.println("(-------------------)");
        System.out.println("( Bike Stations )");
        System.out.println("(-------------------)");
       
        for (int i = 0; i < this.stations.size(); i++){
            System.out.println("(" + this.stations.get(i).toString() + ")");
            List<Dock> ds = this.stations.get(i).getAllDocks();
            for (int j = 0; j < ds.size(); j++)
                System.out.println("(" + ds.get(j).toString() + ")");
        }

        System.out.println("(------------------)");
        System.out.println("( Simulation start )");
        System.out.println("(------------------)");
    }

    /**
     * Displays one line of the simulation of a step.
     * @param n The step number.
     * @param s The text that describes what happened to a bike in that step.
     */
    private void showStep(int n, String s) 
    {
        System.out.println("(step: " + n + " - " + s + ")");
    }
    
    /**
     * Displays the final information after the simulation has run.
     * The bikes are shown ordered by arrival step, with the bikes that did not
     * arrive last, and by identifier as a tie-breaker; under each bike, the
     * identifiers of the docks where it recharged. The stations are shown ordered
     * by number of recharges (descending) and by identifier as a tie-breaker, with
     * each dock and, under it, the bikes recharged there.
     * The lists are copied before sorting, so the state of the demo is not altered.
     * @see ComparatorEBArrivalStep
     * @see ComparatorEBIdBike
     * @see ComparatorBikeStationNumberRecharged
     * @see ComparatorBikeStationId
     */
    private void showFinalInfo() 
    {
 
        System.out.println("(-------------------)");
        System.out.println("( Final information )");        
        System.out.println("(-------------------)");
        
        System.out.println("(-------------------)");
        System.out.println("( Electric Bikes )");
        System.out.println("(-------------------)");
        
        List<ElectricBike> sb = new ArrayList<>(this.bikes);
        Collections.sort(sb, new ComparatorEBIdBike());
        Collections.sort(sb, new ComparatorEBArrivalStep());

        for (int i = 0; i < sb.size(); i++){
            System.out.println("(" + sb.get(i).toStringFinal() + ")");
            List<Dock> ds = sb.get(i).getAllDocks();
            for (int j = 0; j < ds.size(); j++)
                System.out.println("(Dock: " + ds.get(j).getId() + ")");
        }
        
        System.out.println("(-------------------)");
        System.out.println("( Bike Stations )");
        System.out.println("(-------------------)");
        
        List<BikeStation> sbs = new ArrayList<>(this.stations);
        Collections.sort(sbs, new ComparatorBikeStationId());
        Collections.sort(sbs, new ComparatorBikeStationNumberRecharged());
        
        for (int i = 0; i < sbs.size(); i++){
            System.out.println("(" + sbs.get(i).toString() + ")");
            List<Dock> ds = sbs.get(i).getAllDocks();
            for (int j = 0; j < ds.size(); j++){
                System.out.println("(" + ds.get(j).toString() + ")");
                List<ElectricBike> eb = ds.get(j).getAllBikesRecharged();
                for (int k = 0; k < eb.size(); k++)
                System.out.println("(" + eb.get(k).toStringFinal() + ")");
            }
        }
    }
    
    
    /**
     * The main entry point for running the EBikeDemo simulation.
     * Creates an instance of {@code EBikeDemo} and starts the simulation.
     */
    public static void main() 
    {
        EBikeDemo demo = new EBikeDemo();
        demo.run();
    }
}
