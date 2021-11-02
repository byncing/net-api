package eu.byncing.net.api;

public class NetOption<T> {

    public static final NetOption<Integer> TIMEOUT = new NetOption<>(-1);
    public static final NetOption<Integer> BUFFER_SIZE = new NetOption<>(1024);

    protected T value;

    protected NetOption(T value) {
        this.value = value;
    }

    protected T getValue() {
        return value;
    }

    @SuppressWarnings("unchecked")
    protected void setValue(Object value) {
        this.value = (T) value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}