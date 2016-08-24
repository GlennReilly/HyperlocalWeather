package demo.bluemongo.com.hyperlocal_weather;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import demo.bluemongo.com.hyperlocal_weather.model.ForecastAsyncTask;
import demo.bluemongo.com.hyperlocal_weather.model.ForecastJsonDTO;
import demo.bluemongo.com.hyperlocal_weather.model.WeatherModel;
import demo.bluemongo.com.hyperlocal_weather.presenter.WeatherPresenter;
import demo.bluemongo.com.hyperlocal_weather.view.WeatherDisplayView;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;


public class ForecastUnitTest {
    WeatherDisplayView view;
    WeatherModel model;
    WeatherPresenter presenter;
    ForecastAsyncTask.TaskPayload taskPayload;
    private final double DUMMY_LATITUDE = -33.865143;
    private final double DUMMY_LONGITUDE = 151.209900;
    private URL DUMMY_API_URL;
    ForecastAsyncTask forecastAsyncTask;
    ForecastJsonDTO forecastJsonDTO;
    JSONObject jsonObject;


    @Before
    public void setupObjects() throws MalformedURLException, JSONException {
        view = mock(WeatherDisplayView.class);
        model = mock(WeatherModel.class);
        presenter = new WeatherPresenter(view, model);
        taskPayload = new ForecastAsyncTask.TaskPayload(model);
        DUMMY_API_URL = new URL("https://api.forecast.io/forecast/APIKEY/LATITUDE,LONGITUDE,TIME");
        forecastAsyncTask = mock(ForecastAsyncTask.class);
        forecastJsonDTO = mock(ForecastJsonDTO.class);
        model.setLongitude(DUMMY_LONGITUDE);
        model.setLatitude(DUMMY_LATITUDE);
        taskPayload.setLatitude(model.getLatitude());
        taskPayload.setLongitude(model.getLongitude());
        taskPayload.setForecastUri(DUMMY_API_URL);
        jsonObject = mock(JSONObject.class);
        jsonObject.put("message", "success!");
    }

    @Test
    public void initialNullTest(){
        Assert.assertNotNull(model);
        Assert.assertNotNull(view);
        Assert.assertNotNull(presenter);
        Assert.assertNotNull(forecastAsyncTask);
        Assert.assertNotNull(taskPayload.getLatitude());
        Assert.assertNotNull(taskPayload.getLongitude());
    }

    @Test
    public void testPresenterCallingView() throws Exception {
        forecastAsyncTask.execute(taskPayload);
        model.postForecastResult(forecastJsonDTO);
        presenter.updateForecastView(forecastJsonDTO);
        verify(view, times(1)).showForecast(forecastJsonDTO);
    }

    @Test
    public void testPresenterCallingModel() {
        presenter.requestForecastForCurrentLocation();
        verify(model, times(1)).requestForecastForCurrentLocation();
    }

}