package com.example.workv1;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Data {
    private  int coin;
    private String date;
    private int step;

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public int getCoin() {
        return coin;
    }

    public Data(int coin, String date, int step) {
        this.coin = coin;
        this.date = date;
        this.step = step;
    }

    public Data() {
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(Date date) {

        String formattedDate = dateFormat.format( date);
        this.date = formattedDate;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }



}
