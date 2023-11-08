package org.coffeshop.receipt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.coffeshop.receipt.config.ConfigurationLoader;
import org.coffeshop.receipt.model.Offering;

public class TestUtils {

    private TestUtils() {

    }

    /**
     * Generation offering for provided configuration. Key for hash is offering id, value is how many offerings should
     * be generated
     *
     * @param config configuration for offering generation
     *
     * @return list with generated warnings
     */
    public static List<Offering> generateOfferings(Map<String, Integer> config) {
        List<Offering> configuredOfferings = ConfigurationLoader.loadOfferingConfiguration("offerings.csv");

        List<Offering> offerings = new ArrayList<>();

        for (var configEntry : config.entrySet()) {
            Offering offering = configuredOfferings.stream().filter(o -> o.getId().equals(configEntry.getKey())).findFirst().orElseThrow();
            for (int i = 0; i < configEntry.getValue(); i++) {
                offerings.add(offering);
            }
        }

        return offerings;
    }

}
