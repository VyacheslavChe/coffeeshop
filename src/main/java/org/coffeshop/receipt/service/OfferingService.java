package org.coffeshop.receipt.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.coffeshop.receipt.exceptions.AmbiguousOfferingsException;
import org.coffeshop.receipt.exceptions.IncompatibleExtraOfferingException;
import org.coffeshop.receipt.exceptions.OfferingParsingException;
import org.coffeshop.receipt.exceptions.OfferingsNotFoundException;
import org.coffeshop.receipt.model.Offering;
import org.coffeshop.receipt.model.OfferingType;

public class OfferingService {

    private static final String DELIMITER_FOR_EXTRA = "\\swith\\s";

    private final List<Offering> configuredOfferings;

    public OfferingService(List<Offering> configuredOfferings) {
        this.configuredOfferings = configuredOfferings;
    }

    public List<Offering> parseOfferingString(String offeringString) throws OfferingParsingException {
        List<Offering> parsedOfferings = new ArrayList<>();

        // index 0 is base offering, others (if exists) are extras
        String[] checkedOfferings = offeringString.toLowerCase().split(DELIMITER_FOR_EXTRA);

        for (Offering offering : configuredOfferings) {
            if (isBaseOfferingMatched(checkedOfferings[0], offering)) {
                if (parsedOfferings.isEmpty()) {
                    parsedOfferings.add(offering);
                } else {
                    throw new AmbiguousOfferingsException("More than one offering detected for string: " + offeringString);
                }
            }
        }
        if (parsedOfferings.isEmpty()) {
            throw new OfferingsNotFoundException("No any offering match to the string: " + offeringString);
        }

        // Process extras now
        for (int index = 1; index < checkedOfferings.length; index++) {
            boolean extraDetected = false;
            for (Offering offering : configuredOfferings) {
                if (isExtraOfferingMatched(checkedOfferings[index], offering, parsedOfferings.get(0))) {
                    if (extraDetected) {
                        throw new AmbiguousOfferingsException("More than one offering detected for string: " + checkedOfferings[index]);
                    }
                    parsedOfferings.add(offering);
                    extraDetected = true;
                }
            }
        }

        return parsedOfferings;
    }

    private boolean isBaseOfferingMatched(String offeringString, Offering offering) {
        if (offering.getCategory() == OfferingType.EXTRA) {
            return false;
        }
        if (offering.getVolume() != null && !offeringString.contains(offering.getVolume())) {
            return false;
        }
        for (String pattern : offering.getPatterns()) {
            if (offeringString.contains(pattern)) {
                return true;
            }
        }

        return false;
    }

    private boolean isExtraOfferingMatched(String offeringString, Offering offering,
                                           Offering baseOffering) throws IncompatibleExtraOfferingException {
        if (offering.getCategory() != OfferingType.EXTRA) {
            return false;
        }
        for (String pattern : offering.getPatterns()) {
            if (offeringString.contains(pattern)) {
                if (isExtraCompatibleWithBase(offering, baseOffering)) {
                    return true;
                }
                throw new IncompatibleExtraOfferingException("incompatible extra ordered for offering: " + offeringString);
            }
        }

        return false;
    }

    private boolean isExtraCompatibleWithBase(Offering extraOffering, Offering baseOffering) {
        if (baseOffering.getCompatibleExtraOffers() == null) {
            return false;
        }
        return Arrays.stream(baseOffering.getCompatibleExtraOffers()).anyMatch(offering -> offering.equals(extraOffering.getId()));
    }
}
