package org.gkk.bioshopapp.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
public class Log extends BaseEntity{

    private String username;

    private String description;

    private String propertyId;

    private LocalDateTime time;

    public Log() {
    }

    public Log(String username, String description, String propertyId, LocalDateTime time) {
        this.username = username;
        this.description = description;
        this.propertyId = propertyId;
        this.time = time;
    }

    @Column(name = "username", length = 50, nullable = false, updatable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "description", nullable = false, updatable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "property_id", nullable = false, updatable = false)
    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    @Column(name = "time", nullable = false)
    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
