package org.gkk.bioshopapp.service.model.log;

import java.time.LocalDateTime;

public class LogServiceModel {

    private String username;

    private String description;

    private String propertyId;

    private LocalDateTime time;

    public LogServiceModel() {
    }

    public LogServiceModel(String username, String description, String propertyId, LocalDateTime time) {
        this.username = username;
        this.description = description;
        this.propertyId = propertyId;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
