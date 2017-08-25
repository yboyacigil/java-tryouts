package tryouts.rxjava.helloworld;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        //
        Flowable.just("Hello world!").subscribe(System.out::println);

        //
        Flowable.fromCallable(() -> {
           Thread.sleep(1000);
           return "Some background result";
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(System.out::println, Throwable::printStackTrace);

        Thread.sleep(2000);

        //
        Flowable.range(1, 10)
                .observeOn(Schedulers.computation())
                .map(v -> v * v)
                .blockingSubscribe(System.out::println);

        //
        Flowable.range(1, 10)
                .flatMap(v -> Flowable.just(v)
                        .subscribeOn(Schedulers.computation())
                        .map(w -> w * w)
                )
                .blockingSubscribe(System.out::println);

        //
        Observable<Integer> observable = Observable.range(1, 5);

        // Hand over other threads to subscribe
        observable
                .subscribeOn(Schedulers.computation())
                .forEach(v -> {
                    System.out.printf("[%s] next: %d\n", Thread.currentThread().getName(), v);
                });
        // wait some time for subscribers do their work
        Thread.sleep(2000);

        // Hand over other threads to observe
        observable.observeOn(Schedulers.computation())
                .subscribe(v -> {
                    System.out.printf("[%s] next: %d\n", Thread.currentThread().getName(), v);
                });
        // wait some time for subscribers do their work
        Thread.sleep(2000);
    }
}
