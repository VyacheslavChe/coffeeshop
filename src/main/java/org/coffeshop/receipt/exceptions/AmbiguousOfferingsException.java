package org.coffeshop.receipt.exceptions;

public class AmbiguousOfferingsException extends OfferingParsingException {

    public AmbiguousOfferingsException(String message) {
        super(message);
    }
}
