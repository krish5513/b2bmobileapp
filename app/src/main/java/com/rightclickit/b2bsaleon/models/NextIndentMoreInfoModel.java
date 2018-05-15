package com.rightclickit.b2bsaleon.models;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rightclickit.b2bsaleon.activities.AgentTDC_Order;
import com.rightclickit.b2bsaleon.activities.DashboardActivity;
import com.rightclickit.b2bsaleon.activities.NextIndent_Moreinfo;
import com.rightclickit.b2bsaleon.activities.Products_Activity;
import com.rightclickit.b2bsaleon.beanclass.DashboardPendingIndentBean;
import com.rightclickit.b2bsaleon.beanclass.Nextdayindent_moreinfoBeen;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomProgressDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.OnAsyncRequestCompleteListener;
import com.rightclickit.b2bsaleon.util.AsyncRequest;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by PPS on 10/10/2017.
 */

public class NextIndentMoreInfoModel implements OnAsyncRequestCompleteListener {
    public static final String TAG = Products_Activity.class.getSimpleName();

    private Context context;
    private NextIndent_Moreinfo activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private JSONArray routesArray;
    private String type = "";
    private ArrayList<String> regionIdsList = new ArrayList<String>();

    private ArrayList<Nextdayindent_moreinfoBeen> MoreinfoBeanList = new ArrayList<Nextdayindent_moreinfoBeen>();
    private String mStock = "", mAgentPrice = "", mRetailerPrice = "", mConsumerPrice = "";
    private String selecteddate = "", backDate = "";

    private HashMap<String, JSONArray> milkDatelist = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> milkVolumelist = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> curdDatelist = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> curdVollist = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> othersDatelist = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> othersVollist = new HashMap<String, JSONArray>();


    public NextIndentMoreInfoModel(Context context, NextIndent_Moreinfo activity) {
        this.context = context;
        this.activity = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");


        cal.add(Calendar.DATE, -20);
        backDate = df.format(cal.getTime());


        //selecteddate = mPreferences.getString("selectedDate");


    }

    public void getReportsData(String s,JSONArray idsarray) {
        try {
            selecteddate = s;
            routesArray=idsarray;
            if (MoreinfoBeanList.size() > 0) {
                MoreinfoBeanList.clear();
            }
            if (milkDatelist.size() > 0) {
                milkDatelist.clear();
            }
            if (milkVolumelist.size() > 0) {
                milkVolumelist.clear();
            }
            if (curdDatelist.size() > 0) {
                curdDatelist.clear();
            }
            if (curdVollist.size() > 0) {
                curdVollist.clear();
            }

            if (othersDatelist.size() > 0) {
                othersDatelist.clear();
            }

            if (othersVollist.size() > 0) {
                othersVollist.clear();
            }


            if (new NetworkConnectionDetector(context).isNetworkConnected()) {

                /*HashMap<String, String> userMapData = mDBHelper.getUsersData();

                JSONObject routesJob = new JSONObject(userMapData.get("route_ids").toString());
                routesArray = routesJob.getJSONArray("routeArray");*/
                String reportsURL = String.format("%s%s", Constants.MAIN_URL, Constants.GET_DASHBOARD_NEXTINDENTMOREINFO_URL);
                JSONObject params = new JSONObject();
                params.put("route_ids", routesArray);
                params.put("from_date", backDate);
                params.put("to_date", selecteddate);
               System.out.println("MOREINFO URL::: " + reportsURL);
                System.out.println("MOREINFO DATA::: " + params.toString());
                AsyncRequest routeidRequest = new AsyncRequest(context, this, reportsURL, AsyncRequest.MethodType.POST, params);
                routeidRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void asyncResponse(String response, Constants.RequestCode requestCode) {
        try {

            JSONArray milkDateArray = null, milkVolArray = null,curdDateArray=null,curdVolArray=null,othersDateArray=null,othersVolArray=null;
            CustomProgressDialog.hideProgressDialog();
            HashMap<String, NextIndentMoreInfoModel> map = new HashMap<>();
            HashMap<String, Double> mapMilk = new HashMap<>();
            HashMap<String, Double> mapCurd = new HashMap<>();
            HashMap<String, Double> mapOthers = new HashMap<>();

            JSONArray responseObj = new JSONArray(response);
            int lengthh = responseObj.length();
            JSONObject milkjob = responseObj.getJSONObject(0);
            JSONObject curdjob = responseObj.getJSONObject(1);
            JSONObject othersjob = responseObj.getJSONObject(2);

            Nextdayindent_moreinfoBeen moreinfoBeen = new Nextdayindent_moreinfoBeen();
            ArrayList<Nextdayindent_moreinfoBeen> moreinfoBeenList = new ArrayList<>();

            if(!milkjob.get("date").equals(null) && !milkjob.get("vol").equals(null)){
                milkDateArray = milkjob.getJSONArray("date");
                milkVolArray = milkjob.getJSONArray("vol");

                for(int i=0; i<milkDateArray.length(); i++){
                    String str = (String) milkDateArray.opt(i);
                    double doubleVal = (double) milkVolArray.opt(i);
                   // mapMilk.put(str,doubleVal);

                    moreinfoBeen = new Nextdayindent_moreinfoBeen();
                    moreinfoBeen.setDate(str);
                    moreinfoBeen.setMilkVol(doubleVal+"");
                    moreinfoBeen.setCurdVol(0+"");
                    moreinfoBeen.setOtherVol(0+"");
                    moreinfoBeenList.add(moreinfoBeen);
                }
            }

            if(!curdjob.get("date").equals(null) && !curdjob.get("vol").equals(null)){
                curdDateArray = curdjob.getJSONArray("date");
                curdVolArray = curdjob.getJSONArray("vol");

                for(int i=0; i<curdDateArray.length(); i++){
                    String str = (String) curdDateArray.opt(i);
                    double doubleVal = (double) curdVolArray.opt(i);
                    if(moreinfoBeenList.size()>0){
                        for(int j=0; j<moreinfoBeenList.size(); j++){
                            if(moreinfoBeenList.get(j).getDate().equals(str)){
                                moreinfoBeenList.get(j).setCurdVol(doubleVal+"");
                                /*moreinfoBeenList.get(j).setMilkVol(doubleVal+"");
                                moreinfoBeenList.get(j).setOtherVol(doubleVal+"");*/
                            }else{
                                moreinfoBeen = new Nextdayindent_moreinfoBeen();
                                moreinfoBeen.setDate(str);
                                moreinfoBeen.setCurdVol(doubleVal+"");
                                moreinfoBeen.setMilkVol(0+"");
                                moreinfoBeen.setOtherVol(0+"");
                                moreinfoBeenList.add(moreinfoBeen);
                            }
                        }
                    }else{
                        moreinfoBeen = new Nextdayindent_moreinfoBeen();
                        moreinfoBeen.setDate(str);
                        moreinfoBeen.setCurdVol(doubleVal+"");
                        moreinfoBeen.setMilkVol(0+"");
                        moreinfoBeen.setOtherVol(0+"");
                        moreinfoBeenList.add(moreinfoBeen);
                    }
                }
            }/*else{
                for(int j=0; j<moreinfoBeenList.size(); j++){
                        moreinfoBeenList.get(j).setCurdVol(0+"");
                }
            }*/

            if(!othersjob.get("date").equals(null) && !othersjob.get("vol").equals(null)){
                othersDateArray = othersjob.getJSONArray("date");
                othersVolArray = othersjob.getJSONArray("vol");

                for(int i=0; i<othersDateArray.length(); i++){
                    String str = (String) othersDateArray.opt(i);
                    double doubleVal = (double) othersVolArray.opt(i);
                    if(moreinfoBeenList.size()>0){
                        for(int j=0; j<moreinfoBeenList.size(); j++){
                            if(moreinfoBeenList.get(j).getDate().equals(str)){
                                moreinfoBeenList.get(j).setOtherVol(doubleVal+"");
                            }else{
                                moreinfoBeen = new Nextdayindent_moreinfoBeen();
                                moreinfoBeen.setDate(str);
                                moreinfoBeen.setOtherVol(doubleVal+"");
                                moreinfoBeen.setMilkVol(0+"");
                                moreinfoBeen.setCurdVol(0+"");
                                moreinfoBeenList.add(moreinfoBeen);
                            }
                        }
                    }else{
                        moreinfoBeen = new Nextdayindent_moreinfoBeen();
                        moreinfoBeen.setDate(str);
                        moreinfoBeen.setOtherVol(doubleVal+"");
                        moreinfoBeen.setMilkVol(0+"");
                        moreinfoBeen.setCurdVol(0+"");
                        moreinfoBeenList.add(moreinfoBeen);
                    }

                }
            }/*else{
                for(int j=0; j<moreinfoBeenList.size(); j++){
                    moreinfoBeenList.get(j).setOtherVol(0+"");
                }
            }*/

            for(int i=0; i<moreinfoBeenList.size(); i++){
                Log.i(moreinfoBeenList.get(i).getDate()+"",moreinfoBeenList.get(i).getMilkVol()+"");
                Log.i(moreinfoBeenList.get(i).getDate()+"",moreinfoBeenList.get(i).getCurdVol()+"");
                Log.i(moreinfoBeenList.get(i).getDate()+"",moreinfoBeenList.get(i).getOtherVol()+"");
            }





            synchronized (this) {

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateandTime = sdf.format(new Date());
                String currentDate = sdf1.format(new Date());


                for(int i=0; i<moreinfoBeenList.size(); i++){
                    mDBHelper.insertPendingIndentMoreInfoDetails(moreinfoBeenList.get(i),moreinfoBeenList.get(i).getDate());
                }


            }

            synchronized (this) {
                activity.loadmoreInfo(moreinfoBeenList);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

