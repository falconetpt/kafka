package provider1.publisher;

public interface EventPublisher<T> {
  void send(final T element);
}
