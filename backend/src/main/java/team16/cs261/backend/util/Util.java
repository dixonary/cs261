package team16.cs261.backend.util;

/**
 * Created by martin on 10/03/15.
 */
public class Util {

    public static int getTick(long time, int size) {
        long over = time % size;
        return (int) ((time - over) / size);
    }



}
