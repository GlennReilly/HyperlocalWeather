package demo.bluemongo.com.hyperlocal_weather.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Iterator;

import demo.bluemongo.com.hyperlocal_weather.model.WeatherModel;
import demo.bluemongo.com.hyperlocal_weather.R;
import demo.bluemongo.com.hyperlocal_weather.model.ForecastJsonDTO;
import demo.bluemongo.com.hyperlocal_weather.presenter.WeatherPresenter;


public class WeatherDisplayFragment extends Fragment implements WeatherDisplayView {

    public static final String TAG = WeatherDisplayFragment.class.getSimpleName();
    private final WeatherDisplayViewImpl weatherDisplayViewImpl = new WeatherDisplayViewImpl(this);

    private WeatherPresenter weatherPresenter;
    private OnWeatherFragmentInteractionListener mListener;


    public WeatherDisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        weatherPresenter = new WeatherPresenter(this, weatherModel);*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_weather, container, false);
        weatherDisplayViewImpl.requestForecast();
        return viewRoot;
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnWeatherFragmentInteractionListener) {
            mListener = (OnWeatherFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnWeatherFragmentInteractionListener");
        }

        weatherPresenter = new WeatherPresenter(this, mListener.getModel());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showForecast(ForecastJsonDTO forecastJsonDTO) {
        weatherDisplayViewImpl.showForecast(forecastJsonDTO);
    }

    @Override
    public void addCurrentForecastItems(ForecastJsonDTO forecastJsonDTO, LinearLayout container) {

        Iterator<String> iteratorCurrently = forecastJsonDTO.getCurrently().keys();
        while(iteratorCurrently.hasNext()){
            TextView textView = new TextView(getActivity());
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            String next = iteratorCurrently.next();
            String value = forecastJsonDTO.getCurrently().optString(next.toString());
            if (next.equals("time")){
                long unixTime = Long.parseLong(value);
                value = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (unixTime * 1000));
            }
            if (next.equals("temperature") || next.equals("apparentTemperature") || next.equals("dewPoint")){
                double temperatureF = Double.parseDouble(value);
                double temperatureC = ((temperatureF - 32d) *((double)5/9));
                DecimalFormat df = new DecimalFormat("#.00 °C");
                value = String.valueOf(df.format(temperatureC));
            }

            textView.setText("current: " + next + ": " + value);
            textView.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
            container.addView(textView);
        }
    }



    @Override
    public void requestForecast() {
        weatherDisplayViewImpl.requestForecast();
    }

    @Override
    public ForecastJsonDTO getForecastJson() {
        return weatherDisplayViewImpl.getForecastJson();
    }

    public void updateForecastView(ForecastJsonDTO forecastJsonDTO) {
        weatherPresenter.updateForecastView(forecastJsonDTO);
    }

    public WeatherPresenter getWeatherPresenter() {
        return weatherPresenter;
    }


    public interface OnWeatherFragmentInteractionListener {
        WeatherModel getModel();
    }
}
