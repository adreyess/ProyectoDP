import java.util.*;
/**
 * Provides a simple demonstration and simulation environment for the 
 * Electric Bike (EB) and Bike Station model.
 * It sets up the environment and runs the simulation for a fixed number of steps.
 *
 * Scenarios: 
 * <ul>
 * <li>Demo SIMPLE (demo=DemoType.SIMPLE): Two bikes are created.</li>
 * <li>Demo MEDIUM (demo=DemoType.MEDIUM): Five bikes are created.</li>
 * <li>Demo ADVANCED(demo=DemoType.ADVANCED): Eight bikes are created.</li>
 * </ul>
 * * @author DP Clasess
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
    
    /** * The simulation's bikes; they are the electric bikes of the company. 
     * @see ElectricBike 
     */
    private List<ElectricBike> bikes; 
    
    /** * The bike stations available in the city. 
     * @see BikeStation 
     */
    private List<BikeStation> stations;
    
    /** Constant for selecting the demo scenario, using the {@link DemoType} enumeration. */
    private static final DemoType DEMO=DemoType.SIMPLE;
    
        
    /**
     * Constructor for objects of class EBikeDemo.
     * Initializes the company, bikes, and stations lists, and sets the number of 
     * bikes based on the {@code DEMO} constant before calling {@code reset()}.
     */
    public EBikeDemo()
    {
        this.company = new EBCompany("EBBike Cáceres");
        this.bikes = new ArrayList<>();
        this.stations = new ArrayList<>();
        reset();
    }

    /**
     * Run the demo for a fixed number of steps (50 steps).
     * In each step, all bikes perform their action.
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
     * Run the demo for one step by requesting all bikes to act.
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
     * Reset the demo to a starting point.
     * Clears existing bikes and stations, resets the company, and re-creates
     * the initial setup.
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
     * Creates the {@link ElectricBike}s based on {@code numBikesCreated}, assigns them
     * starting and target {@link Location}s, and adds them to the company.
     * The bikes list is sorted by plate using {@link ComparatorEBPlate}.
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
     * Creates predefined {@link BikeStation}s and adds them to the company.
     * The stations list is sorted by ID using {@link ComparatorBikeStationId}.
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
     * Creates a fixed number of {@link Dock} units for each {@link BikeStation}
     * and orders the Docks within each station.
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
      * Instructs each {@link ElectricBike} to calculate its initial route.
      * This determines if an intermediate recharging stop is necessary.
      */
     private void configureRoutes() 
     {
        for (int i = 0; i < this.bikes.size(); i++){
            this.bikes.get(i).calculateRoute();
        }
     }

    /**
     * Displays the initial information for the simulation, including 
     * the details of all {@link ElectricBike}s and {@link BikeStation}s.
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
     * Displays the final information after the simulation has run.
     * bikes are sorted by arrival step using {@link ComparatorEBArrivingStep}.
     * Stations are sorted by the number of recharges using {@link ComparatorBikeStationNumerRecharged}.
     */
    private void showStep(int n, String s) 
    {
        System.out.println("(step: " + n + " - " + s + ")");
    }
    
    /**
     * Displays the final information after the simulation has run.
     * bikes are sorted by arrival step using {@link ComparatorEBArrivingStep}.
     * Stations are sorted by the number of recharges using {@link ComparatorBikeStationNumerRecharged}.
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
                List<ElectricBike> eb = ds.get(i).getAllBikesRecharged();
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
