package com.swatcat.your_name.services.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;


import com.swatcat.your_name.base.BaseActivity;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by max_ermakov on 9/22/16.
 */
public class Tools {

    private static final String TAG = Tools.class.getSimpleName();

    public static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static String getUserAgent() {
        return System.getProperty("http.agent");
    }

    /**
     * Method check is collection null or empty
     *
     * @param collection - Collection to check
     * @return true if Collection null or not null but empty
     */
    // TODO: synchronized?? Weird. Seems like workaround for bad threading code
    public static synchronized boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Method check is String null or empty
     *
     * @param string - String to check
     * @return true if string null or not null but empty
     */
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static String format(int number) {
        return NumberFormat.getIntegerInstance().format(number);
    }

    public static boolean equalsNullEnabled(Object first, Object second) {
        if (first == second) {
            return true;
        } else if (first != null) {
            return first.equals(second);
        } else if (second != null) {
            return second.equals(first);
        } else {
            return false;
        }
    }

    public static String getDeviceId(Context context) {
        String mDeviceId = android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        return mDeviceId;
    }

    /*public static boolean checkPlayServices(BaseActivity activity) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity,
                        resultCode, Constants.PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                //finish();
            }
            return false;
        }
        return true;
    }*/

    //TODO uncomment when time to human readable format needed
    public static String convertTimeToHumanReadableFormat(long time, Context context) {
        int timeFormat = 0;
        DateTime dt = new DateTime(time);
        String timeResult = net.danlew.android.joda.DateUtils.getRelativeTimeSpanString(context, dt,
                timeFormat
                        | DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_SHOW_YEAR).toString();
        return timeResult;
    }

    public static Drawable changeDrawableColor(int drawableRes, int colorRes, Context context) {
        //Convert drawable res to bitmap
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableRes);
        final Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth() - 1, bitmap.getHeight() - 1);
        final Paint p = new Paint();
        final Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, p);

        //Create new drawable based on bitmap
        final Drawable drawable = new BitmapDrawable(context.getResources(), resultBitmap);
        drawable.setColorFilter(new
                PorterDuffColorFilter(context.getResources().getColor(colorRes), PorterDuff.Mode.SRC_IN));
        return drawable;
    }

    public static String obtainParameterFromUrl(String url, String nameOfParameter) {
        Uri uri = Uri.parse(String.valueOf(url));
        String parameter = uri.getQueryParameter(nameOfParameter);
        Log.i(TAG, "parsed parameter = " + parameter);

        return parameter;
    }

    public static String getNameForDay(int year, int month, int dayOfMonth) {
        String dayName = "";

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


        dayName = new SimpleDateFormat("EEE", Locale.ENGLISH).format(calendar.getTime());

        return dayName;
    }

    public static int getDaysCountInMonth(int year, int month) {
        int count = 0;

        DateTime dateTime = new DateTime();
        dateTime = dateTime.withYear(year).withMonthOfYear(month);

        count = dateTime.dayOfMonth().getMaximumValue();

        return count;
    }

    public static String getMonthName(int month) {
        String monthName = "";

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);

        DateTime dateTime = new DateTime();
        dateTime = dateTime.withMonthOfYear(month);

        monthName = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);
        monthName = dateTime.toString("MMM", Locale.US);
        return monthName;
    }


    public static String getUniquePsuedoID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their device or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their device, there will be a duplicate entry
        String serial = null;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static String getMonthNameShort(int month)
    {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.withMonthOfYear(month);
        return  dateTime.toString("MMM", Locale.getDefault());
    }

    public static Drawable setTint(Drawable d, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(d);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }


    public static boolean isSameDay(Date date1, Date date2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        return sameDay;
    }

    public static Long search(Long value, List<Long>a) {
        int lo = 0;
        int hi = a.size() - 1;

        Long lastValue = 0L;

        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            lastValue = a.get(mid);
            if (value < lastValue) {
                hi = mid - 1;
            } else if (value > lastValue) {
                lo = mid + 1;
            } else {
                return lastValue;
            }
        }
        return lastValue;
    }

    public static void removeFragmentFromStack(Class clazz, BaseActivity activity){
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(clazz.getSimpleName());
        if(fragment != null)
            activity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            activity.getSupportFragmentManager().popBackStack();
    }

    public static int getSoundId(String name){
        int id = 0;
        switch (name){
            case "":
                id = 0;
        }
        return id;
    }
}
