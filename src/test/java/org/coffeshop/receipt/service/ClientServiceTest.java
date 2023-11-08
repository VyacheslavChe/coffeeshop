package org.coffeshop.receipt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.coffeshop.receipt.TestUtils;
import org.coffeshop.receipt.model.Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClientServiceTest {

    @Test
    @DisplayName("Test client stamp program update")
    void updateClientProgram() {
        ClientService clientService = new ClientService();
        Client client = new Client(0L);

        clientService.updateClientProgram(client, TestUtils.generateOfferings(Map.of("small-coffee", 2, "orange-juice", 2, "bacon-roll", 3, "extra"
                + "-milk", 10)));

        assertEquals(3, client.getStampCount());
    }

    @Test
    @DisplayName("Test for free snack calculations")
    void getFreeSnackCount() {
        ClientService clientService = new ClientService();

        Client client = new Client(0L);
        assertEquals(0, clientService.getFreeSnackCount(client));

        client.setStampCount(4L);
        assertEquals(0, clientService.getFreeSnackCount(client));

        client.setStampCount(5L);
        assertEquals(1, clientService.getFreeSnackCount(client));

        client.setStampCount(9L);
        assertEquals(1, clientService.getFreeSnackCount(client));

        client.setStampCount(10L);
        assertEquals(2, clientService.getFreeSnackCount(client));

        client.setStampCount(11L);
        assertEquals(2, clientService.getFreeSnackCount(client));

    }

    @Test
    @DisplayName("Test for free snacks consumption")
    void testUseFreeSnacks() {
        ClientService clientService = new ClientService();
        Client client = new Client(6L);
        clientService.useFreeSnacks(client, 1L);
        assertEquals(1, client.getStampCount());

        client.setStampCount(12L);
        clientService.useFreeSnacks(client, 1L);
        assertEquals(7, client.getStampCount());

        client.setStampCount(12L);
        clientService.useFreeSnacks(client, 2L);
        assertEquals(2, client.getStampCount());
    }

    @Test
    @DisplayName("Test for illegal free snack consumption")
    void testUseTooManyFreeSnacks() {
        ClientService clientService = new ClientService();
        Client client = new Client(6L);
        assertThrows(RuntimeException.class, () -> {
            clientService.useFreeSnacks(client, 2L);
        });
    }

}