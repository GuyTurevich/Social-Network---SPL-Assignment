package bgu.spl.net.api;

import java.util.Arrays;

public class MessageEncoderDecoderImpl<T> implements MessageEncoderDecoder<String> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private byte[] opCode = new byte[2];
    private int currIndex = 0;

    @Override
    public String decodeNextByte(byte nextByte) {
        if(currIndex<2)
            opCode[currIndex++] = nextByte;
        else{
            if(nextByte == ';') {
                currIndex = 0;
                return shortToString(bytesToShort(opCode)) + " " + bytes.toString().replaceAll("\0", " ") + ";";
            }
            pushByte(nextByte);
        }
        return null;
    }

    private void pushByte(byte nextByte){
        if (currIndex>= bytes.length)
            bytes = Arrays.copyOf(bytes, currIndex*2); //  if there is no enough space in the array, copy bytes to new array of doubled size
        bytes[currIndex++] = nextByte;
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

    private short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
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

    private String shortToString(short opcode) {
        if (opcode == 1)
            return "REGISTER";
        if (opcode == 2)
            return "LOGIN";
        if (opcode == 3)
            return "LOGOUT";
        if (opcode == 4)
            return "FOLLOW";
        if (opcode == 5)
            return "POST";
        if (opcode == 6)
            return "PM";
        if (opcode == 7)
            return "LOGSTAT";
        if (opcode == 8)
            return "STAT";
        if (opcode == 9)
            return "NOTIFICATION";
        if (opcode == 10)
            return "ACK";
        if (opcode == 11)
            return "ERROR";
        if (opcode == 12)
            return "BLOCK";
        return null;
    }


}
