package Server;

import java.util.HashMap;
import java.util.Map;

public class MyPublisher {
    private Map<Integer, Map<Role, MySubscriber>> subscribers = new HashMap<>();

    public synchronized void addSubscriber(int partida, Role role, MySubscriber subscriber) {
        subscribers
                .computeIfAbsent(partida, k -> new HashMap<>())
                .put(role, subscriber);
    }

    public synchronized void sendTo(int partida, Role role, String message) {
        Map<Role, MySubscriber> partidaSubscribers = subscribers.get(partida);

        if (partidaSubscribers == null) {
            return;
        }

        MySubscriber subscriber = partidaSubscribers.get(role);

        if (subscriber != null) {
            subscriber.send(message);
        }
    }
}