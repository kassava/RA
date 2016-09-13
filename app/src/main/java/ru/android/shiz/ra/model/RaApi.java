package ru.android.shiz.ra.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import ru.android.shiz.ra.R;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kassava on 09.05.2016.
 */
public class RaApi {

    private final String LOG_TAG = RaApi.class.getSimpleName();

    private int requestCounter = 0;
    private int errorAfter = 3;

    private Map<Integer, Stream> streamsMap = new HashMap<Integer, Stream>() {
        {
            put(0, new Stream(0, "Форпост"));
            put(1, new Stream(1, "Охотник"));
            put(2, new Stream(2, "Звезда смерти"));
        }
    };

    private Map<Integer, Stream> streamsMap2 = new HashMap<Integer, Stream>() {
        {
            put(0, new Stream(0, "Рипер"));
            put(1, new Stream(1, "Глобал Хоук"));
            put(2, new Stream(2, "Жнец"));
        }
    };

    public Observable<List<Stream>> getStreams() {
        Random random = new Random();
        List<Stream> streams = null;
        if (random.nextInt(2) == 1) {
            streams = new ArrayList(streamsMap.values());
        } else {
            streams = new ArrayList(streamsMap2.values());
        }

        return Observable.just(streams).delay(3, TimeUnit.MILLISECONDS);
    }

    public Observable<StreamDetail> getDetails(int id) {
        Log.d(LOG_TAG, "id: " + id);

        Stream stream = streamsMap.get(id);
        List<StreamDetail> details = new ArrayList<>();
        List<Info> infoList = new ArrayList<>();
        switch (id) {
            case 0:
                infoList.add(new InfoText(R.string.app_name,"Тренировочный полёт."));
                infoList.add(new InfoText(R.string.app_name, "Ирак"));
                break;
            case 1:
                infoList.add(new InfoText(R.string.app_name,"За Бен Ладоном."));
                infoList.add(new InfoText(R.string.app_name, "Афганистан"));
                break;
            default:
                infoList.add(new InfoText(R.string.app_name,"Домой."));
                infoList.add(new InfoText(R.string.app_name, "США"));
                break;
        }

        StreamDetail streamDetail = new StreamDetail(id, "url", infoList);
        details.add(streamDetail);
        return Observable.just(streamDetail).delay(2, TimeUnit.MILLISECONDS);
    }
}
