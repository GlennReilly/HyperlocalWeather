package demo.bluemongo.com.hyperlocal_weather.model;

import org.json.JSONObject;

/**
 * Created by glenn on 19/06/16.
 */
public class ForecastJsonDTO {
    private String latitude;
    private String longitude;
    private String timezone;
    private String offset;
    private JSONObject currently;
    private JSONObject hourly;
    private JSONObject daily;
    private JSONObject flags;

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getLongitude() {
        return longitude;

    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getOffset() {
        return offset;
    }

    public void setCurrently(JSONObject currently) {
        this.currently = currently;
    }

    public JSONObject getCurrently() {
        return currently;
    }

    public void setHourly(JSONObject hourly) {
        this.hourly = hourly;
    }

    public JSONObject getHourly() {
        return hourly;
    }

    public void setDaily(JSONObject daily) {
        this.daily = daily;
    }

    public JSONObject getDaily() {
        return daily;
    }

    public void setFlags(JSONObject flags) {
        this.flags = flags;
    }

    public JSONObject getFlags() {
        return flags;
    }
}
