package com.medicalwale.gniapp;

public class AppConfiguration {

    public static boolean isCertificate = false;
    public static boolean isLive = true;
    public static String MAIN_BASE_URL = isLive ? "https://live.medicalwale.com/" : "http://sandboxapi.medicalwale.com/";
    public static String VERSION = isLive ? "v55/" : "v47/";
    public static String MAIN_URL_AUTH = MAIN_BASE_URL + VERSION + "auth/";
    public static String MAIN_URL_INDEX = MAIN_BASE_URL + VERSION;
    public static final String TOKEN = "25iwFyq/LSO1U";

}