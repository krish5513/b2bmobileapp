package com.rightclickit.b2bsaleon.models;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.activities.Agents_AddActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetAddressTask extends AsyncTask<String, Void, String> {
    String address,province,country,postalCode,knownName;
    private Agents_AddActivity activity;

    public GetAddressTask(Agents_AddActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(activity, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(params[0]), Double.parseDouble(params[1]), 1);
            if ((addresses == null) || (addresses.size()== 0)) {

                Log.i("addkjgfjygf",addresses+"");

            }

            for(int i=0;i<addresses.size();i++){
                address = addresses.get(i).getAddressLine(i);

                province = addresses.get(i).getAdminArea();
                country = addresses.get(i).getCountryName();
                postalCode = addresses.get(i).getPostalCode();
                knownName = addresses.get(i).getFeatureName();
            }
            //get current Street name
          /*  String address = addresses.get(0).getAddressLine(0);

            //get current province/City
            String province = addresses.get(0).getAdminArea();

            //get country
            String country = addresses.get(0).getCountryName();

            //get postal code
            String postalCode = addresses.get(0).getPostalCode();

            //get place Name
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
*/
            return "Street: " + address + "\n" + "City/Province: " + province + "\nCountry: " + country
                    + "\nPostal CODE: " + postalCode + "\n" + "Place Name: " + knownName;

        } catch (IOException ex) {
            ex.printStackTrace();
            return "IOE EXCEPTION";

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return "IllegalArgument Exception";
        }

    }

    /**
     * When the task finishes, onPostExecute() call back data to Activity UI and displays the address.
     * @param address
     */
    @Override
    protected void onPostExecute(String address) {
        // Call back Data and Display the current address in the UI
        activity.callBackDataFromAsyncTask(address);
    }
}