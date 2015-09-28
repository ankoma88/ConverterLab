package com.ankoma88.converterlab.util;


import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Converter {

    public static String prepareUrl(String link) {
        if (!link.startsWith("www.") && !link.startsWith("http://")) {
            link = "www." + link;
        }
        if (!link.startsWith("http://")) {
            link = "http://" + link;
        }
        return link;
    }

    public static String formatDouble(double number){
        NumberFormat formatter = new DecimalFormat("#0.0000");
        return formatter.format(number);
    }
}
