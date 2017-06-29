package com.szxb.api.jni_interface;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.media.TransportMediator;
import android.util.Log;
//import com.szxb.s8api.MainActivity;

public class api_interface {
    public static final int BIT_WIDTH = 384;
    private static final int BUF_LEN = 1024;
    private static final int GSV_HEAD = 0;
    private static final int WIDTH = 48;

    public static native int ICCardPowerOff();

    public static native String ICCardPowerOn();

    public static native String[] ICCardSendAPDU(String str);

    public static native String MifareGetSNR(byte[] bArr);

    public static native String[] RFID_APDU(String str);

    public static native String TypeA_RATS();

    public static native String deviceGetVersion();

    public static native String[] msr_getData();

    public static native int printBmp(byte[] bArr, int i);

    private static native int printerdata(byte[] bArr, int i);

    public static native int printersetfont(byte b, byte b2);

    public static native int printertest();

    public static native String psamCardReset(int i, int i2);

    public static native String[] psamCardSendAPDUT0(int i, String str);

    public static native int usbctrl(byte b);

//    public static native int ymodemUpdate(AssetManager assetManager, String str, MainActivity mainActivity);

    static {
        try {
            System.loadLibrary("HalTransModle");
        } catch (Throwable e) {
            Log.e("jni", "i can't find HalTransModle so!");
            e.printStackTrace();
        }
        try {
            System.loadLibrary("SeriesCom");
        } catch (Throwable e2) {
            Log.e("jni", "i can't find SeriesCom so!");
            e2.printStackTrace();
        }
        try {
            System.loadLibrary("PsamCard");
        } catch (Throwable e22) {
            Log.e("jni", "i can't find usbctrl so!");
            e22.printStackTrace();
        }
        try {
            System.loadLibrary("usbctrl");
        } catch (Throwable e222) {
            Log.e("jni", "i can't find usbctrl so!");
            e222.printStackTrace();
        }
        try {
            System.loadLibrary("ymodem");
        } catch (Throwable e2222) {
            Log.e("jni", "i can't find ymodem so!");
            e2222.printStackTrace();
        }
    }

    public static int printerStr(byte[] str) {
        return printerdata(str, str.length);
    }

    private static byte[] generateBitmapArrayMSB(Bitmap bm, int bitMarginLeft, int bitMarginTop) {
        byte[] result = new byte[(((bm.getHeight() + bitMarginTop) * WIDTH) + GSV_HEAD)];
        for (int y = GSV_HEAD; y < bm.getHeight(); y++) {
            int x = GSV_HEAD;
            while (x < bm.getWidth() && x + bitMarginLeft < BIT_WIDTH) {
                int color = bm.getPixel(x, y);
                int alpha = Color.alpha(color);
                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);
                if (alpha > TransportMediator.FLAG_KEY_MEDIA_NEXT && (red < TransportMediator.FLAG_KEY_MEDIA_NEXT || green < TransportMediator.FLAG_KEY_MEDIA_NEXT || blue < TransportMediator.FLAG_KEY_MEDIA_NEXT)) {
                    int bitX = bitMarginLeft + x;
                    int byteX = bitX / 8;
                    int i = (((y + bitMarginTop) * WIDTH) + GSV_HEAD) + byteX;
                    result[i] = (byte) (result[i] | (TransportMediator.FLAG_KEY_MEDIA_NEXT >> (bitX - (byteX * 8))));
                }
                x++;
            }
        }
        return result;
    }

    public static void printBitmap(Bitmap bm, int bitMarginLeft, int bitMarginTop) {
        byte[] result = generateBitmapArrayMSB(bm, bitMarginLeft, bitMarginTop);
        byte[] sendbuf = new byte[3075];
        int i = GSV_HEAD;
        while (i < result.length / 3072) {
            sendbuf[GSV_HEAD] = (byte) 42;
            sendbuf[1] = (byte) 64;
            sendbuf[2] = (byte) 48;
            System.arraycopy(result, i * 3072, sendbuf, 3, 3072);
            printBmp(sendbuf, (sendbuf[1] * sendbuf[2]) + 3);
            i++;
        }
        sendbuf[GSV_HEAD] = (byte) 42;
        sendbuf[1] = (byte) ((result.length % 3072) / WIDTH);
        sendbuf[2] = (byte) 48;
        System.arraycopy(result, i * 3072, sendbuf, 3, result.length % 3072);
        printBmp(sendbuf, (sendbuf[1] * sendbuf[2]) + 3);
    }
    public static byte[] font_SansSerif_56() {
        byte[] rf1 = new byte[]{(byte)26, (byte)75, (byte)65};
        return rf1;
    }
    public String font_SansSerif_54(String data) {
        byte[] rf1 = new byte[]{(byte)26, (byte)75, (byte)45};
        String s = new String(rf1);
        return s + data;
    }
    public String font_SansSerif_53(String data) {
        byte[] rf1 = new byte[]{(byte)26, (byte)75, (byte)40};
        String s = new String(rf1);
        return s + data;
    }
    public String font_SansSerif_52(String data) {
        byte[] rf1 = new byte[]{(byte)26, (byte)75, (byte)35};
        String s = new String(rf1);
        return s + data;
    }
    public String font_SansSerif_51(String data) {
        byte[] rf1 = new byte[]{(byte)26, (byte)75, (byte)20};
        String s = new String(rf1);
        return s + data;
    }
}
