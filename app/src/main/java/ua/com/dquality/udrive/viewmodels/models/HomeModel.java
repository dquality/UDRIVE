package ua.com.dquality.udrive.viewmodels.models;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import ua.com.dquality.udrive.R;
import ua.com.dquality.udrive.UDriveApplication;
import ua.com.dquality.udrive.constants.Const;

public class HomeModel {
    public StatusLevel Level;
    public StatusLevel NextMonthLevel;
    public int NextLevelPercentage;
    public int UcoinsCount;

    public int PrevMonthTripsCount;
    public int WeekTripsCount;
    public int RemainsTripsCount;
    public double BalanceAmount;

    private Bitmap QrBitmap;
    private Bitmap EanBitmap;
    private String Barcode;

    public void setBarcode(String barCode, Context appContext){
        boolean isEmptyBarcode = barCode == null || barCode.isEmpty() || barCode == "null";
        if(isEmptyBarcode) {
            barCode = Const.DEFAULT_BARCODE;
        }

        if(barCode.equals(Barcode)) {
            return;
        }

        Barcode = barCode;
        if(!isEmptyBarcode) {
            generateBitmapAsync(appContext);
        }
    }

    private Thread mAsync;

    private void generateBitmapAsync(Context appContext){
        int black = Color.BLACK;
        int white = ContextCompat.getColor(appContext, R.color.colorPrimaryLight);
        mAsync = new Thread(()->{
            int size = 512;

            QRCodeWriter writer = new QRCodeWriter();
            try {
                BitMatrix bitMatrix = writer.encode(Barcode, BarcodeFormat.QR_CODE, size, size);
                QrBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
                for (int x = 0; x < size; x++) {
                    for (int y = 0; y < size; y++) {
                        QrBitmap.setPixel(x, y, bitMatrix.get(x, y) ? black : white);
                    }
                }

            } catch (WriterException e) {
                e.printStackTrace();
            }

            MultiFormatWriter barcodeWriter = new MultiFormatWriter();
            try {
                BitMatrix barcodeBitMatrix = barcodeWriter.encode(Barcode, BarcodeFormat.EAN_13, size, size);
                EanBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
                for (int x = 0; x < size; x++) {
                    for (int y = 0; y < size; y++) {
                        EanBitmap.setPixel(x, y, barcodeBitMatrix.get(x, y) ? black : white);
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

    public Bitmap getBarcodeBitmap(boolean qrCode){
        if(mAsync != null) {
            try {
                mAsync.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return qrCode ? QrBitmap: EanBitmap;
    }
}
