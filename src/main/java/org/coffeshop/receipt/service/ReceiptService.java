package org.coffeshop.receipt.service;

import static java.util.stream.Collectors.groupingBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import org.coffeshop.receipt.model.Client;
import org.coffeshop.receipt.model.Offering;
import org.coffeshop.receipt.model.OfferingType;
import org.coffeshop.receipt.model.Receipt;

public class ReceiptService {
    private final ClientService clientService;

    private static final String CURRENCY = "CHF";

    private static final String HEADER_FORMAT = "%-30s%5s%10s%14s\n";

    private static final String LINE_FORMAT = "%-30s%5d%10.2f%4s%10.2f%4s\n";

    private static final String TOTAL_FORMAT = "\n%-49s%10.2f%4s\n";

    private static final Integer MAX_ITEM_LENGTH = 29;

    public ReceiptService(ClientService clientService) {
        this.clientService = clientService;
    }

    public Receipt createReceipt(Client client, List<Offering> selectedOfferings) {
        Receipt receipt = new Receipt();
        StringBuilder output = new StringBuilder(String.format(HEADER_FORMAT, "Item", "Count", "Price", "Sum"));
        List<BigDecimal> totals = new ArrayList<>();

        Long currentStamps = client.getStampCount();
        Long receivedStamps = clientService.updateClientProgram(client, selectedOfferings);
        receipt.setEarnedStamps(receivedStamps);

        long beverageCount = selectedOfferings.stream().filter(offering -> offering.getCategory() == OfferingType.BEVERAGE).count();
        selectedOfferings.stream()
                .filter(offering -> offering.getCategory() == OfferingType.BEVERAGE).sorted(Comparator.comparing(Offering::getPrice))
                .collect(groupingBy(Offering::getId)).values().forEach(offerings -> {
                    LineResult lineResult = generateLineForOffering(offerings, new AtomicInteger(0));
                    output.append(lineResult.receiptLine());
                    totals.add(lineResult.total());
                });

        long snackCount = selectedOfferings.stream().filter(offering -> offering.getCategory() == OfferingType.SNACK).count();
        AtomicInteger freeSnackCount = new AtomicInteger(Math.min(clientService.getFreeSnackCount(client).intValue(), (int) snackCount));
        Long usedStamps = clientService.useFreeSnacks(client, freeSnackCount.longValue());
        if (usedStamps <= currentStamps) {
            receipt.setEarnedStamps(receivedStamps);
        } else {
            receipt.setEarnedStamps(currentStamps + receivedStamps - usedStamps);
        }

        selectedOfferings.stream()
                .filter(offering -> offering.getCategory() == OfferingType.SNACK).sorted(Comparator.comparing(Offering::getPrice))
                .collect(groupingBy(Offering::getId)).values().forEach(offerings -> {
                    LineResult lineResult = generateLineForOffering(offerings, freeSnackCount);
                    output.append(lineResult.receiptLine());
                    totals.add(lineResult.total());
                });

        AtomicInteger freeExtraCount = new AtomicInteger((int) Math.min(beverageCount, snackCount));
        selectedOfferings.stream()
                .filter(offering -> offering.getCategory() == OfferingType.EXTRA)
                .collect(groupingBy(Offering::getId)).values().forEach(offerings -> {
                    LineResult lineResult = generateLineForOffering(offerings, freeExtraCount);
                    output.append(lineResult.receiptLine());
                    totals.add(lineResult.total());
                });

        output.append(String.format(TOTAL_FORMAT, "Total:", totals.stream().reduce(BigDecimal.ZERO, BigDecimal::add), CURRENCY));

        receipt.setReceiptText(output.toString());
        return receipt;
    }

    private LineResult generateLineForOffering(List<Offering> offerings, AtomicInteger freeOfferingCount) {
        BigDecimal total = new BigDecimal(0);
        StringBuilder item = new StringBuilder(offerings.get(0).getName());
        if (offerings.get(0).getVolume() != null && !offerings.get(0).getVolume().isEmpty()) {
            item.append(" (").append(offerings.get(0).getVolume()).append(")");
        }
        if (item.length() > MAX_ITEM_LENGTH) {
            item.replace(MAX_ITEM_LENGTH, item.length(), "");
        }
        StringBuilder receiptLine = new StringBuilder();
        if (freeOfferingCount.intValue() > 0) {
            receiptLine.append(String.format(Locale.ENGLISH, LINE_FORMAT, item, Math.min(offerings.size(), freeOfferingCount.intValue()), 0.0, CURRENCY, 0.0, CURRENCY));
        }
        if (offerings.size() > freeOfferingCount.intValue()) {
            BigDecimal offeringTotalSum = offerings.get(0).getPrice()
                    .multiply(new BigDecimal(offerings.size() - freeOfferingCount.intValue()));
            receiptLine.append(String.format(Locale.ENGLISH, LINE_FORMAT, item, offerings.size() - freeOfferingCount.intValue(), offerings.get(0)
                    .getPrice(), CURRENCY, offeringTotalSum, CURRENCY));
            total = total.add(offeringTotalSum);
        }
        freeOfferingCount.getAndAdd(-(Math.min(offerings.size(), freeOfferingCount.get())));

        return new LineResult(receiptLine.toString(), total);
    }

    private record LineResult(String receiptLine, BigDecimal total) {
    }
}
