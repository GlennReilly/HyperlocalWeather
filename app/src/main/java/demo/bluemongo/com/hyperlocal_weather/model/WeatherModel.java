package demo.bluemongo.com.hyperlocal_weather.model;

/**
 * Created by glenn on 18/06/16.
 */
public interface WeatherModel {
    void populateDeviceLocationAndForecast();

    void setModelForecastFromNetwork();
    void postForecastResult(ForecastJsonDTO forecastResult);

    ForecastJsonDTO getForecastResult();

    ForecastAsyncTask.TaskPayload getTaskPayload();

    double getLatitude();

    void setLatitude(double latitude);

    double getLongitude();

    void setLongitude(double longitude);

    void requestForecastForCurrentLocation();

}
