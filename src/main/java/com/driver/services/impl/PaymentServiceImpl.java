package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.model.SpotType;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation=reservationRepository2.findById(reservationId).get();
        int amount=reservation.getNumberOfHours()*reservation.getSpot().getPricePerHour();
        if(amount>amountSent){
            throw new Exception("Insufficient Amount");
        }
        String sp= String.valueOf(SpotType.FOUR_WHEELER);
        if (!(mode==PaymentMode.CARD.toString() || mode==PaymentMode.UPI.toString() || mode==PaymentMode.CASH.toString())){
            throw new Exception("Payment mode not detected");
        }

        PaymentMode paymentMode;
        if (mode==PaymentMode.CASH.toString()){
            paymentMode=PaymentMode.CASH;
        }
        else if (mode==PaymentMode.CARD.toString()){
            paymentMode=PaymentMode.CARD;
        }
        else {
            paymentMode=PaymentMode.UPI;
        }

        Payment payment=new Payment();
        payment.setPaymentCompleted(true);
        payment.setPaymentMode(paymentMode);
        payment.setReservation(reservation);
        reservation.setPayment(payment);

        reservationRepository2.save(reservation);
        Payment payment1=paymentRepository2.save(payment);

        return payment1;
    }
}
