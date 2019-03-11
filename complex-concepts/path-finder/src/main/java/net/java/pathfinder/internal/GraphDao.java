package net.java.pathfinder.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.faulttolerance.Retry;

@ApplicationScoped
public class GraphDao implements Serializable {

    private final Random random = new Random();

    @Retry(maxRetries = 10)  // retries in case of a "Random failure"    
    public List<String> listLocations() {
        if (new Random().nextBoolean()) {
            throw new RuntimeException("Random failure");
        }
        return new ArrayList<>(Arrays.asList("CNHKG", "AUMEL", "SESTO",
                "FIHEL", "USCHI", "JNTKO", "DEHAM", "CNSHA", "NLRTM", "SEGOT",
                "CNHGH", "USNYC", "USDAL"));
    }

    public String getVoyageNumber(String from, String to) {
        int i = random.nextInt(5);

        switch (i) {
            case 0:
                return "0100S";
            case 1:
                return "0200T";
            case 2:
                return "0300A";
            case 3:
                return "0301S";
            default:
                return "0400S";
        }
    }
}
