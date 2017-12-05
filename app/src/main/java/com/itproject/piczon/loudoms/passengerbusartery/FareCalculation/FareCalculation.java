package com.itproject.piczon.loudoms.passengerbusartery.FareCalculation;

/**
 * Created by LouDoms on 12/5/2017.
 */

public class FareCalculation {

    private FareCalculationListener listener;
    private String distance, type, accommodation;
    private int numberOfSeats, minFare = 20, fare;

    public FareCalculation(FareCalculationListener listener, String distance, String type,
                           String accommodation, int numberOfSeats) {
        this.listener = listener;
        this.distance = distance;
        this.type = type;
        this.accommodation = accommodation;
        this.numberOfSeats = numberOfSeats;
    }

    public void execute(){
        calculateFare();
    }

    private void calculateFare() {
        String[] parts = distance.split(" ");
        String part = parts[0];

        final double distanceValue = Double.parseDouble(part);
        //1peso per km

        if ((type.equals("PWD")) || (type.equals("Senior Citizen"))) {
//1.20/km * .20 of the discount
            if (accommodation.equals("Ordinary")) {
                if (numberOfSeats == 1) {
                    fare = (int) Math.ceil((distanceValue * 1.20) - (distanceValue * 1.20 * .2));
                    if (fare < 20) {;
                        listener.onFareCalculate(minFare);
                    } else {
                        listener.onFareCalculate(fare);
                    }
                } else {
                    fare = (int) Math.ceil(((distanceValue * 1.20) - (distanceValue * 1.20 * .2)) + ((distanceValue * 1.20) * (numberOfSeats - 1)));
                    listener.onFareCalculate(fare);
                }
            } else {
                if (numberOfSeats == 1) {
                    fare = (int) Math.ceil((distanceValue * 1.50) - (distanceValue * 1.50 * .2));
                    if (fare < 60) {
                        listener.onFareCalculate(60);
                    } else {
                        listener.onFareCalculate(fare);
                    }
                } else {
                    fare = (int) Math.ceil(((distanceValue * 1.50) - (distanceValue * 1.50 * .2)) + ((distanceValue * 1.50) * (numberOfSeats - 1)));
                    listener.onFareCalculate(fare);
                }
            }
        } else if (type.equals("Student")) {
//0.96/km
            if (accommodation.equals("Ordinary")) {
                if (numberOfSeats == 1) {
                    fare = (int) Math.ceil(distanceValue * .96);
                    if (fare < 20) {
                        listener.onFareCalculate(minFare);
                    } else {
                        listener.onFareCalculate(fare);
                    }
                } else {
                    fare = (int) Math.ceil((distanceValue * .96) + ((distanceValue * 1.20) * (numberOfSeats - 1)));
                    listener.onFareCalculate(fare);
                }
            } else {
                if (numberOfSeats == 1) {
                    fare = (int) Math.ceil((distanceValue * 1.50));
                    if (fare < 60) {
                        listener.onFareCalculate(60);
                    } else {
                        listener.onFareCalculate(fare);
                    }
                } else {
                    fare = (int) Math.ceil(((distanceValue * 1.50) + ((distanceValue * 1.50) * (numberOfSeats - 1))));
                    listener.onFareCalculate(fare);
                }
            }
        } else {
//1.20/km
            if (accommodation.equals("Ordinary")) {
                if (numberOfSeats == 1) {
                    fare = (int) Math.ceil(distanceValue * 1.20);
                    if (fare < 20) {
                        listener.onFareCalculate(minFare);
                    } else {
                        listener.onFareCalculate(fare);
                    }
                } else {
                    fare = (int) Math.ceil(distanceValue * 1.20 * numberOfSeats);
                    listener.onFareCalculate(fare);
                }
            } else {
                if (numberOfSeats == 1) {
                    fare = (int) Math.ceil((distanceValue * 1.50));
                    if (fare < 60) {
                        listener.onFareCalculate(60);
                    } else {
                        listener.onFareCalculate(fare);
                    }
                } else {
                    fare = (int) Math.ceil(((distanceValue * 1.50) + ((distanceValue * 1.50) * (numberOfSeats - 1))));
                    listener.onFareCalculate(fare);
                }
            }
        }

    }
}
