package com.example.workv1;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SavingDataManagement {

    private static final String  preferenceName="CoinData";
    private static final String  key="KeyData";
    private  final Context context;

    private HashMap<String,Data> dataDataHashMap;



    public SavingDataManagement(Context context) {
        this.context = context;
        dataDataHashMap = loadDataFromLocal();

        if (dataDataHashMap == null) {
            dataDataHashMap = new HashMap<>();
        }
    }

    public HashMap<String, Data> getDataDataHashMap() {
        return dataDataHashMap;
    }

    public void setDataDataHashMap(HashMap<String, Data> dataDataHashMap) {
        this.dataDataHashMap = dataDataHashMap;
    }

    public void saveDataToLocal() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert HashMap<Data, Data> to JSON and store as a string
        Gson gson = new Gson();
        String json = gson.toJson(dataDataHashMap);

        editor.putString(key, json);
        editor.apply();
    }

    public HashMap<String, Data> loadDataFromLocal() {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
            String json = sharedPreferences.getString(key, null);

            if (json != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<HashMap<String, Data>>() {}.getType();
                HashMap<String, Data> dataHashMap = gson.fromJson(json, type);
                return dataHashMap;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null; // No data found or exception occurred
    }

//    public void addStepAndSave(int step) {
//        String formattedDate =Data.dateFormat.format(new Date());
//        Data data=new Data(getCoin(step),formattedDate,step);
//        addDataAndSave(data);
//    }

    public void addStepAndSave(int step) {
        addStepAndSave(step, false);
    }

    public void addStepAndSave(int step,boolean isTimerRunning) {
        String formattedDate =Data.dateFormat.format(new Date());
        Data data=new Data(getCoin(step, isTimerRunning),formattedDate,step);
        addDataAndSave(data);
    }

    public void addDataAndSave(Data newData) {
        // Add the new data to the existing HashMap
//        String formattedDate =Data.dateFormat.format( newData);
        dataDataHashMap.put(newData.getDate(), newData);

        // Save the updated data to SharedPreferences
        saveDataToLocal();
    }


    public  Data getData(Date date)
    {
        String formattedDate = Data.dateFormat.format( date);
       Data data=dataDataHashMap.get(formattedDate);
       if(data==null)
           return new Data(0,formattedDate,0);
        return data;
    }


    public  Data getLastData()
    {
        return getData(new Date());
    }


    public  int getCoin(int step)
    {
        return getCoin(step,false);
    }
    public  int getCoin(int step,boolean isTimerRunning)
    {
        int mul=1;
        if(isTimerRunning) mul=Constants.BOOST_MULITE;
        if(sumOfALlStepLastXDays(Constants.DAYS_IN_MONTH)>=Constants.DAY_GOAL_MONTH)
            return   mul *getCoinInDay(Constants.DAYS_IN_MONTH)+Constants.COINS_FOR_MONTH_GOAL;
        if(sumOfALlStepLastXDays(Constants.DAYS_IN_WEEK)>=Constants.DAY_GOAL_WEEK)
            return mul *getCoinInDay(Constants.DAYS_IN_WEEK)+Constants.COINS_FOR_WEEK_GOAL;
        if(step>Constants.DAY_GOAL_STEP) return getCoinInDay(1)+ Constants.COINS_FOR_DAY_GOAL;
        else
        {
            int x=mul *((int)(step /Constants.SMALL_STEP))*Constants.COINS_FOR_SMALL_STEP;
            return x+getCoinInDay(1);
        }
    }

    public int sumOfALlStepLastXDays(int number) {
        int sum = 0;

        // Get today's date as a string (format: "yyyy-MM-dd")

        Date today = new Date();
        String todayDate = Data.dateFormat.format(today);

        // Calculate the date of "number" days ago
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_YEAR, -number);
        Date xDaysAgoDate = calendar.getTime();
        String xDaysAgoDateString =  Data.dateFormat.format(xDaysAgoDate);


        for (Map.Entry<String, Data> entry : dataDataHashMap.entrySet()) {
            String entryDate = entry.getKey();
            Data entryData = entry.getValue();

            if (entryDate.compareTo(xDaysAgoDateString) >= 0 && entryDate.compareTo(todayDate) <= 0) {
                sum += entryData.getStep();
            }
        }

        return sum;
    }

    public int getCoinInDay(int  dayAgo) {
        Date date=new Date();
       date.setDate(date.getDate()-dayAgo);
        String stringDay = Data.dateFormat.format(date);

        if (dataDataHashMap.containsKey(stringDay)) {
            Data data = dataDataHashMap.get(stringDay);
            return data != null ? data.getCoin() : 0;
        } else {

            return 0; // Default value
        }
    }



}
