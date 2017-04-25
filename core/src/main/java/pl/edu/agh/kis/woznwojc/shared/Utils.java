package pl.edu.agh.kis.woznwojc.shared;

import java.util.ArrayList;

public class Utils {
    public static byte[] unboxByteArrayList(ArrayList<Byte> arrayList) {
        byte[] unboxedArray = new byte[arrayList.size()];
        for (int i = 0; i < unboxedArray.length; i++) {
            unboxedArray[i] = arrayList.get(i);
        }
        return unboxedArray;
    }
}
