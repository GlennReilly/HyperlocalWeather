package demo.bluemongo.com.hyperlocal_weather.model;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class ForecastAsyncTask extends AsyncTask<ForecastAsyncTask.TaskPayload, Void, ForecastJsonDTO> {
    public final String TAG = ForecastAsyncTask.class.getSimpleName();
    private TaskPayload taskPayload;

    public static class TaskPayload{
        private final WeatherModel weatherModel;
        private double latitude;
        private double longitude;
        private URL forecastUri;

        public TaskPayload(WeatherModel weatherModel) {
            this.weatherModel = weatherModel;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public WeatherModel getWeatherModel() {
            return weatherModel;
        }

        public URL getForecastUri() {
            return forecastUri;
        }

        public void setForecastUri(URL forecastUri) {
            this.forecastUri = forecastUri;
        }


        public void postForecastResult(ForecastJsonDTO forecastResult){
            if (forecastResult != null){
                weatherModel.postForecastResult(forecastResult);
            }
        }
    }

    @Override
    protected ForecastJsonDTO doInBackground(TaskPayload... taskPayloads) {
        taskPayload = taskPayloads[0];
        String forecastJsonStr;
        URL forecastURL = taskPayload.getForecastUri();
        ForecastJsonDTO weatherDataOutput = new ForecastJsonDTO();

        HttpURLConnection forecastConnection = null;
        InputStream inputStream = null;
        BufferedReader reader;

        if (forecastURL == null) {
            cancel(true);
        }
        else {
            if (!isCancelled()) {
                try {
                    forecastConnection = (HttpURLConnection) forecastURL.openConnection();
                    forecastConnection.setRequestMethod("GET");
                    forecastConnection.setConnectTimeout(3000);
                    forecastConnection.setReadTimeout(3000);
                    forecastConnection.connect();
                    inputStream = forecastConnection.getInputStream();
                    StringBuffer stringBuffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }

                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuffer.append(line + "\n");
                    }

                    if (stringBuffer.length() == 0) {
                        // Stream was empty.
                        return null;
                    }
                    forecastJsonStr = stringBuffer.toString();

                    Log.v(TAG, "Forecast JSON String: " + forecastJsonStr);
                    try {
                        weatherDataOutput = getWeatherDataFromJson(forecastJsonStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (forecastConnection != null) {
                        forecastConnection.disconnect();
                    }
                }
            }
        }
        return weatherDataOutput;
    }

    @Override
    protected void onPostExecute(ForecastJsonDTO forecastJsonDTO) {
        if (forecastJsonDTO != null && !isCancelled()) {
            taskPayload.postForecastResult(forecastJsonDTO);
        }
    }

    private ForecastJsonDTO getWeatherDataFromJson(String forecastJsonStr) throws JSONException{

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        String latitude = forecastJson.getString("latitude");
        String longitude = forecastJson.getString("longitude");
        String timezone = forecastJson.getString("timezone");
        String offset = forecastJson.getString("offset");
        JSONObject currently = forecastJson.getJSONObject("currently");
        JSONObject hourly =  forecastJson.getJSONObject("hourly");
        JSONObject daily =  forecastJson.getJSONObject("daily");
        JSONObject flags =  forecastJson.getJSONObject("flags");

        ForecastJsonDTO forecastJsonNDTO = new ForecastJsonDTO();
        forecastJsonNDTO.setLatitude(latitude);
        forecastJsonNDTO.setLongitude(longitude);
        forecastJsonNDTO.setTimezone(timezone);
        forecastJsonNDTO.setOffset(offset);
        forecastJsonNDTO.setCurrently(currently);
        forecastJsonNDTO.setHourly(hourly);
        forecastJsonNDTO.setDaily(daily);
        forecastJsonNDTO.setFlags(flags);

        return forecastJsonNDTO;
    }


}
