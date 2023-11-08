package org.coffeshop.receipt;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.coffeshop.receipt.config.ConfigurationLoader;
import org.coffeshop.receipt.exceptions.OfferingParsingException;
import org.coffeshop.receipt.model.Client;
import org.coffeshop.receipt.model.Offering;
import org.coffeshop.receipt.service.ClientService;
import org.coffeshop.receipt.service.OfferingService;
import org.coffeshop.receipt.service.ReceiptService;

/**
 * Simple application to producing receipt for selected offerings
 * Input could be accepted in interactive mode (-i) or in batch mode from the file (-f)
 */
public class ReceiptPrinterApp {
    private static final String USAGE = "Possible options: -c <current stamp count> -f <filename for order information>";

    public static void main(String[] args) {
        var client = new Client(0L);
        String orderFileName = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-f":
                    if (i + 1 == args.length) {
                        displayUsage();
                        return;
                    }
                    orderFileName = args[i + 1];
                    i++;
                    break;
                case "-c":
                    if (i + 1 == args.length) {
                        displayUsage();
                        return;
                    }
                    try {
                        Long clientStampCount = Long.parseLong(args[i + 1]);
                        client.setStampCount(clientStampCount);
                        i++;
                    } catch (Exception ex) {
                        displayUsage();
                        return;
                    }
                    break;
                default:
                    displayUsage();
                    return;
            }
        }

        List<Offering> configuredOfferings = ConfigurationLoader.loadOfferingConfiguration("offerings.csv");

        Scanner userInputScanner;
        var requestedOfferings = new ArrayList<String>();
        var currentStamps = client.getStampCount();
        if (orderFileName == null) {
            System.out.println("Enter user offering (one by one). Enter empty string for finishing");
            userInputScanner = new Scanner(System.in);
        } else {
            try {
                userInputScanner = new Scanner(Path.of(orderFileName));
            } catch (IOException e) {
                System.out.println("Can not read file with order: " + orderFileName);
                return;
            }
        }
        for (String userInput = userInputScanner.nextLine(); !userInput.isEmpty(); userInput = userInputScanner.nextLine()) {
            requestedOfferings.add(userInput);
        }
        if (orderFileName != null) {
            userInputScanner.close();
        }

        var offeringService = new OfferingService(configuredOfferings);
        var clientService = new ClientService();
        var receiptService = new ReceiptService(clientService);

        var parsedOfferings = new ArrayList<Offering>();

        try {
            for (var offering : requestedOfferings) {
                parsedOfferings.addAll(offeringService.parseOfferingString(offering));
            }
            System.out.println(receiptService.formatReceipt(client, parsedOfferings));
            System.out.println("Stamps earned: " + (client.getStampCount() - currentStamps));
        } catch (OfferingParsingException e) {
            System.out.println(e.getMessage());
        }

    }

    private static void displayUsage() {
        System.out.println(USAGE);
    }
}
