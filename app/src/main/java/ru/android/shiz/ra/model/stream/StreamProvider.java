package ru.android.shiz.ra.model.stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import ru.android.shiz.ra.R;
import ru.android.shiz.ra.model.account.AccountManager;
import ru.android.shiz.ra.model.account.NotAuthenticatedException;
import ru.android.shiz.ra.model.stream.statistics.StreamStatistics;
import ru.android.shiz.ra.model.stream.statistics.StreamsCount;
import rx.Observable;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * Created by kassava on 24.08.2016.
 */
public class StreamProvider {

    public static int DELAY = 2000;
    public static int authExceptionEach = 15;
    public static int errorEach = 5;
    private int counter = 0;
    private AtomicInteger lastId;

    private AccountManager accountManager;
    private List<Stream> streams;
    public static final Label INBOX_LABEL = new Label(Label.INBOX, R.drawable.ic_inbox, 0);
    private Label sentLabel = new Label(Label.SENT, R.drawable.ic_send, 0);
    private Label spamLabel = new Label(Label.SPAM, R.drawable.ic_spam, 0);
    private Label trashLabel = new Label(Label.TRASH, R.drawable.ic_delete, 0);

    public StreamProvider(AccountManager accountManager, StreamGenerator genrator) {
        this.accountManager = accountManager;
        streams = genrator.generateStreams();
        lastId = new AtomicInteger(streams.get(streams.size() - 1).getId());
    }

    /**
     * Get the labels with the number of unread streams.
     */
    public Observable<List<Label>> getLabels() {
        return Observable.just(streams).flatMap(new Func1<List<Stream>, Observable<List<Label>>>() {
            @Override public Observable<List<Label>> call(List<Stream> streams) {

                delay();

                Observable error = checkExceptions();
                if (error != null) {
                    return error;
                }

                List<Label> labels = new ArrayList<>(4);
                labels.add(INBOX_LABEL);
                labels.add(sentLabel);
                labels.add(spamLabel);
                labels.add(trashLabel);

                int inbox = 0;
                int spam = 0;
                int sent = 0;
                int trash = 0;

                for (Stream s : streams) {

                    if (s.isRead()) {
                        continue;
                    }

                    switch (s.getLabel()) {
                        case Label.INBOX:
                            inbox++;
                            break;

                        case Label.SENT:
                            sent++;
                            break;

                        case Label.SPAM:
                            spam++;
                            break;

                        case Label.TRASH:
                            trash++;
                            break;
                    }
                }

                INBOX_LABEL.setUnreadCount(inbox);
                sentLabel.setUnreadCount(sent);
                spamLabel.setUnreadCount(spam);
                trashLabel.setUnreadCount(trash);

                return Observable.just(labels);
            }
        });
    }

    public Observable<Stream> getStream(final int id) {
        return getFilteredStreamList(new Func1<Stream, Boolean>() {
            @Override
            public Boolean call(Stream stream) {
                return stream.getId() == id;
            }
        }).flatMap(new Func1<List<Stream>, Observable<Stream>>() {
            @Override
            public Observable<Stream> call(List<Stream> streams) {

                if (streams == null || streams.isEmpty()) {
                    return Observable.error(new NotFoundException());
                }

                return Observable.just(streams.get(0));
            }
        });
    }

    /**
     * Get a list of streams with the given label
     */
    public Observable<List<Stream>> getStreamsOfLabel(final String l) {

        return getFilteredStreamList(new Func1<Stream, Boolean>() {
            @Override public Boolean call(Stream stream) {
                return stream.getLabel().equals(l);
            }
        });
    }

    public Observable<Stream> markAsRead(final Stream stream, final boolean read) {
        stream.read(read);
        final int id = stream.getId();
        return getFilteredStreamList(new Func1<Stream, Boolean>() {
            @Override
            public Boolean call(Stream stream) {
                return stream.getId() == id;
            }
        }, false).flatMap(new Func1<List<Stream>, Observable<Stream>>() {
            @Override
            public Observable<Stream> call(List<Stream> streams) {

                if (streams == null || streams.isEmpty()) {
                    return Observable.error(new NotFoundException());
                }

                Stream toChange = streams.get(0);
                toChange.read(read);

                return Observable.just(toChange);
            }
        });
    }

    /**
     * Star or unstar a stream
     *
     * @param streamId the id of the stream
     * @param star true, if you want to star, false if you want to unstar
     */
    public Observable<Stream> starStream(int streamId, final boolean star) {

        return getStream(streamId).map(new Func1<Stream, Stream>() {
            @Override
            public Stream call(Stream stream) {
                stream.setStarred(star);
                return stream;
            }
        });
    }

    /**
     * Creates and saves a new stream
     */
    public Observable<Stream> addStreamWithDelay(final Stream mail) {
        return Observable.defer(new Func0<Observable<Stream>>() {
            @Override
            public Observable<Stream> call() {

                delay();
                Observable o = checkExceptions();
                if (o != null) {
                    return o;
                }
                return Observable.just(mail);
            }
        }).flatMap(new Func1<Stream, Observable<Stream>>() {
            @Override
            public Observable<Stream> call(Stream stream) {
                return addStream(stream);
            }
        });
    }

    public Observable<Stream> addStream(final Stream stream) {
        return Observable.defer(new Func0<Observable<Stream>>() {
            @Override public Observable<Stream> call() {
                stream.id(lastId.incrementAndGet());
                streams.add(stream);
                return Observable.just(stream);
            }
        });
    }

    public Observable<Stream> setLabel(Stream stream, final String label) {
        stream.label(label);
        return setLabel(stream.getId(), label);
    }

    public Observable<Stream> setLabel(int streamId, final String label) {
        return getStream(streamId).map(new Func1<Stream, Stream>() {
            @Override
            public Stream call(Stream stream) {
                stream.label(label);
                return stream;
            }
        });
    }

    public Observable<List<Stream>> searchForStreams(String query, final int limit) {
        return searchForOlderStreams(query, new Date(), limit);
    }

    public Observable<List<Stream>> searchForOlderStreams(final String query, final Date olderAs,
                                                      final int limit) {

        return getFilteredStreamList(new Func1<Stream, Boolean>() {
            @Override
            public Boolean call(Stream stream) {
                boolean senderCheck =
                        stream.getSender() != null ? stream.getSender().getEstream().contains(query) : false;

                boolean receiverCheck =
                        stream.getReceiver() != null ? stream.getReceiver().getEstream().contains(query) : false;

                return stream.getDate().before(olderAs) && (stream.getSubject().contains(query)
                        || stream.getText().contains(query)
                        || senderCheck
                        || receiverCheck);
            }
        }).map(new Func1<List<Stream>, List<Stream>>() {
            @Override
            public List<Stream> call(List<Stream> streams) {
                if (streams.size() <= limit) {
                    return streams;
                }
                return streams.subList(0, limit);
            }
        });
    }

    private Observable<List<Stream>> getFilteredStreamList(Func1<Stream, Boolean> filterFnc) {
        return this.getFilteredStreamList(filterFnc, true);
    }

    public Observable<List<Stream>> getStreamsSentBy(final int personId) {
        return getFilteredStreamList(new Func1<Stream, Boolean>() {
            @Override
            public Boolean call(Stream stream) {
                return stream.getSender().getId() == personId;
            }
        });
    }

    public Observable<StreamStatistics> getStatistics() {
        return Observable.defer(new Func0<Observable<StreamStatistics>>() {
            @Override public Observable<StreamStatistics> call() {

                delay();
                Observable o = checkExceptions();
                if (o != null) {
                    return o;
                }

                Map<String, StreamsCount> streamsCountMap = new HashMap<String, StreamsCount>();

                for (Stream m : streams) {
                    StreamsCount count = streamsCountMap.get(m.getLabel());
                    if (count == null) {
                        count = new StreamsCount(m.getLabel(), 0);
                        streamsCountMap.put(m.getLabel(), count);
                    }

                    count.incrementCount();
                }

                return Observable.just(
                        new StreamStatistics(new ArrayList<StreamsCount>(streamsCountMap.values())));
            }
        });
    }

    /**
     * Filters the list of streams by the given criteria
     */
    private Observable<List<Stream>> getFilteredStreamList(Func1<Stream, Boolean> filterFnc,
                                                       final boolean withDelayAndError) {
        return Observable.defer(new Func0<Observable<Stream>>() {
            @Override
            public Observable<Stream> call() {

                if (withDelayAndError) {
                    delay();
                    Observable o = checkExceptions();
                    if (o != null) {
                        return o;
                    }
                }

                return Observable.from(streams);
            }
        }).filter(filterFnc).collect(new Func0<List<Stream>>() {
            @Override
            public List<Stream> call() {
                return new ArrayList<Stream>();
            }
        }, new Action2<List<Stream>, Stream>() {
            @Override
            public void call(List<Stream> streams, Stream stream) {
                streams.add(stream);
            }
        }).map(new Func1<List<Stream>, List<Stream>>() {
            @Override
            public List<Stream> call(List<Stream> streams) {
                Collections.sort(streams, StreamComparator.INSTANCE);
                return streams;
            }
        });
    }

    private void delay() {
        if (DELAY > 0) {

            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
        }
    }

    private Observable checkExceptions() {

        counter++;
        if (authExceptionEach > 0 && (!accountManager.isUserAuthenticated()
                || counter % authExceptionEach == 0)) {
            return Observable.error(new NotAuthenticatedException());
        }

        if (errorEach > 0 && counter % errorEach == 0) {
            return Observable.error(new Exception("Fake Excption"));
        }

        return null;
    }

    int getLastStreamId() {
        return lastId.get();
    }
}
