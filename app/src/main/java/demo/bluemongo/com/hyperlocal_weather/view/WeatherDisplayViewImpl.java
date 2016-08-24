package demo.bluemongo.com.hyperlocal_weather.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import demo.bluemongo.com.hyperlocal_weather.model.ForecastJsonDTO;
import demo.bluemongo.com.hyperlocal_weather.R;

public class WeatherDisplayViewImpl implements WeatherDisplayView {
    private final WeatherDisplayFragment weatherDisplayFragment;
    private ForecastJsonDTO forecastJsonDTO;

    public WeatherDisplayViewImpl(WeatherDisplayFragment weatherDisplayFragment) {
        this.weatherDisplayFragment = weatherDisplayFragment;
    }

    @Override
    public void showForecast(ForecastJsonDTO forecastJsonDTO) {
        this.forecastJsonDTO = forecastJsonDTO;
        LayoutInflater inflater = (LayoutInflater) weatherDisplayFragment.getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        ScrollView scrollView = (ScrollView) weatherDisplayFragment.getView().findViewById(R.id.scrollViewForForecast);
        LinearLayout container = (LinearLayout) weatherDisplayFragment.getView().findViewById(R.id.linear_layout_forecast);
        container.removeAllViews();
        if (container != null) {
            View view = inflater.inflate(R.layout.forecast_summary, null);
            TextView tvTimezone = (TextView) view.findViewById(R.id.tvTimezone);
            tvTimezone.setText(forecastJsonDTO.getTimezone());

            TextView tvLatitude = (TextView) view.findViewById(R.id.tvLatitude);
            tvLatitude.setText(forecastJsonDTO.getLatitude());

            TextView tvLongitude = (TextView) view.findViewById(R.id.tvLongitude);
            tvLongitude.setText(forecastJsonDTO.getLongitude());

            TextView tvOffset = (TextView) view.findViewById(R.id.tvOffset);
            tvOffset.setText(forecastJsonDTO.getOffset());

            container.addView(view);
            try {
                weatherDisplayFragment.addCurrentForecastItems(forecastJsonDTO, container);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(WeatherDisplayFragment.TAG, e.getMessage());
            }

            container.invalidate();
            scrollView.invalidate();
        }
    }

    @Override
    public void addCurrentForecastItems(ForecastJsonDTO forecastJsonDTO, LinearLayout container) {

    }

    @Override
    public void requestForecast() {
        weatherDisplayFragment.getWeatherPresenter().requestForecastForCurrentLocation();
    }

    @Override
    public ForecastJsonDTO getForecastJson() {
        return forecastJsonDTO;
    }
}