package com.jakubjirak.dpcpp.enterprise;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.*;

/**
 * Enterprise License Manager
 * Handles license validation, seat management, and feature access control.
 */
public class LicenseManager {
    private static final Logger LOG = Logger.getInstance(LicenseManager.class);
    
    private LicenseType currentLicense;
    private LocalDate expirationDate;
    private int maxSeats;
    private Set<String> activeUsers;
    
    public LicenseManager() {
        this.currentLicense = LicenseType.COMMUNITY;
        this.activeUsers = new HashSet<>();
    }
    
    public enum LicenseType {
        COMMUNITY("Community", 0, Set.of("syntax", "completion", "templates")),
        PROFESSIONAL("Professional", 99, Set.of("debugging", "profiling", "ai", "cloud")),
        ACADEMIC("Academic", 50, Set.of("debugging", "profiling", "ai", "cloud")),
        ENTERPRISE("Enterprise", 499, Set.of("debugging", "profiling", "ai", "cloud", "team", "sso"));
        
        private final String displayName;
        private final int annualPriceUSD;
        private final Set<String> features;
        
        LicenseType(String displayName, int annualPriceUSD, Set<String> features) {
            this.displayName = displayName;
            this.annualPriceUSD = annualPriceUSD;
            this.features = features;
        }
        
        public boolean hasFeature(String feature) {
            return features.contains(feature);
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public int getAnnualPrice() {
            return annualPriceUSD;
        }
    }
    
    public boolean validateLicense(@NotNull String licenseKey) {
        LOG.info("Validating license key");
        // TODO: Implement actual validation logic
        return licenseKey.startsWith("DPCPP-");
    }
    
    public boolean hasFeature(String feature) {
        return currentLicense.hasFeature(feature);
    }
    
    public LicenseType getCurrentLicense() {
        return currentLicense;
    }
    
    public void setLicense(LicenseType license, LocalDate expiration, int seats) {
        this.currentLicense = license;
        this.expirationDate = expiration;
        this.maxSeats = seats;
        LOG.info("License activated: " + license.getDisplayName());
    }
    
    public boolean isExpired() {
        return expirationDate != null && LocalDate.now().isAfter(expirationDate);
    }
    
    public String getLicenseInfo() {
        StringBuilder info = new StringBuilder();
        info.append("License: ").append(currentLicense.getDisplayName()).append("\n");
        if (expirationDate != null) {
            info.append("Expires: ").append(expirationDate).append("\n");
        }
        if (maxSeats > 0) {
            info.append("Seats: ").append(activeUsers.size()).append("/").append(maxSeats).append("\n");
        }
        return info.toString();
    }
}
