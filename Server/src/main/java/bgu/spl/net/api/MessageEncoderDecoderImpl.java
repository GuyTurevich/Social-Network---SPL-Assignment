package bgu.spl.net.api;

public class MessageEncoderDecoderImpl<T> implements MessageEncoderDecoder<String> {


    @Override
    public String decodeNextByte(byte nextByte) {

        return null;// should delete after done implementing
    }

    @Override
    public byte[] encode(String message) {

        int spaceIndex = message.indexOf(" ");
        if (spaceIndex == -1) {
            byte[] output = shortToBytes(stringToShort(message));
            return output;
        }

        String command = message.substring(0, spaceIndex);
        short opcode = stringToShort(command);
        byte[] restOfMessage = (message.replaceAll(" ", "\0") + ";").getBytes();
        byte[] op = shortToBytes(opcode);
        byte[] output = new byte[restOfMessage.length + 2];
        output[0] = op[0];
        output[1] = op[1];

        for (int i = 2; i < output.length; i++) {
            output[i] = restOfMessage[i - 2];
        }
        return output;
    }

    public byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte) ((num >> 8) & 0xFF);
        bytesArr[1] = (byte) (num & 0xFF);
        return bytesArr;
    }

    private short stringToShort(String command) {
        if (command.equals("REGISTER"))
            return 1;
        if (command.equals("LOGIN"))
            return 2;
        if (command.equals("LOGOUT"))
            return 3;
        if (command.equals("FOLLOW"))
            return 4;
        if (command.equals("POST"))
            return 5;
        if (command.equals("PM"))
            return 6;
        if (command.equals("LOGSTAT"))
            return 7;
        if (command.equals("STAT"))
            return 8;
        if (command.equals("NOTIFICATION"))
            return 9;
        if (command.equals("ACK"))
            return 10;
        if (command.equals("ERROR"))
            return 11;
        if (command.equals("BLOCK"))
            return 12;
        return -1;
    }
}
