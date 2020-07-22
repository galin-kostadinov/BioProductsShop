package org.gkk.bioshopapp.service.model.log;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LogServiceModel implements Serializable {

    private String id;

    private String username;

    private String description;

    private String propertyId;

    private LocalDateTime time;

    public LogServiceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
