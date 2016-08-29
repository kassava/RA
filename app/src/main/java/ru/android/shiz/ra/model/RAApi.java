package ru.android.shiz.ra.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.android.shiz.ra.model.stream.Stream;
import rx.Observable;

/**
 * Created by kassava on 09.05.2016.
 */
public class RAApi {

    private Map<Integer, String> streamsMap = new HashMap<Integer, String>() {
        {
            put(0, "Форпост");
            put(1, "Охотник");
            put(2, "Звезда смерти");
        }
    };

    public Observable<List<Stream>> getStreams() {
        List<Stream> streams = new ArrayList(streamsMap.values());
        return Observable.just(streams);
    }
}
