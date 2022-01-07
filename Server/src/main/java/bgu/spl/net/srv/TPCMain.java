package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;

public class TPCMain {
    public static void main(String[] args) {

        try (Server<String> TPCServer = Server.threadPerClient(7777,
                () -> new BidiMessagingProtocolImpl(new Database()), MessageEncoderDecoderImpl::new);) {
            TPCServer.serve();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}