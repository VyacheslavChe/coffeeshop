package org.coffeshop.receipt;

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

    public static void main(String[] args) {
        List<Offering> configuredOfferings = ConfigurationLoader.loadOfferingConfiguration("offerings.csv");

        System.out.println("Enter user offering (one by one). Enter empty string for finishing");
        var requestedOfferings = new ArrayList<String>();
        Scanner userInputScanner = new Scanner(System.in);
        for (String userInput = userInputScanner.nextLine(); !userInput.isEmpty(); userInput = userInputScanner.nextLine()) {
            requestedOfferings.add(userInput);
        }

        var client = new Client(0L);
        var offeringService = new OfferingService(configuredOfferings);
        var clientService = new ClientService();
        var receiptService = new ReceiptService(clientService);

        var parsedOfferings = new ArrayList<Offering>();

        try {
            for (var offering : requestedOfferings) {
                parsedOfferings.addAll(offeringService.parseOfferingString(offering));
            }
            System.out.println(receiptService.formatReceipt(client, parsedOfferings));
            System.out.println("Stamps earned: " + client.getStampCount());
        } catch (OfferingParsingException e) {
            System.out.println(e.getMessage());
        }

    }
}
