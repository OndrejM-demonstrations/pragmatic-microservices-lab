package net.java.cargotracker.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import net.java.cargotracker.domain.model.cargo.Itinerary;
import net.java.cargotracker.domain.model.cargo.Leg;
import net.java.cargotracker.domain.model.cargo.RouteSpecification;
import net.java.cargotracker.domain.model.location.LocationRepository;
import net.java.cargotracker.domain.model.location.UnLocode;
import net.java.cargotracker.domain.model.voyage.VoyageNumber;
import net.java.cargotracker.domain.model.voyage.VoyageRepository;

@Stateless
public class RoutingService {

    // TODO Use injection instead?
    private static final Logger LOGGER = Logger.getLogger(RoutingService.class.getName());

    private static final long ONE_MIN_MS = 1000 * 60;
    private static final long ONE_DAY_MS = ONE_MIN_MS * 60 * 24;
    private final Random random = new Random();

    @Inject
    private LocationRepository locationRepository;
    @Inject
    private VoyageRepository voyageRepository;
    @Inject
    private GraphDao dao;

    /**
     * @param routeSpecification route specification
     * @return A list of itineraries that satisfy the specification. May be an
     * empty list if no route is found.
     */
    public List<Itinerary> fetchRoutesForSpecification(
            RouteSpecification routeSpecification) {
        Date date = nextDate(new Date());

        String originUnLocode = routeSpecification.getOrigin()
                .getUnLocode().getIdString();
        String destinationUnLocode = routeSpecification.getDestination()
                .getUnLocode().getIdString();

        List<String> allVertices = dao.listLocations();
        allVertices.remove(originUnLocode);
        allVertices.remove(destinationUnLocode);

        int candidateCount = getRandomNumberOfCandidates();

        List<Itinerary> itineraries = new ArrayList<>(candidateCount);

        for (int i = 0; i < candidateCount; i++) {
            allVertices = getRandomChunkOfLocations(allVertices);
            List<Leg> legs = new ArrayList<>(allVertices.size() - 1);
            String firstLegTo = allVertices.get(0);

            Date fromDate = nextDate(date);
            Date toDate = nextDate(fromDate);
            date = nextDate(toDate);

            legs.add(new Leg(voyageRepository.find(
                    new VoyageNumber(dao.getVoyageNumber(originUnLocode, firstLegTo))),
                    routeSpecification.getOrigin(),
                    locationRepository.find(new UnLocode(firstLegTo)), fromDate, toDate));

            for (int j = 0; j < allVertices.size() - 1; j++) {
                String current = allVertices.get(j);
                String next = allVertices.get(j + 1);
                fromDate = nextDate(date);
                toDate = nextDate(fromDate);
                date = nextDate(toDate);
                legs.add(new Leg(voyageRepository.find(
                        new VoyageNumber(dao.getVoyageNumber(current, next))),
                        locationRepository.find(new UnLocode(current)),
                        locationRepository.find(new UnLocode(next)), fromDate, toDate));
            }

            String lastLegFrom = allVertices.get(allVertices.size() - 1);
            fromDate = nextDate(date);
            toDate = nextDate(fromDate);
            legs.add(new Leg(voyageRepository.find(new VoyageNumber(
                    dao.getVoyageNumber(lastLegFrom, destinationUnLocode))),
                    locationRepository.find(new UnLocode(lastLegFrom)),
                    routeSpecification.getDestination(), fromDate, toDate));

            Itinerary itinerary = new Itinerary(legs);

            if (routeSpecification.isSatisfiedBy(itinerary)) {
                itineraries.add(itinerary);
            } else {
                LOGGER.log(Level.FINE,
                        "Received itinerary that did not satisfy the route specification");
            }
        }

        LOGGER.log(Level.INFO, "Path finder service called for {0} to {1}",
                new Object[]{routeSpecification.getOrigin(),
                    routeSpecification.getDestination()});

        return itineraries;
    }

    private Date nextDate(Date date) {
        return new Date(date.getTime() + ONE_DAY_MS
                + (random.nextInt(1000) - 500) * ONE_MIN_MS);
    }

    private int getRandomNumberOfCandidates() {
        return 3 + random.nextInt(3);
    }

    private List<String> getRandomChunkOfLocations(List<String> allLocations) {
        Collections.shuffle(allLocations);
        int total = allLocations.size();
        int chunk = total > 4 ? 1 + new Random().nextInt(5) : total;
        return allLocations.subList(0, chunk);
    }
}
