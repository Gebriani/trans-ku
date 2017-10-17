package id.co.sumi.transaku.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.util.Patterns;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import id.co.sumi.transaku.view.activity.SellerProfileActivity;


/**
 * Created on 27/02/17.
 */

public class CommonFunction {

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String convertCurrencyFormat(Double amount){
        DecimalFormat numberFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();

        formatSymbols.setCurrencySymbol("Rp. ");
        formatSymbols.setMonetaryDecimalSeparator(',');
        formatSymbols.setGroupingSeparator('.');

        numberFormat.setDecimalFormatSymbols(formatSymbols);

        return String.valueOf(numberFormat.format(amount));
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public static String ConvertToBase64(File file){
        String result = "";
        try {
            InputStream inputStream = new FileInputStream(file.getAbsolutePath());
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream outputStream = new Base64OutputStream(output, Base64.DEFAULT);
            try {
                while((bytesRead = inputStream.read(buffer))!= -1){
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                result = output.toString();
                Log.d("CONVERTTOBASE64", result);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }
}
