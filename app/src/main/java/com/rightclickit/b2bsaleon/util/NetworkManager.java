/*
 * Copyright (C) 2014 The Neobric
 *
 * Licensed under the Neobric License.
 * 
 */

package com.rightclickit.b2bsaleon.util;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * NetworkManager is a class to make REST API Calls using JSON.
 *
 * @author venkat
 */
public class NetworkManager {
    URL urlObj = null;
    HttpURLConnection con = null;
    String responseObject = null;

    private final String USER_AGENT = "Mozilla/5.0";
    private final String ACCEPT_LANGUAGE = "en-US,en;q=0.5";
    private final String CONTENT_TYPE = "application/json";
    private final String CONTENT_TYPE_FORM_ENCODE = "application/x-www-form-urlencoded";

    /**
     * Constructor
     */
    public NetworkManager() {

    }

    /**
     * Method to execute post requests.
     *
     * @param url
     * @param params
     * @return String
     */

    public String makeHttpPostConnection(String url, JSONObject params) {
        try {
            urlObj = new URL(url);
            con = (HttpURLConnection) urlObj.openConnection();

            // add reuqest header
            con.setRequestMethod("POST");
            //con.setRequestProperty("User-Agent", USER_AGENT);
            // con.setRequestProperty("Accept-Language", ACCEPT_LANGUAGE);
            con.setRequestProperty("Content-Type", CONTENT_TYPE);

            // Send post request
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(params.toString());
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            printDataInLog("POST Request : ", "URL : " + url);
            printDataInLog("POST Request : ", "Parameters : " + params);
            printDataInLog("POST Request : ", "Response Code : " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                responseObject = response.toString();

                //JSONObject jsonObject = new JSONObject(responseObject);

            } else if (responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
                responseObject = "timeout";
            } else {
                responseObject = "failure";
            }

            // print result
            printDataInLog("POST Response : ", responseObject);

        } catch (Exception e) {
            responseObject = "error";
            e.printStackTrace();
        } finally {
            con.disconnect();
        }

        return responseObject;
    }


    /**
     * Method to execute GET requests.
     *
     * @param url
     * @return String
     */
    public String makeHttpGetConnection(String url) {
        try {
            urlObj = new URL(url);
            con = (HttpURLConnection) urlObj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            // add request header
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", ACCEPT_LANGUAGE);

            int responseCode = con.getResponseCode();
            printDataInLog("GET Request : ", "URL : " + url);
            printDataInLog("GET Request : ", "Response Code : " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                responseObject = response.toString();

                //JSONObject jsonObject = new JSONObject(responseObject);

            } else if (responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
                responseObject = "timeout";
            } else {
                responseObject = "failure";
            }

            // print result
            printDataInLog("GET Response : ", responseObject);

        } catch (Exception e) {
            // TODO: handle exception
            responseObject = "error";
            e.printStackTrace();
        } finally {
            con.disconnect();
        }

        return responseObject;
    }

    public void printDataInLog(String tag, String message) {
        Log.d(tag, message);
    }

    /**
     * Method to execute post requests with url encoded data as input params.
     *
     * @param url
     * @param params
     * @return String
     */

    public String makeHttpPostConnectionWithUrlEncoeContentType(String url, HashMap<String, String> params) {
        try {
            urlObj = new URL(url);
            con = (HttpURLConnection) urlObj.openConnection();

            // add reuqest header
            con.setRequestMethod("POST");
            //con.setRequestProperty("User-Agent", USER_AGENT);
            // con.setRequestProperty("Accept-Language", ACCEPT_LANGUAGE);
            con.setRequestProperty("Content-Type", CONTENT_TYPE_FORM_ENCODE);

            // Send post request
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(getPostDataString(params));
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            printDataInLog("POST Request : ", "URL : " + url);
            printDataInLog("POST Request : ", "Parameters : " + params);
            printDataInLog("POST Request : ", "Response Code : " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                responseObject = response.toString();

                //JSONObject jsonObject = new JSONObject(responseObject);

            } else if (responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
                responseObject = "timeout";
            } else {
                responseObject = "failure";
            }

            // print result
            printDataInLog("POST Response : ", responseObject);

        } catch (Exception e) {
            responseObject = "error";
            e.printStackTrace();
        } finally {
            con.disconnect();
        }

        return responseObject;
    }

    /**
     * Method to execute post requests with url encoded data as input params.
     *
     * @param url
     * @param params
     * @return String
     */

    public String makeHttpPostConnectionWithUrlEncoeContentTypeSettings(String url, HashMap<String, Object> params) {
        try {
            urlObj = new URL(url);
            con = (HttpURLConnection) urlObj.openConnection();

            // add reuqest header
            con.setRequestMethod("POST");
            //con.setRequestProperty("User-Agent", USER_AGENT);
            // con.setRequestProperty("Accept-Language", ACCEPT_LANGUAGE);
            con.setRequestProperty("Content-Type", CONTENT_TYPE_FORM_ENCODE);

            // Send post request
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(getPostDataString2(params));
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            printDataInLog("POST Request : ", "URL : " + url);
            printDataInLog("POST Request : ", "Parameters : " + params);
            printDataInLog("POST Request : ", "Response Code : " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                responseObject = response.toString();

                //JSONObject jsonObject = new JSONObject(responseObject);

            } else if (responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
                responseObject = "timeout";
            } else {
                responseObject = "failure";
            }

            // print result
            printDataInLog("POST Response : ", responseObject);

        } catch (Exception e) {
            responseObject = "error";
            e.printStackTrace();
        } finally {
            con.disconnect();
        }

        return responseObject;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private String getPostDataString1(HashMap<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private String getPostDataString2(HashMap<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    /**
     * Method to execute GET requests.
     *
     * @param url
     * @return json object
     */
    public JSONObject makeHttpGetConnectionWithJsonOutput(String url) {
        JSONObject jObj = null;
        try {
            //String url1 = "http://dev.ppa.neobric.com/mobile/released_version1/login";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            // add request header
            //con.setRequestProperty("User-Agent", USER_AGENT);


            int responseCode = con.getResponseCode();
            printDataInLog("GET Request : ", "URL : " + url);
            printDataInLog("GET Request : ", "Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            jObj = new JSONObject(response.toString());
            // print result
            printDataInLog("GET Request Response : ", response.toString());
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
        return jObj;
    }
}


