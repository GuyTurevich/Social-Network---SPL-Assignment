package bgu.spl.net.api.bidi;

public interface Message<T> {

    public void process(T message);
}
