package com.wslfinc.cf;

import org.json.JSONObject;

/**
 * @author Wsl_F
 */
public class Constants {

  public static final String BACK_END_URL
    = "https://cf-predictor-compute.wasylf.xyz/";
  // = "https://cf-predictor-backend.herokuapp.com";
  // = "http://cf-predictor-backend.us-west-2.elasticbeanstalk.com";
  // = "http://localhost:8084/CF-PredictorBackEnd/";

  public static final String JSON_FAIL_STRING = "{ \"status\" : \"FAIL\", \"result\":[]}";

  public static final JSONObject JSON_FAIL = new JSONObject(JSON_FAIL_STRING);

  public static final String GITHUB_RATING_URL = "https://codeforcesContests.github.io/RatingAfterRounds";
}
