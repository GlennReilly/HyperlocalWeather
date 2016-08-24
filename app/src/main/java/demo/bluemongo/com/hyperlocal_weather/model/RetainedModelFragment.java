package demo.bluemongo.com.hyperlocal_weather.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import demo.bluemongo.com.hyperlocal_weather.R;


public class RetainedModelFragment extends Fragment implements WeatherModel {
    public static final String TAG = RetainedModelFragment.class.getSimpleName();
    private static final double DUMMY_LATITUDE = -33.865143;
    private static final double DUMMY_LONGITUDE = 151.209900;

    private OnRetainedModelFragmentInteractionListener mListener;
    private double latitude;
    private double longitude;
    private ForecastJsonDTO forecast;
    private ForecastAsyncTask.TaskPayload taskPayload;
    private ForecastAsyncTask forecastAsyncTask;
    private ProgressDialog progressDialog;

    public RetainedModelFragment() {
        // Required empty public constructor
    }

    @Override
    public ForecastJsonDTO getForecastResult() {
        return forecast;
    }

    @Override
    public void postForecastResult(ForecastJsonDTO forecastResult) {
        forecast = forecastResult;
        if (mListener != null) {
            mListener.updateForecastView(forecastResult);
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateDeviceLocationAndForecast();
    }


    @Override
    public ForecastAsyncTask.TaskPayload getTaskPayload() {
        return taskPayload;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    @Override
    public void requestForecastForCurrentLocation() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(getActivity(), getString(R.string.dialogTitle),
                    getString(R.string.dialogMessage), true);
        }
        taskPayload = new ForecastAsyncTask.TaskPayload(this);
        taskPayload.setLatitude(getLatitude());
        taskPayload.setLongitude(getLongitude());
        taskPayload.setForecastUri(getForecastUri());
        forecastAsyncTask = new ForecastAsyncTask();
        forecastAsyncTask.execute(taskPayload);
    }

    @Override
    public void populateDeviceLocationAndForecast() {
        String locationProvider = LocationManager.NETWORK_PROVIDER;

        final long MIN_MINUTE_TIME_BETWEEN_UPDATES = 180000;  // 3 minute
        final long MIN_METRE_DISTANCE_BETWEEN_UPDATES = 300; // 300 metres

        final LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        boolean network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (network_enabled) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            if (lastKnownLocation != null){
                setLatitude(lastKnownLocation.getLatitude());
                setLongitude(lastKnownLocation.getLongitude());
            }

            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null){
                        setLatitude(location.getLatitude());
                        setLongitude(location.getLongitude());
                        requestForecastForCurrentLocation();
                        Log.i(TAG, "Location changed, updating");
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.i(TAG, "Location onStatusChanged");
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.i(TAG, "Location onProviderEnabled");
                }

                @Override
                public void onProviderDisabled(String provider) {
                    locationManager.removeUpdates(this);
                }
            };

            locationManager.requestLocationUpdates(locationProvider, MIN_MINUTE_TIME_BETWEEN_UPDATES, MIN_METRE_DISTANCE_BETWEEN_UPDATES, locationListener);
        }
        else {
            setLatitude(DUMMY_LATITUDE);
            setLongitude(DUMMY_LONGITUDE);
        }
        requestForecastForCurrentLocation();
    }



    @Override
    public void setModelForecastFromNetwork() {
        populateDeviceLocationAndForecast();
    }



    @Nullable
    private URL getForecastUri() {
        String api_key = getString(R.string.api_key);
        final String FORECAST_BASE_URL = "https://api.forecast.io/forecast/";
        URL url = null;
        Uri forecastUri = Uri.parse(FORECAST_BASE_URL)
                .buildUpon()
                .appendPath(api_key)
                .appendPath(getLatitude() + "," + getLongitude()).build();
        try {
            url = new URL(forecastUri.toString());
            Log.i(TAG,"forecastUri: " + forecastUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG,e.getMessage());
            return null;
        }
        return url;
    }


    public interface OnRetainedModelFragmentInteractionListener {
        void updateForecastView(ForecastJsonDTO forecastForThisLatLong);
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnRetainedModelFragmentInteractionListener) {
            mListener = (OnRetainedModelFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRetainedModelFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        forecastAsyncTask.cancel(true);
    }
}
