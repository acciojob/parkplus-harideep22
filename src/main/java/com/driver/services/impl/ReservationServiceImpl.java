package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    public int convert(SpotType spotType){
        if(spotType==SpotType.TWO_WHEELER){
            return 2;
        } else if (spotType==SpotType.FOUR_WHEELER) {
            return 4;
        }
        else return Integer.MAX_VALUE;
    }

    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        ParkingLot parkingLot;
        User user;
        try{
            parkingLot=parkingLotRepository3.findById(parkingLotId).get();
        }
        catch (Exception e){
            throw new Exception("Cannot make reservation");
        }
        try {
            user=userRepository3.findById(userId).get();
        }
        catch (Exception e){
            throw new Exception("Cannot make reservation");
        }
        List<Spot> spotList=parkingLot.getSpotlist();
        if(spotList.size()==0){
            throw new Exception("Cannot make reservation");
        }






        int c=0;
        for(Spot spot:spotList){
            if(spot.getOccupied()==false && (convert(spot.getSpotType())>numberOfWheels)){
                c++;
            }
        }
        if(c==0){
            throw new Exception("Cannot make reservation");
        }



        Spot bookingSpot = null;
        for (Spot spot:spotList){
            if (spot.getOccupied()==false && (convert(spot.getSpotType())>numberOfWheels)){
                if(bookingSpot==null){
                    bookingSpot=spot;
                }
                else if (bookingSpot.getPricePerHour()>spot.getPricePerHour()){
                    bookingSpot=spot;
                }
            }
        }
        Reservation reservation=new Reservation();
        reservation.setSpot(bookingSpot);
        reservation.setUser(user);
        reservation.setNumberOfHours(timeInHours);

//        Payment payment=new Payment();
//        payment.setPaymentCompleted(false);
//        payment.setReservation(reservation);

        user.getReservationList().add(reservation);
        bookingSpot.getReservationList().add(reservation);
        userRepository3.save(user);
        spotRepository3.save(bookingSpot);

        Reservation saveReservation=reservationRepository3.save(reservation);

        return saveReservation;
    }
}
