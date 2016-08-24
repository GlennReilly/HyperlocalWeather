package demo.bluemongo.com.hyperlocal_weather.view;

import android.widget.LinearLayout;

import demo.bluemongo.com.hyperlocal_weather.model.ForecastJsonDTO;

/**
 * Created by glenn on 18/06/16.
 */
public interface WeatherDisplayView {
    void showForecast(ForecastJsonDTO forecast);

    void addCurrentForecastItems(ForecastJsonDTO forecastJsonDTO, LinearLayout container);

    void requestForecast();

    ForecastJsonDTO getForecastJson();
}
