package org.coffeshop.receipt.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.coffeshop.receipt.config.ConfigurationLoader;
import org.coffeshop.receipt.exceptions.AmbiguousOfferingsException;
import org.coffeshop.receipt.exceptions.IncompatibleExtraOfferingException;
import org.coffeshop.receipt.exceptions.OfferingParsingException;
import org.coffeshop.receipt.exceptions.OfferingsNotFoundException;
import org.coffeshop.receipt.model.Offering;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OfferingServiceTest {

    OfferingService offeringService;

    @BeforeEach
    void setUp() {
        var configuredOfferings = ConfigurationLoader.loadOfferingConfiguration("offerings.csv");
        offeringService = new OfferingService(configuredOfferings);
    }

    @Test
    @DisplayName("Test for correct single offering")
    void testParseOffering() throws OfferingParsingException {
        List<Offering> parsedOfferings = offeringService.parseOfferingString("Medium coffee");

        Assertions.assertEquals(1, parsedOfferings.size());
        Assertions.assertEquals("Coffee", parsedOfferings.get(0).getName());
        Assertions.assertEquals("medium", parsedOfferings.get(0).getVolume());
    }

    @Test
    @DisplayName("Test for correct input with extra")
    void testParseOfferingWithExtra() throws OfferingParsingException {
        List<Offering> parsedOfferings = offeringService.parseOfferingString("Large coffee with extra milk");

        Assertions.assertEquals(2, parsedOfferings.size());
        Assertions.assertEquals("Coffee", parsedOfferings.get(0).getName());
        Assertions.assertEquals("large", parsedOfferings.get(0).getVolume());
        Assertions.assertEquals("Extra milk", parsedOfferings.get(1).getName());
    }

    @Test
    @DisplayName("Test for several extras")
    void testParseOfferingWithSeveralExtras() throws OfferingParsingException {
        List<Offering> parsedOfferings = offeringService.parseOfferingString("Large coffee with extra milk with special roast");

        Assertions.assertEquals(3, parsedOfferings.size());
        Assertions.assertEquals("Coffee", parsedOfferings.get(0).getName());
        Assertions.assertEquals("large", parsedOfferings.get(0).getVolume());
        Assertions.assertEquals("Extra milk", parsedOfferings.get(1).getName());
        Assertions.assertEquals("Special roast coffee", parsedOfferings.get(2).getName());
    }

    @Test
    @DisplayName("Test for case insensitivity")
    void testParseOfferingWithCaps() throws OfferingParsingException {
        List<Offering> parsedOfferings = offeringService.parseOfferingString("LARGE COFFEE WITH EXTRA MILK WITH SPECIAL ROAST");

        Assertions.assertEquals(3, parsedOfferings.size());
        Assertions.assertEquals("Coffee", parsedOfferings.get(0).getName());
        Assertions.assertEquals("large", parsedOfferings.get(0).getVolume());
        Assertions.assertEquals("Extra milk", parsedOfferings.get(1).getName());
        Assertions.assertEquals("Special roast coffee", parsedOfferings.get(2).getName());
    }

    @Test
    @DisplayName("Test for incorrect input with ambitious base offering")
    void testParseOfferingStringForAmbitiousBaseOffering() throws OfferingParsingException {
        assertThrows(AmbiguousOfferingsException.class, () -> {
            offeringService.parseOfferingString("Large coffee roll");
        });
    }

    @Test
    @DisplayName("Test for incorrect input with ambitious extra offering")
    void testParseOfferingStringForAmbitiousExtraOffering() throws OfferingParsingException {
        assertThrows(AmbiguousOfferingsException.class, () -> {
            offeringService.parseOfferingString("Large coffee with extra milk and special roast");
        });
    }

    @Test
    @DisplayName("Test for incorrect input with not found offering")
    void testParseOfferingStringForUnknownInput() {
        assertThrows(OfferingsNotFoundException.class, () -> {
            offeringService.parseOfferingString("some unexpected input");
        });
    }

    @Test
    @DisplayName("Test for unknown extra offering")
    void testParseOfferingStringForUnknownExtra() {
        assertThrows(OfferingsNotFoundException.class, () -> {
            offeringService.parseOfferingString("coffee with something");
        });
    }

    @Test
    @DisplayName("Test for extra without base offering")
    void testParseExtraOfferingWithoutBase() throws OfferingParsingException {
        assertThrows(OfferingsNotFoundException.class, () -> {
            offeringService.parseOfferingString("extra milk");
        });
    }

    @Test
    @DisplayName("Test for incompatible extra")
    void testParseIncompatibleExtraOffering() throws OfferingParsingException {
        assertThrows(IncompatibleExtraOfferingException.class, () -> {
            offeringService.parseOfferingString("roll with extra milk");
        });
    }
}