package com.example.ray.carbontracker_flame.Model;

import android.content.Context;
import android.util.Log;

import com.example.ray.carbontracker_flame.App;
import com.example.ray.carbontracker_flame.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * The CarbonTrackerModel class holds the apps model and data
 */
public class CarbonTrackerModel {
    public static final int INITIALIZE_VALUE = -1;
    private static final String MODEL_TAG = "CarbonTrackerModel";
    private static final int TOTAL_NUMBER_OF_CAR_MAKE = 133;
    private static CarbonTrackerModel instance = new CarbonTrackerModel();
    public final UtilityBillCollection utilityBillCollection;
    public final JourneyCollection journeyCollection;
    public final CarCollection carCollection;
    private final RouteCollection routeCollection;
    private final List<CarData> carDataList;
    private String[] carMakes;
    private String[][] carModels;
    private String[][][] carYears;
    private List<CarData> carsChosenList;
    private int[] makeMarked;
    private int counterJourneyId;
    private int counterBillId;
    private boolean dataLoaded = false;
    private List<Tip> tips;
    private boolean isUsingRelatableUnit = true;
    private DBAdapter dbAdapter;
    private Context applicationContext = App.getInstance();

    private CarbonTrackerModel() {
        carDataList = new ArrayList<>();
        carCollection = new CarCollection();
        routeCollection = new RouteCollection();
        journeyCollection = new JourneyCollection();
        carsChosenList = new LinkedList<>();
        utilityBillCollection = new UtilityBillCollection();
        tips = new ArrayList<>();
        carMakes = new String[TOTAL_NUMBER_OF_CAR_MAKE];
        makeMarked = new int[TOTAL_NUMBER_OF_CAR_MAKE];
        // applicationContext = App.getInstance();
    }

    public static CarbonTrackerModel getInstance() {
        return instance;
    }


    public List<CarData> getCarsChosenList() {
        return carsChosenList;
    }

    public UtilityBillCollection getUtilityBillCollection() {
        return utilityBillCollection;
    }

    public List<Tip> getTipsSaved() {
        return tips;
    }

    public String[] getCarMakes() {
        return carMakes;
    }

    public void loadCarDataFromDisk(Context context) {
        if (!dataLoaded) {
            carDataList.clear();

            InputStream inputStream = context.getResources().openRawResource(R.raw.car_data);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"))
            );
            String currentLine = "";
            try {
                //skip header line
                reader.readLine();
                //read and store car data from the csv file
                boolean lineHasNoEmptyTokens;
                while ((currentLine = reader.readLine()) != null) {
                    String[] tokens = currentLine.split(",");
                    lineHasNoEmptyTokens = true;

                    for (String token : tokens) {
                        if (token.length() < 1) {
                            lineHasNoEmptyTokens = false;
                        }
                    }
                    if (lineHasNoEmptyTokens) {
                        String make = tokens[0];
                        String model = tokens[1];
                        int year = Integer.parseInt(tokens[2]);

                        //Data has NA for some some records, default to 0
                        double displacementInLiters = 0;

                        try {
                            displacementInLiters = Double.parseDouble(tokens[3]);
                        } catch (Exception e) {
                            Log.wtf("Parsing error", currentLine);
                        }
                        String transmission = tokens[4];
                        int cityMPG = Integer.parseInt(tokens[5]);
                        int highwayMPG = Integer.parseInt(tokens[6]);
                        String fuelType = tokens[7];

                        carDataList.add(
                                new CarData(make, model, year, displacementInLiters,
                                        transmission, cityMPG, highwayMPG, fuelType)
                        );
                    } else {
                        Log.i("Line had empty fields", currentLine);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.wtf(MODEL_TAG, "CarData csv failed to read" + currentLine, e);
            }
            dataLoaded = true;
        }
        populateCarMakes(context);
    }

    private void populateCarMakes(Context context) {
        InputStream inputStream = context.getResources().openRawResource(R.raw.car_make);
        BufferedReader reader_carmake = new BufferedReader(
                new InputStreamReader(inputStream, Charset.forName("UTF-8"))
        );
        String currLine = "";
        int counter = 0;
        try {
            //skip header line
            reader_carmake.readLine();
            //read and store car data from the csv file
            while ((currLine = reader_carmake.readLine()) != null) {
                String[] tokens = currLine.split(",");
                carMakes[counter] = tokens[0];
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.wtf(MODEL_TAG, "CarMakes csv failed to read" + currLine, e);
        }
        populateMakeAndModelArray();
    }

    private void populateMakeAndModelArray() {
        int counterModel = 0;
        carModels = new String[carMakes.length][];
        for (int ii = 0; ((counterModel) < carDataList.size()); ii++) {
            int counterTemp = 0;
            int start_Model = counterModel;
            makeMarked[ii] = counterModel;

            while ((counterModel < carDataList.size()) && carMakes[ii].equals(carDataList.get(counterModel).getMake())) {
                counterTemp++;
                counterModel++;
            }
            carModels[ii] = new String[counterTemp]; // Create the Double array to store the model.
            int start = 0;
            while (start < counterTemp) {
                carModels[ii][start] = (carDataList.get(start_Model).getModel());
                start++;
                start_Model++;
            }
            removeDuplicateCarModels(ii);
        }
        populateMakeModelYearArray();
    }

    private void populateMakeModelYearArray() {
        // initialization the array to store MakeModelYear in one 3-D array.
        carYears = new String[carMakes.length][][];
        // fill the carModels
        for (int ii = 0; ii < carMakes.length; ii++) {
            carYears[ii] = new String[carModels[ii].length][];
        }
        int counterData = 0;
        for (int ii = 0; ((counterData) < carDataList.size()); ii++) {
            int startCounterData = counterData;
            for (int jj = 0; jj < carModels[ii].length; jj++) {
                int countYears = 0;
                while ((counterData < carDataList.size())
                        && carMakes[ii].equals(carDataList.get(counterData).getMake())
                        && carModels[ii][jj].equals(carDataList.get(counterData).getModel())) {
                    countYears++;
                    counterData++;
                }
                carYears[ii][jj] = new String[countYears];
                int start = 0;
                while (start < countYears) {
                    carYears[ii][jj][start] = Integer.toString(carDataList.get(startCounterData).getYear());
                    start++;
                    startCounterData++;
                }
                removeDuplicateYears(ii, jj);
            }
        }
    }

    private void removeDuplicateCarModels(int indexMake) {
        int numberOfUniqueElements = 0;
        int arrayLength = carModels[indexMake].length;
        for (int ii = 0; ii < arrayLength; ii++) {
            numberOfUniqueElements++;
            String tempString = carModels[indexMake][ii];
            while (((ii + 1) < arrayLength) && tempString.equals(carModels[indexMake][ii + 1])) {
                ii++;
            }
        }
        String[] temp = new String[numberOfUniqueElements];
        temp[0] = carModels[indexMake][0];
        int xx = 0;
        int yy;
        for (yy = 0; yy < arrayLength; yy++) {
            if (!(temp[xx].equals(carModels[indexMake][yy]))) {
                xx++;
                temp[xx] = carModels[indexMake][yy];
            }
        }
        carModels[indexMake] = temp;
    }

    private void removeDuplicateYears(int indexMake, int indexModel) {
        int numberOfUniqueElements = 0;
        int arrayLength = carYears[indexMake][indexModel].length;
        for (int ii = 0; ii < arrayLength; ii++) {
            numberOfUniqueElements++;
            String tempString = carYears[indexMake][indexModel][ii];
            while (((ii + 1) < arrayLength)
                    && tempString.equals(carYears[indexMake][indexModel][ii + 1])) {
                ii++;
            }
        }
        String[] tempYearArray = new String[numberOfUniqueElements];
        tempYearArray[0] = carYears[indexMake][indexModel][0];
        int xx = 0;
        int yy;
        for (yy = 0; yy < arrayLength; yy++) {
            if (!(tempYearArray[xx].equals(carYears[indexMake][indexModel][yy]))) {
                xx++;
                tempYearArray[xx] = carYears[indexMake][indexModel][yy];
            }
        }
        carYears[indexMake][indexModel] = tempYearArray;
    }

    public void findSelectedOptionCars(String makeSelected,
                                       String modelSelected,
                                       String yearSelected) {
        // carsChosenList is made to create a list of chosen Cars from the UI interaction.
        carsChosenList = new ArrayList<>();
        //list of all Cars = carDataList
        int yearSelected2 = Integer.parseInt(yearSelected);
        int[] searchBoundary = findStartingEndingPointOfSearch(makeSelected);
        int start = searchBoundary[0];
        int end = searchBoundary[1];

        for (int ii = start; ii < end; ii++) {
            if (makeSelected.equals(carDataList.get(ii).getMake())
                    && modelSelected.equals(carDataList.get(ii).getModel())
                    && yearSelected2 == carDataList.get(ii).getYear()) {
                carsChosenList.add(carDataList.get(ii));
            }
            /* If we have found what we need, exit the program. If not continue.
            * As an example, if we have found 3 cars matched.
            * And the total number of the cars matched is exactly the occurence of that Car.
            * We quit the program.
            */
            else if (carsChosenList.size() > 0) {
                break;
            }
        }
    }

    /**
     * Create a temporary array to store the value of search
     * Search starting point started from The first occurence of the Car's Make.
     * Search ending point ended when The Car Make's name is changed.
     * For example: Searching for Acura, only search from the field when Make: Acura.
     * When it changed to the next one, save that index also to the end point.
     *
     * @param makeSelected is the Car Make which is chosen by the user.
     * @return the array[0] for the starting point, array[1] for the ending point.
     */
    private int[] findStartingEndingPointOfSearch(String makeSelected) {
        //initialization of the array
        int[] indexChosen = new int[2];
        indexChosen[0] = INITIALIZE_VALUE; // for the starting search point
        indexChosen[1] = INITIALIZE_VALUE; // for the ending search point

        for (int ii = 0; ii < carMakes.length; ii++) {
            if (makeSelected.equals(carMakes[ii])) {
                if ((ii + 1) == carMakes.length) {
                    indexChosen[0] = makeMarked[ii];
                    indexChosen[1] = carDataList.size();
                } else {
                    indexChosen[0] = makeMarked[ii];
                    indexChosen[1] = makeMarked[ii + 1];
                }
            }
        }
        return indexChosen;
    }

    public String[] getCarModelsFromMake(int make) {
        return carModels[make];
    }

    public String[] getCarYearsFromMakeModel(int make, int model) {
        return carYears[make][model];
    }

    public String[] getCarsChosenListDescription() {
        String[] carChosenDescription = new String[carsChosenList.size()];
        for (int ii = 0; ii < carsChosenList.size(); ii++) {
            carChosenDescription[ii] = carsChosenList.get(ii).getDescription();
        }
        return carChosenDescription;
    }

    public int getCounterForJourneyId() {
        return counterJourneyId;
    }

    public void setCounterForJourneyId(int lastJourneyCounter) {
        this.counterJourneyId = lastJourneyCounter;
    }

    public void addCounterForJourneyId() {
        this.counterJourneyId = counterJourneyId + 1;
    }

    public void resetCounterForJourneyId() {
        this.counterJourneyId = 1;
    }


    //update Tips!! need more conditions, constraints.
    public void updateTips() {
        tips = new ArrayList<>();
        addJourneyTips();
        addBillTips();

        Collections.sort(tips, new Comparator<Tip>() {
            @Override
            public int compare(Tip o1, Tip o2) {
                return o1.compareTo(o2);
            }
        });
    }

    private void addJourneyTips() {
        double todaysEmissions = 0;
        int todaysCarJourneys = 0;
        double totalCarEmissions = 0;
        int totalCarJourneys = 0;
        if (journeyCollection.getSavedJourneys().size() > 0) {
            double totalCityDistance = 0;
            double totalHwyDistance = 0;
            for (Journey journey : journeyCollection.getSavedJourneys()) {
                Transport curTransport = journey.getTransportTaken();
                Route curRoute = journey.getRouteTaken();
                final String thisJourneyGeneratesString = applicationContext.getString(R.string.this_journey_generates);
                if (curRoute.getNumOfCityKilometers() <= 2.0 && curTransport.getTransportType() != TRANSPORT_TYPES.WALKING_OR_CYCLING) {
                    tips.add(new Tip(journey.getDescription() + thisJourneyGeneratesString
                            + CarbonUnitPrinter.getConvertedNumStringWithUnits(
                            Math.round(journey.getEmissionsInKG()), 2) +
                            applicationContext.getString(R.string.maybe_walk),
                            journey.getEmissionsInKG()));
                } else if (curTransport.getTransportType() == TRANSPORT_TYPES.CAR) {
                    if (curRoute.getNumOfHighWayKilometers() <= 1.0 && curRoute.getNumOfCityKilometers() >= 2.0) {
                        tips.add(new Tip(journey.getDescription() +
                                thisJourneyGeneratesString +
                                CarbonUnitPrinter.getConvertedNumStringWithUnits(Math.round(journey.getEmissionsInKG()), 2) +
                                applicationContext.getString(R.string.maybe_bus),
                                journey.getEmissionsInKG()));
                    } else if (curRoute.getNumOfCityKilometers() + curRoute.getNumOfHighWayKilometers() <= 50) {
                        tips.add(new Tip(journey.getDescription() + thisJourneyGeneratesString +
                                CarbonUnitPrinter.getConvertedNumStringWithUnits(Math.round(journey.getEmissionsInKG()), 2) +
                                applicationContext.getString(R.string.matbe_skytrain), journey.getEmissionsInKG()));
                    }
                }
                double totalCurDistance = curRoute.getNumOfCityKilometers() + curRoute.getNumOfHighWayKilometers();
                if (totalCurDistance > 100) {
                    tips.add(new Tip(journey.getDescription() + thisJourneyGeneratesString +
                            CarbonUnitPrinter.getConvertedNumStringWithUnits(Math.round(journey.getEmissionsInKG()), 2) +
                            applicationContext.getString(R.string.maybe_park_and_ride), journey.getEmissionsInKG()));
                    tips.add(new Tip(journey.getDescription() + thisJourneyGeneratesString +
                            CarbonUnitPrinter.getConvertedNumStringWithUnits(Math.round(journey.getEmissionsInKG()), 2) +
                            applicationContext.getString(R.string.maybe_carpool), journey.getEmissionsInKG()));

                }
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat form = new SimpleDateFormat("yyyy/MM/dd", Locale.CANADA);
                String date = "" + form.format(calendar.getTime());
                if (curTransport.getTransportType() == 0) {
                    totalCityDistance += curRoute.getNumOfCityKilometers();
                    totalHwyDistance += curRoute.getNumOfHighWayKilometers();
                    if (journey.getDateToString().equals(date)) todaysCarJourneys++; //todays cars
                    totalCarJourneys++;//all_days cars
                    totalCarEmissions += journey.getEmissionsInKG(); //all_days cars
                }
                if (journey.getDateToString().equals(date))
                    todaysEmissions += journey.getEmissionsInKG(); //todays all_transports
            }
            if (todaysCarJourneys > 3) {
                final String youHadString = applicationContext.getString(R.string.you_had);
                final String numOfCarTripsString = applicationContext.getString(R.string.num_car_trips);
                tips.add(new Tip(youHadString + todaysCarJourneys + numOfCarTripsString +
                        CarbonUnitPrinter.getConvertedNumStringWithUnits(Math.round(todaysEmissions), 2) + applicationContext.getString(R.string.ccombine_trips), todaysEmissions));
                tips.add(new Tip(youHadString + todaysCarJourneys + numOfCarTripsString +
                        CarbonUnitPrinter.getConvertedNumStringWithUnits(Math.round(todaysEmissions), 2) + applicationContext.getString(R.string.combine_trips_tommorow), todaysEmissions));
            }
            LinkedList<Car> carsSaved = carCollection.getCarsSaved();
            if (carsSaved.size() != 0) {
                Car cur = carsSaved.get(0);
                Car worst = carsSaved.get(0);
                for (Car car : carsSaved) {
                    if (car.isSelectableOnMenu()) {
                        if (getCarEmission(cur, totalCityDistance, totalHwyDistance) > getCarEmission(car, totalCityDistance, totalHwyDistance)) {
                            cur = car;
                        } else {
                            worst = car;
                        }
                    }
                }
                if (!isOnlyCar(cur)) {
                    double revised = getCarEmission(cur, totalCityDistance, totalHwyDistance);
                    if (2 < totalCarJourneys) {
                        final String youHave = applicationContext.getString(R.string.you_have);
                        final String carJourneysTotal = applicationContext.getString(R.string.car_journey_total);
                        tips.add(new Tip(youHave + totalCarJourneys + carJourneysTotal
                                + CarbonUnitPrinter.getConvertedNumStringWithUnits(Math.round(totalCarEmissions), 2) +
                                applicationContext.getString(R.string.driving_car) + ":(" +
                                cur.getCarNickname() + ")" + applicationContext.getString(R.string.reduce_to) +
                                CarbonUnitPrinter.getConvertedNumStringWithUnits(Math.round(revised), 2) + "", totalCarEmissions));
                        if (!cur.getCarNickname().equals(worst.getCarNickname())) {
                            tips.add(new Tip(youHave + totalCarJourneys + carJourneysTotal
                                    + CarbonUnitPrinter.getConvertedNumStringWithUnits(Math.round(totalCarEmissions), 2) +
                                    applicationContext.getString(R.string.avoid_car) + ":(" + worst.getCarNickname() + ")" +
                                    applicationContext.getString(R.string.because_bad), totalCarEmissions));
                        }
                    }
                }
            }
        }
    }

    private void addBillTips() {
        double gasDailyEmission = 0;
        double hydroDailyEmission = 0;
        Calendar today = Calendar.getInstance();
        for (UtilityBill bill : utilityBillCollection.getBillsSaved()) {
            if (today.after(bill.getStartDate()) && today.before(bill.getEndDate())) {
                if (bill.getBillType() == UtilityBill.BILL_TYPE.NATURAL_GAS) {
                    gasDailyEmission += bill.getDailyEmissionsRate();
                } else if (bill.getBillType() == UtilityBill.BILL_TYPE.HYDRO) {
                    hydroDailyEmission += bill.getDailyEmissionsRate();
                }
            }
        }
        final String youGenerate = applicationContext.getString(R.string.you_generate);
        if (gasDailyEmission > 10) {  //need to merge with current master to check the utility type(gas / hydro).
            tips.add(new Tip(youGenerate +
                    CarbonUnitPrinter.getConvertedNumStringWithUnits(Math.round(gasDailyEmission), 2) +
                    applicationContext.getString(R.string.nat_gas_tip1), gasDailyEmission));
            tips.add(new Tip(youGenerate +
                    CarbonUnitPrinter.getConvertedNumStringWithUnits(Math.round(gasDailyEmission), 2) +
                    applicationContext.getString(R.string.natgas_tip2) +
                    CarbonUnitPrinter.getConvertedNumStringWithUnits(
                            Math.round(gasDailyEmission * ((0.009) / (56.1 * 0.0036))), 2) +
                    "", gasDailyEmission));
            tips.add(new Tip(youGenerate +
                    CarbonUnitPrinter.getConvertedNumStringWithUnits(Math.round(gasDailyEmission), 2) +
                    applicationContext.getString(R.string.natgas_tip3), gasDailyEmission));
        }
        if (hydroDailyEmission > 1) {
            tips.add(new Tip(youGenerate +
                    CarbonUnitPrinter.getConvertedNumStringWithUnits(Math.round(hydroDailyEmission), 2) +
                    applicationContext.getString(R.string.hydro_tip1), hydroDailyEmission));
            tips.add(new Tip(youGenerate +
                    CarbonUnitPrinter.getConvertedNumStringWithUnits(Math.round(hydroDailyEmission), 2) +
                    applicationContext.getString(R.string.hydro_tip2), hydroDailyEmission));
            tips.add(new Tip(youGenerate +
                    CarbonUnitPrinter.getConvertedNumStringWithUnits(Math.round(hydroDailyEmission), 2) +
                    applicationContext.getString(R.string.hydro_tip3), hydroDailyEmission));
        }
    }

    private boolean isOnlyCar(Car car) {
        for (Journey journey : journeyCollection.getSavedJourneys()) {
            if (journey.getCarDriven() != null && !journey.getCarDriven().getCarNickname().equals(car.getCarNickname())) {
                return false;
            }
        }
        return true;
    }

    private double getCarEmission(Car car, double city, double hwy) {
        return (car.getEmissionsPerHighwayKMInKG() * hwy) + (int) (car.getEmissionsPerCityKMInKG() * city);
    }

    //--------------------------------
    public int getCounterForBillId() {
        return counterBillId;
    }

    public void setCounterForBillId(int lastBillCounter) {
        this.counterBillId = lastBillCounter;
    }

    public void addCounterForBillId() {
        this.counterBillId = counterBillId + 1;
    }

    public void resetCounterForBillId() {
        this.counterBillId = 1;
    }

    public RouteCollection getRouteCollection() {
        return routeCollection;
    }

    public JourneyCollection getJourneyCollection() {
        return journeyCollection;
    }

    public CarCollection getCarCollection() {
        return carCollection;
    }

    private void openDbAdapter(Context ctx) {
        dbAdapter = new DBAdapter(ctx);
        dbAdapter.open();
    }

    private void closeDbAdapter() {
        dbAdapter.close();
    }

    public void addCarToDatabase(Car car, Context ctx) {
        openDbAdapter(ctx);

        dbAdapter.insertRowCars(car.getCarNickname(),
                car.getCarData().getMake(), car.getCarData().getModel(),
                car.getCarData().getYear(), car.getCarData().getDisplacementInLiters(),
                car.getCarData().getTransmission(), car.getCarData().getCityMPG(),
                car.getCarData().getHighwayMPG(), car.getCarData().getFuelType(), car.getIconId(),
                true);

        closeDbAdapter();
    }

    public void updateCarInDatabase(int idCar, Car car_chosen, Context ctx, boolean isSelectable) {
        openDbAdapter(ctx);

        dbAdapter.updateCarRow(
                idCar,
                car_chosen.getCarNickname(),
                car_chosen.getCarData().getMake(),
                car_chosen.getCarData().getModel(),
                car_chosen.getCarData().getYear(),
                car_chosen.getCarData().getDisplacementInLiters(),
                car_chosen.getCarData().getTransmission(),
                car_chosen.getCarData().getCityMPG(),
                car_chosen.getCarData().getHighwayMPG(),
                car_chosen.getCarData().getFuelType(),
                car_chosen.getIconId(),
                isSelectable);

        closeDbAdapter();
    }

    public void addRouteToDatabase(Route route, Context ctx) {
        openDbAdapter(ctx);

        dbAdapter.insertRowRoutes(route.getName(),
                route.getNumOfCityKilometers(),
                route.getNumOfHighWayKilometers(),
                true);

        closeDbAdapter();
    }


    public void updateRouteInDatabase(int idRoute, Route routeSelected, Context ctx, boolean isSelectable) {
        openDbAdapter(ctx);

        dbAdapter.updateRouteRow(idRoute,
                routeSelected.getName(),
                routeSelected.getNumOfCityKilometers(),
                routeSelected.getNumOfHighWayKilometers(),
                isSelectable);

        closeDbAdapter();
    }

    public void addJourneyToDatabase(int transportSelected, int indexCarSelected,
                                     int indexRouteSelected, String date, Context ctx) {
        openDbAdapter(ctx);

        dbAdapter.insertRowJourneys(transportSelected, indexCarSelected,
                indexRouteSelected, date);

        closeDbAdapter();
    }

    public void updateJourneyToDatabaseAddCar(Journey journey, Car car, Context ctx) {
        openDbAdapter(ctx);

        String formattedDate = formatDate(journey.getDate());
        dbAdapter.updateJourneyRow(journey.getJourneyId(),
                car.getTransportType(),
                (carCollection.getSavedCars().size() - 1),
                (journey.getRoutePosition()),
                formattedDate);

        closeDbAdapter();
    }

    public void updateJourneyToDatabaseEditCar(Journey journey, int position, Context ctx) {
        openDbAdapter(ctx);

        String formattedDate = formatDate(journey.getDate());
        dbAdapter.updateJourneyRow(
                journey.getJourneyId(),
                TRANSPORT_TYPES.CAR,
                position,
                (journey.getRoutePosition()),
                formattedDate);

        closeDbAdapter();
    }

    public void updateJourneyToDatabaseEditCalendar(Journey journey, String calendar, Context ctx) {
        openDbAdapter(ctx);

        dbAdapter.updateJourneyRow(
                journey.getJourneyId(),
                journey.getTransportTaken().getTransportType(),
                journey.getCarPosition(),
                (journey.getRoutePosition()),
                calendar);

        closeDbAdapter();
    }

    public void updateJourneyCarTypeInDatabase(int journeyId, int type,
                                               int value, int routePos, String date, Context ctx) {
        openDbAdapter(ctx);

        dbAdapter.updateJourneyRow(
                journeyId, type,
                value, routePos,
                date);

        closeDbAdapter();
    }


    public void deleteJourneyInDatabase(int journeyId, Context ctx) {
        openDbAdapter(ctx);

        dbAdapter.deleteRowJourney(journeyId);

        closeDbAdapter();
    }

    public void addBillToDatabase(String startDate, String endDate,
                                  double usersEmissionsInKg, String type,
                                  double usageInKwh, int numOfPeople,
                                  Context ctx) {
        openDbAdapter(ctx);

        dbAdapter.insertRowBills(startDate, endDate,
                usersEmissionsInKg, type,
                usageInKwh, numOfPeople);

        closeDbAdapter();
    }

    public void updateBillToDatabase(int billId, String startDate, String endDate,
                                     double usersEmissionsInKg, String type,
                                     double usageInKwh, int numOfPeople,
                                     Context ctx) {
        openDbAdapter(ctx);

        dbAdapter.updateBillsRow(billId,
                startDate, endDate,
                usersEmissionsInKg, type,
                usageInKwh, numOfPeople);

        closeDbAdapter();
    }

    public void deleteBillInDatabase(int billId, Context ctx) {
        openDbAdapter(ctx);

        dbAdapter.deleteRowBill(billId);

        closeDbAdapter();
    }

    private String formatDate(Calendar date) {
        SimpleDateFormat form = new SimpleDateFormat("yyyy/MM/dd", Locale.CANADA);
        return form.format(date.getTime());
    }

    public boolean isUsingRelatableUnit() {
        return isUsingRelatableUnit;
    }

    public void setUsingRelatableUnit(boolean usingRelatableUnit) {
        isUsingRelatableUnit = usingRelatableUnit;
    }

    public LinkedList<Car> getCarSavedCollectionForAdapter() {
        LinkedList<Car> carList = carCollection.getCarsSaved();

        LinkedList<Car> carListEdited = new LinkedList<Car>();
        for (int ii = 0; ii < carList.size(); ii++) {
            if (carList.get(ii).isSelectableOnMenu()) {
                carListEdited.add(carList.get(ii));
            }
        }
        return carListEdited;
    }


}