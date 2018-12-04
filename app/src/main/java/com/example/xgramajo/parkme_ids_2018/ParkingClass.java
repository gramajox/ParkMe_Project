package com.example.xgramajo.parkme_ids_2018;

import android.widget.Toast;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ParkingClass {
    private static boolean prepayment;     //tipo de estacionamiento (true - pago adelantado / false - pago diferido)
    private static String patent;          //numero de matricula
    private static int price;              //Precio calculado en Setup / en CounterFragment
    private static String direccion;        //Ubicación del estacionamiento: calle + altura
    private static double lat, lng;         //Ubicación del estacionamiento: Latitud + Longitud
    private static boolean alreadyPaid;    //Estado del estacionamiento (true - Pagado / false - No pagado)
    private static String time;            //tiempo de estacionamiento pago adelantado
    private static boolean setup=false;
    private static String startTime;          // Tiempo de inicio del estacionamiento
    private static String endTime;            // Tiempo de fin del estacionamiento

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

    public static void setDireccion(String d) {
        ParkingClass.direccion = d;
    }

    public static String getDireccion() {
        return direccion;
    }

    public static void setLatLng(Double la, Double lo){
        ParkingClass.lat = la;
        ParkingClass.lng = lo;
    }

    public static Double getLat(){
        return lat;
    }

    public static Double getLng(){
        return lng;
    }

    public static boolean isAlreadyPaid() {
        return alreadyPaid;
    }

    public static void setAlreadyPaid(boolean alreadyPaid) {
        ParkingClass.alreadyPaid = alreadyPaid;
    }

    public static Boolean getSetup(){return setup;}

    public static void setupTrue(){setup = true;}

    public static void setupFalse(){setup = false;}


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

    public static long getMinutes (){
        long longNumberTime = Long.valueOf(getOnlyNumberTime());
        return longNumberTime;
    }

    public static long getParkingTimeInMillis (){
        long minutes = getMinutes();
        long milliseconds = minutes * 60 * 1000;
        return milliseconds;
    }

    public static long getTotalTimeInMillis (){
        long totalTimeInMillis = getTimeInMillis() + getParkingTimeInMillis();
        return totalTimeInMillis;
    }

    public static String getEndTime(){
        endTime = convertMillisToString(getTotalTimeInMillis());
        return endTime;
    }

    public static Date getDate() {
        Date date = new Date();
        return date; // Tiempo actual del sistema en formato Day Month Date hh:mm:ss Zone Year
    }

    public static Timestamp getTimeStamp(){
        Date date = new Date();
        Timestamp timeStamp = (new Timestamp(date.getTime()));
        return timeStamp; // Tiempo actual del sistema en formato aaaa-mm-dd hh:mm:ss.mmm
    }

    public static long getTimeInMillis() {
        Timestamp myTimeStamp = getTimeStamp();
        long timeInMillis = myTimeStamp.getTime();
        return timeInMillis;
    }

    public static String getStartTime(){
        startTime = convertMillisToString(getTimeInMillis());
        return startTime;
    }


    public static String convertMillisToString (long milliSeconds) {

        /**
         * Return date in specified format.
         * @param milliSeconds Date in milliseconds
         * @return String representing date in specified format
         */

        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
        // Nota: la cadena de String como pattern la podemos reemplazar por una variable con el pattern que nosotros querramos

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        String formattedDate = formatter.format(calendar.getTime());
        return formattedDate;
    }


}
