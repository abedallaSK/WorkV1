package com.example.workv1

class Constants {
    companion object {

        //DAYS
        const val DAYS_IN_WEEK = 7
        const val DAYS_IN_MONTH = 30


        //STEPS
        const val SMALL_STEP = 1000
        const val DAY_GOAL_STEP = 10 *SMALL_STEP
        const val DAY_GOAL_WEEK = DAY_GOAL_STEP *DAYS_IN_WEEK
        const val DAY_GOAL_MONTH = DAY_GOAL_STEP *DAYS_IN_MONTH

        //COINS
        const val COINS_FOR_SMALL_STEP = 1
        const val COINS_FOR_DAY_GOAL = 10
        const val COINS_FOR_WEEK_GOAL = 100
        const val COINS_FOR_MONTH_GOAL = 1000
        
        
        //BOOST
        const val BOOST_TIME=30L
        const val BOOST_MULITE=2


    }
}