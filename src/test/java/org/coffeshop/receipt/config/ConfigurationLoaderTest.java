package org.coffeshop.receipt.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.coffeshop.receipt.model.Offering;
import org.coffeshop.receipt.model.OfferingType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConfigurationLoaderTest {

    @Test
    @DisplayName("Test configuration loading")
    void testConfigurationLoading() {
        List<Offering> configuredOfferings = ConfigurationLoader.loadOfferingConfiguration("simple-offerings.csv");

        assertNotNull(configuredOfferings);
        assertEquals(3, configuredOfferings.size());
        assertEquals("small-coffee", configuredOfferings.get(0).getId());
        assertEquals("bacon-roll", configuredOfferings.get(1).getId());
        assertEquals("special-roast", configuredOfferings.get(2).getId());
    }

    @Test
    @DisplayName("Test for beverage parsing")
    void testBeverageParsing() {
        Offering offering = ConfigurationLoader.extractOfferFromLine("small-coffee;beverage;Coffee;small;2.55;coffee;extra-milk,foamed-milk,"
                + "special-roast");

        assertNotNull(offering);
        assertEquals("small-coffee", offering.getId());
        assertEquals(OfferingType.BEVERAGE, offering.getCategory());
        assertEquals("Coffee", offering.getName());
        assertEquals("small", offering.getVolume());
        assertEquals(new BigDecimal("2.55"), offering.getPrice());
        assertEquals(1, offering.getPatterns().length);
        assertEquals("coffee", offering.getPatterns()[0]);
        assertEquals(3, offering.getCompatibleExtraOffers().length);
        assertEquals("extra-milk", offering.getCompatibleExtraOffers()[0]);
        assertEquals("foamed-milk", offering.getCompatibleExtraOffers()[1]);
        assertEquals("special-roast", offering.getCompatibleExtraOffers()[2]);
    }

    @Test
    @DisplayName("Test for snack parsing")
    void testSnackParsing() {
        Offering offering = ConfigurationLoader.extractOfferFromLine("bacon-roll;snack;Bacon Roll;;4.53;bacon,roll;");

        assertNotNull(offering);
        assertEquals("bacon-roll", offering.getId());
        assertEquals(OfferingType.SNACK, offering.getCategory());
        assertEquals("Bacon Roll", offering.getName());
        assertTrue(offering.getVolume().isEmpty());
        assertEquals(new BigDecimal("4.53"), offering.getPrice());
        assertEquals(2, offering.getPatterns().length);
        assertEquals("bacon", offering.getPatterns()[0]);
        assertEquals("roll", offering.getPatterns()[1]);
        assertNull(offering.getCompatibleExtraOffers());
    }

    @Test
    @DisplayName("Test for extras parsing")
    void testExtrasParsing() {
        Offering offering = ConfigurationLoader.extractOfferFromLine("special-roast;extra;Special roast coffee;;0.95;roast;");

        assertNotNull(offering);
        assertEquals("special-roast", offering.getId());
        assertEquals(OfferingType.EXTRA, offering.getCategory());
        assertEquals("Special roast coffee", offering.getName());
        assertTrue(offering.getVolume().isEmpty());
        assertEquals(new BigDecimal("0.95"), offering.getPrice());
        assertEquals(1, offering.getPatterns().length);
        assertEquals("roast", offering.getPatterns()[0]);
        assertNull(offering.getCompatibleExtraOffers());
    }

}