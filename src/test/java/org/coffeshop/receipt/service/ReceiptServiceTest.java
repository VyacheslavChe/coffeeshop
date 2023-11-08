package org.coffeshop.receipt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.coffeshop.receipt.TestUtils;
import org.coffeshop.receipt.model.Client;
import org.coffeshop.receipt.model.Offering;
import org.coffeshop.receipt.model.Receipt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReceiptServiceTest {

    @Test
    @DisplayName("Test for receipt with several offerings")
    void formatReceipt() {
        String expectedResult = "Item                          Count     Price           Sum\n"
                + "Coffee (small)                    2      2.55 CHF      5.10 CHF\n"
                + "Freshly squeezed orange juice     2      3.95 CHF      7.90 CHF\n"
                + "Bacon Roll                        1      0.00 CHF      0.00 CHF\n"
                + "Bacon Roll                        5      4.53 CHF     22.65 CHF\n"
                + "Extra milk                        1      0.00 CHF      0.00 CHF\n"
                + "\n"
                + "Total:                                                35,65 CHF\n";

        Client client = new Client(0L);
        List<Offering> offerings = TestUtils.generateOfferings(Map.of("small-coffee", 2, "orange-juice", 2, "bacon-roll", 6, "extra"
                + "-milk", 1));
        ClientService clientService = new ClientService();

        ReceiptService receiptService = new ReceiptService(clientService);

        Receipt result = receiptService.createReceipt(client, offerings);

        assertNotNull(result);
        assertEquals(1, client.getStampCount());
        assertEquals(expectedResult, result.getReceiptText());
        assertEquals(1, result.getEarnedStamps());
    }
}