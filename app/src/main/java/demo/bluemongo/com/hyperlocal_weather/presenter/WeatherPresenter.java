package demo.bluemongo.com.hyperlocal_weather.presenter;

import demo.bluemongo.com.hyperlocal_weather.model.WeatherModel;
import demo.bluemongo.com.hyperlocal_weather.model.ForecastJsonDTO;
import demo.bluemongo.com.hyperlocal_weather.view.WeatherDisplayView;

/**
 * Created by glenn on 17/06/16.
 */
public class WeatherPresenter {
    private WeatherDisplayView weatherDisplayView;
    private WeatherModel weatherModel;

    public WeatherPresenter(WeatherDisplayView weatherDisplayView, WeatherModel weatherModel) {
        this.weatherDisplayView = weatherDisplayView;
        this.weatherModel = weatherModel;
    }

    public void requestForecastForCurrentLocation() {
        if(weatherModel != null) {
            weatherModel.requestForecastForCurrentLocation();
        }
    }

    public void updateForecastView(ForecastJsonDTO forecastJsonDTO) {
        weatherDisplayView.showForecast(forecastJsonDTO);
    }
}
