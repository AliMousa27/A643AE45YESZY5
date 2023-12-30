package com.toothtrek.notifications.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "office")
@Data
public class Office {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "office_id_seq")
    @SequenceGenerator(name = "office_id_seq", sequenceName = "office_id_seq", allocationSize = 1)
    private Long id;
    private String name;
    private String address;
    private float latitude;
    private float longitude;

    /**
     * Office constructor
     * 
     * @param name      name of office
     * @param address   address of office
     * @param longitude longitude of office
     * @param latitude  latitude of office
     */
    public Office(String name, String address, float latitude, float longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Office() {
    }
}
