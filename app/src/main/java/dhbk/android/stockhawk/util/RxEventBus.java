package dhbk.android.stockhawk.util;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * A simple event bus built with RxJava
 */
@Singleton
public class RxEventBus {

    /**
     * fixme - a Subject is a Observables and Observers
     *
     * @see <a href="https://medium.com/@kurtisnusbaum/rxandroid-basics-part-2-6e877af352#.j6696hms1"></a>
     */
    private final PublishSubject<Object> mBusSubject;

    /**
     * create subject
     */
    @Inject
    public RxEventBus() {
        mBusSubject = PublishSubject.create();
    }

    /**
     * Posts an object (usually an Event) to the bus
     */
    public void post(Object event) {
        mBusSubject.onNext(event);
    }

    /**
     * Observable that will emmit everything posted to the event bus.
     */
    public Observable<Object> observable() {
        return mBusSubject;
    }

    /**
     * Observable that only emits events of a specific class.
     * Use this if you only want to subscribe to one type of events.
     */
    public <T> Observable<T> filteredObservable(final Class<T> eventClass) {
        /**
         * object này chỉ chứa obj là instance of cái gì đó và nếu instance of thì nó ép kiểu
         */
        return mBusSubject
                .filter(event -> eventClass.isInstance(event))
                .map(event -> (T) event);
    }
}