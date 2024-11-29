package ezen.team.ezenbookstore.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtils {

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(date);
    }

    public static String formatCurrency(Long amount) {
        if (amount == null) {
            return "0원";
        }
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(Locale.KOREA);
        return numberFormatter.format(amount) + "원";
    }
}
