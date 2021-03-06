package ch05.examples;

import commons.CommonUtils;
import commons.Log;
import commons.OkHttpHelper;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenWeatherMapV1 {
    private static final String URL = "http://api.openweathermap.org/data/2.5/weather?q=London&APPID=";

    public void run() {

        Observable<String> source = Observable.just(URL + CommonUtils.API_KEY)
                .map(OkHttpHelper::getWithLog)
                .subscribeOn(Schedulers.io());

        //어떻게 호출을 한번만 하게 할 수 있을까?
        Observable<String> temperature = source.map(this::parseTemperature);
        Observable<String> city = source.map(this::parseCityName);
        Observable<String> country = source.map(this::parseCountry);

        CommonUtils.exampleStart();
        Observable.concat(temperature,
                city,
                country)
                .observeOn(Schedulers.newThread())
                .subscribe(Log::it);

        // 원하는 정보가 나오지만 REST API 호출이 3번 발생

        CommonUtils.sleep(1000);
    }

    private String parseTemperature(String json) {
        return parse(json, "\"temp\":[0-9]*.[0-9]*");
    }

    private String parseCityName(String json) {
        return parse(json, "\"name\":\"[a-zA-Z]*\"");
    }

    private String parseCountry(String json) {
        return parse(json, "\"country\":\"[a-zA-Z]*\"");
    }

    private String parse(String json, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(json);
        if (match.find()) {
            return match.group();
        }
        return "N/DoOnExample";
    }

    public static void main(String[] args) {
        OpenWeatherMapV1 demo = new OpenWeatherMapV1();
        demo.run();
    }
}
