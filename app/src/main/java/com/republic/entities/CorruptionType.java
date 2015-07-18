package com.republic.entities;

/**
 * Created by Akwasi Owusu on 7/17/15.
 */
//The order is important. Please don't reorder the constants
public enum CorruptionType {

    FRAUD ("Fraud"),
    BRIBERY("Bribery"),
    ABUSE("Abuse of power"),
    WASTE("Waste"),
    EXTORTION("Extortion"),
    EMBEZZLEMENT("Embezzlement"),
    FAVORITISM("Favoritism");

    private final String type;
    private CorruptionType(String type){
        this.type = type;
    }
}
