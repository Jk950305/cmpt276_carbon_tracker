package com.example.ray.carbontracker_flame.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * JourneyCollection encapsulates operations on the Journeys saved in the model
 */

public class JourneyCollection {

    private final LinkedList<Journey> journeysSaved;
    private final CarbonTrackerModel ctm;

    public JourneyCollection() {
        journeysSaved = new LinkedList<>();
        ctm = CarbonTrackerModel.getInstance();
    }

    public List<Journey> getSavedJourneys() {
        return journeysSaved;
    }

    public void deleteJourney(Journey journey) {
        getSavedJourneys().remove(journey);
        updateTipsInTheModel();
    }

    public void deleteJourney(int position) {
        getSavedJourneys().remove(position);
        updateTipsInTheModel();
    }

    public void addJourney(Car carDriven, Route routeTaken, Calendar date, int uniqueId) {
        Journey journey = new Journey(carDriven, routeTaken, date);
        journey.setJourneyId(uniqueId);
        getSavedJourneys().add(journey);
        updateTipsInTheModel();
    }

    private void updateTipsInTheModel() {
        if (ctm != null) {
            ctm.updateTips();
        }
    }

    public void addJourney(Transport transport, Route routeTaken, Calendar date, int uniqueId) {
        Journey journey = new Journey(transport, routeTaken, date);
        journey.setJourneyId(uniqueId);
        journeysSaved.add(journey);
        updateTipsInTheModel();
    }

    public ArrayList<String> getJourneyDescriptions(CarbonTrackerModel carbonTrackerModel) {
        ArrayList<String> strings = new ArrayList<>();
        for (Journey current : getSavedJourneys()) {
            strings.add(current.getDescription());
        }
        return strings;
    }

    //TODO: This will be modified after DB is completely integrated with journey Creation Date.
    public int getCountOfCreatedJourney(Calendar today) {
        int count = 0;
        SimpleDateFormat form = new SimpleDateFormat("MM/dd/yyyy");
        String current = form.format(today);
        for (Journey journey : journeysSaved) {
            String target = form.format(journey.getCreatedDate());
            if (target.equals(current)) {
                count++;
            }
        }
        return count;
    }
}
