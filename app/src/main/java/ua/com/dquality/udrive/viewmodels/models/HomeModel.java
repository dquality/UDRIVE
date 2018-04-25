package ua.com.dquality.udrive.viewmodels.models;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import ua.com.dquality.udrive.R;
import ua.com.dquality.udrive.UDriveApplication;

public class HomeModel {
    public StatusLevel Level;
    public StatusLevel NextMonthLevel;
    public int NextLevelPercentage;
    public int UcoinsCount;

    public int PrevMonthTripsCount;
    public int WeekTripsCount;
    public int RemainsTripsCount;
    public double BalanceAmount;

    private Bitmap BarcodeBitmap;
    private String Barcode;

    public void setBarcode(String barCode, Context appContext){
        if(barCode == Barcode)
            return;

        Barcode = barCode;
        generateBitmapAsync(appContext);
    }

    private Thread mAsync;

    private void generateBitmapAsync(Context appContext){
        int black = Color.BLACK;
        int white = ContextCompat.getColor(appContext, R.color.colorPrimaryLight);
        mAsync = new Thread(()->{
            QRCodeWriter writer = new QRCodeWriter();
            try {
                BitMatrix bitMatrix = writer.encode(Barcode, BarcodeFormat.QR_CODE, 512, 512);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                BarcodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        BarcodeBitmap.setPixel(x, y, bitMatrix.get(x, y) ? black : white);
                    }
                }

            } catch (WriterException e) {
                e.printStackTrace();
            }
        });
        mAsync.start();
    }

    public String getBarcode(){
        return Barcode;
    }

    public Bitmap getBarcodeBitmap(){
        try {
            mAsync.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return BarcodeBitmap;
    }
}
