package demo.bluemongo.com.hyperlocal_weather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import demo.bluemongo.com.hyperlocal_weather.model.ForecastJsonDTO;
import demo.bluemongo.com.hyperlocal_weather.model.RetainedModelFragment;
import demo.bluemongo.com.hyperlocal_weather.model.WeatherModel;
import demo.bluemongo.com.hyperlocal_weather.view.WeatherDisplayFragment;

public class MainActivity extends AppCompatActivity
        implements WeatherDisplayFragment.OnWeatherFragmentInteractionListener,
        RetainedModelFragment.OnRetainedModelFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RetainedModelFragment retainedModelFragment;
    private WeatherDisplayFragment weatherDisplayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportFragmentManager().findFragmentByTag(RetainedModelFragment.TAG) != null){
            retainedModelFragment = (RetainedModelFragment) getSupportFragmentManager().findFragmentByTag(RetainedModelFragment.TAG);
            Log.i(TAG, "RetainedModelFragment found, no need to create.");
        }else{
            Log.i(TAG, "RetainedModelFragment NOT found, recreating.");
            retainedModelFragment = new RetainedModelFragment();
            retainedModelFragment.setRetainInstance(true);
            getSupportFragmentManager().beginTransaction().add(retainedModelFragment, RetainedModelFragment.TAG).commit();
        }

        weatherDisplayFragment = new WeatherDisplayFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, weatherDisplayFragment).commit();
    }

    @Override
    public void updateForecastView(ForecastJsonDTO forecastJsonDTO) {
        if(weatherDisplayFragment != null){
            weatherDisplayFragment.updateForecastView(forecastJsonDTO);
        }
    }

    @Override
    public WeatherModel getModel() {
        return retainedModelFragment;
    }
}
