package com.example.xgramajo.parkme_ids_2018;

import com.example.xgramajo.parkme_ids_2018.home.CounterFragment;

public class ParkingClass {
    private static boolean prepayment;     //tipo de estacionamiento (true - pago adelantado / false - pago diferido)
    private static String patent;          //numero de matricula
    private static int price;              //Precio calculado en Setup / en CounterFragment
    private static String location;        //Ubicación del estacionamiento: latitud + longitud + calle + altura
    private static boolean alreadyPaid;    //Estado del estacionamiento (true - Pagado / false - No pagado)
    private static String time;            //tiempo de estacionamiento pago adelantado

    public static boolean isPrepayment() {
        return prepayment;
    }

    public static void setPrepayment(boolean prepayment) {
        ParkingClass.prepayment = prepayment;
    }

    public static String getPatent() {
        return patent;
    }

    public static void setPatent(String patent) {
        ParkingClass.patent = patent;
    }

    static int getPrice() {
        return price;
    }

    public static void setPrice(int price) {
        ParkingClass.price = price;
    }

    public static String getLocation() {
        return location;
    }

    public static void setLocation(String location) {
        ParkingClass.location = location;
    }

    public static boolean isAlreadyPaid() {
        return alreadyPaid;
    }

    public static void setAlreadyPaid(boolean alreadyPaid) {
        ParkingClass.alreadyPaid = alreadyPaid;
    }

    public static String getTime() {
        return time;
    }

    public static void setTime(String time) {
        ParkingClass.time = time;
    }

    public static String getOnlyNumberTime() {

            switch (time) {
                case "30 minutos":
                    return "30";
                case "1 hora":
                    return "60";
                case "1 hora 30 minutos":
                    return "90";
                case "2 horas":
                    return "120";
                case "2 horas 30 minutos":
                    return "150";
                case "3 horas":
                    return "180";
            }
            return "0";

    }
}
