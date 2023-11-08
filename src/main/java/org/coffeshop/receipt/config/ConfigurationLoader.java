package org.coffeshop.receipt.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.coffeshop.receipt.model.Offering;
import org.coffeshop.receipt.model.OfferingType;

public class ConfigurationLoader {

    private ConfigurationLoader() {

    }

    public static List<Offering> loadOfferingConfiguration(String fileName) {
        List<Offering> configuredOfferings = new ArrayList<>();

        var inputStream = ClassLoader.getSystemResourceAsStream(fileName);
        if (inputStream == null) {
            throw new RuntimeException("Can not load offering configuration file");
        }
        var configuredOfferingScanner = new Scanner(inputStream);

        for (String offeringString = configuredOfferingScanner.nextLine(); !offeringString.isEmpty(); offeringString = configuredOfferingScanner.nextLine()) {
            Offering offering = extractOfferFromLine(offeringString);
            if (offering != null) {
                configuredOfferings.add(offering);
            }
        }

        return configuredOfferings;
    }

    public static Offering extractOfferFromLine(String line) {
        Offering.Builder offering = new Offering.Builder();
        try (Scanner rowScanner = new Scanner(line).useLocale(Locale.ENGLISH)) {
            rowScanner.useDelimiter(";");
            if (rowScanner.hasNext()) {
                offering.setId(rowScanner.next());
            } else {
                return null;
            }

            if (rowScanner.hasNext()) {
                offering.setCategory(OfferingType.valueOf(rowScanner.next().toUpperCase()));
            } else {
                return null;
            }

            if (rowScanner.hasNext()) {
                offering.setName(rowScanner.next());
            } else {
                return null;
            }

            if (rowScanner.hasNext()) {
                offering.setVolume(rowScanner.next());
            } else {
                return null;
            }

            if (rowScanner.hasNextBigDecimal()) {
                offering.setPrice(rowScanner.nextBigDecimal());
            } else {
                return null;
            }

            if (rowScanner.hasNext()) {
                offering.setPatterns(rowScanner.next().split(","));
            } else {
                return null;
            }

            if (rowScanner.hasNext()) {
                offering.setCompatibleExtraOffers(rowScanner.next().split(","));
            }
        }

        return offering.createOffering();
    }

}
