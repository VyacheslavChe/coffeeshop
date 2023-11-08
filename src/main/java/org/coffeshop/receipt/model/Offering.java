package org.coffeshop.receipt.model;

import java.math.BigDecimal;

public class Offering {
    private String id;
    private String name;
    private OfferingType category;
    private String volume;
    private BigDecimal price;
    private String[] patterns;
    private String[] compatibleExtraOffers;

    public static class Builder {

        private String id;

        private String name;

        private OfferingType category;

        private String volume;

        private BigDecimal price;

        private String[] patterns;

        private String[] compatibleExtraOffers;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setCategory(OfferingType category) {
            this.category = category;
            return this;
        }

        public Builder setVolume(String volume) {
            this.volume = volume;
            return this;
        }

        public Builder setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder setPatterns(String[] patterns) {
            this.patterns = patterns;
            return this;
        }

        public Builder setCompatibleExtraOffers(String[] compatibleExtraOffers) {
            this.compatibleExtraOffers = compatibleExtraOffers;
            return this;
        }

        public Offering createOffering() {
            return new Offering(id, name, category, volume, price, patterns, compatibleExtraOffers);
        }
    }

    private Offering(String id, String name, OfferingType category, String volume, BigDecimal price, String[] patterns,
                    String[] compatibleExtraOffers) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.volume = volume;
        this.price = price;
        this.patterns = patterns;
        this.compatibleExtraOffers = compatibleExtraOffers;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OfferingType getCategory() {
        return category;
    }

    public String getVolume() {
        return volume;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String[] getPatterns() {
        return patterns;
    }

    public String[] getCompatibleExtraOffers() {
        return compatibleExtraOffers;
    }
}
