package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parkinglot")
public class ParkingLot {
    public ParkingLot() {
    }

    public ParkingLot(int id, String name, String address, List<Spot> spotlist) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.spotlist = spotlist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Spot> getSpotlist() {
        return spotlist;
    }

    public void setSpotlist(List<Spot> spotlist) {
        this.spotlist = spotlist;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String address;
    @OneToMany(mappedBy = "parkingLot",cascade = CascadeType.ALL)
    private List<Spot> spotlist=new ArrayList<>();


}
