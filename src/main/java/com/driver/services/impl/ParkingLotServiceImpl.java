package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        // create parking lot
        ParkingLot parkingLot=new ParkingLot();
        parkingLot.setAddress(address);
        parkingLot.setName(name);

        // saving to repository
        ParkingLot savedParkingLot=parkingLotRepository1.save(parkingLot);
        return savedParkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();
        Spot spot=new Spot();
        SpotType spotType;
        if(numberOfWheels<=2){
            spotType=SpotType.TWO_WHEELER;
        }
        else if(numberOfWheels<=4){
            spotType=SpotType.FOUR_WHEELER;
        }
        else {
            spotType=SpotType.OTHERS;
        }


        spot.setOccupied(false);
        spot.setSpotType(spotType);
        spot.setPricePerHour(pricePerHour);
        spot.setParkingLot(parkingLot);

        parkingLot.getSpotList().add(spot);

        ParkingLot parkingLot1=parkingLotRepository1.save(parkingLot);
        return parkingLot1.getSpotList().get(parkingLot1.getSpotList().size()-1);
    }

    @Override
    public void deleteSpot(int spotId) {
        Spot spot=spotRepository1.findById(spotId).get();
        ParkingLot parkingLot=spot.getParkingLot();
        List<Spot> spotList=parkingLot.getSpotList();

        int co=0;
        for (Spot spot1:spotList){
            if (spot1.getId()==spot.getId()){
                spotList.remove(co);
                break;
            }
            co++;
        }
        parkingLot.setSpotList(spotList);

        parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot=parkingLotRepository1.findById(parkingLotId).get();
        Spot spot=spotRepository1.findById(spotId).get();
        spot.setPricePerHour(pricePerHour);
        spot.setParkingLot(parkingLot);

        parkingLot.getSpotList().add(spot);
        Spot spot1=spotRepository1.save(spot);
        return spot1;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
