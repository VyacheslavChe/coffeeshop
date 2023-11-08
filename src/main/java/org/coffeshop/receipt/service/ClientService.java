package org.coffeshop.receipt.service;

import java.util.List;

import org.coffeshop.receipt.model.Client;
import org.coffeshop.receipt.model.Offering;
import org.coffeshop.receipt.model.OfferingType;

public class ClientService {
    private static final int FREE_SNACK_STAMP_COUNT = 5;

    public Long updateClientProgram(Client client, List<Offering> currentOfferings) {
        Long earnedStamps = currentOfferings.stream()
                .filter(offering -> offering.getCategory() == OfferingType.SNACK)
                .count();
        client.setStampCount(client.getStampCount() + earnedStamps);

        return earnedStamps;
    }

    public Long getFreeSnackCount(Client client) {
        return client.getStampCount() / (FREE_SNACK_STAMP_COUNT);
    }

    public Long useFreeSnacks(Client client, Long usedFreeSnacks) {
        Long freeSnackCount = getFreeSnackCount(client);
        if (usedFreeSnacks > freeSnackCount) {
            throw new RuntimeException("Logic error: unexpected free snack used");
        }
        if (freeSnackCount > 0) {
            client.setStampCount(client.getStampCount() - usedFreeSnacks * FREE_SNACK_STAMP_COUNT);
        }

        return usedFreeSnacks * FREE_SNACK_STAMP_COUNT;
    }

}
