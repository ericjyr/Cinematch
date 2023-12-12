package com.cm.cinematchapp.constants;

/**
 * The `EntityConstants` interface provides a set of constant values that are used throughout the application
 * for defining constraints, keys, and other entity-related configurations.
 */
public interface EntityConstants {
    static final int kMaxNameLen = 16;

    static final String kFirstNameLenViolation = "First name cannot exceed " + EntityConstants.kMaxNameLen + " characters.";

    static final String kLastNameLenViolation = "Last name cannot exceed " + EntityConstants.kMaxNameLen + " characters.";
    static final int kMinUsernameLen = 6;
    static final int kMaxUsernameLen = 16;

    static final String kUsernameLenViolation = "Username must be between " + kMinUsernameLen + " and " + kMaxUsernameLen;

    //used for JWT token generation and verification.
    static final String kSecuritySignKey = "E24Sxcw1SQasd3DSAds3";

    //60 minutes time for session
    static final int kSessionTimeout = 1000 * 60 * 60;

    static final String kAvatarPath = System.getProperty("user.dir") + "/backend/avatars/";
    static final String kDefaultAvatar = System.getProperty("user.dir") + "/backend/avatars/default_avatar.png";

    static final String kPostersPath = System.getProperty("user.dir") + "/backend/posters/";


    static final String kRapidApiUrl = "https://streaming-availability.p.rapidapi.com/search/title";
    static final String kRapidApiKey = "a05a1b2455msh8071b1add6463dap15f40fjsncf719cc3c3fc";
    static final String kRapidApiHost = "streaming-availability.p.rapidapi.com";

    static final String kOMDBApiHost = "http://www.omdbapi.com/?t=";

    static final String kOMDBApiKey = "&apikey=1452cc8c";






}
