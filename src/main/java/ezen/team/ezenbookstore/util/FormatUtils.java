package ezen.team.ezenbookstore.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public static String formatKorea(Long amount) {
        if (amount == null) {
            return "0";
        }
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(Locale.KOREA);
        return numberFormatter.format(amount);
    }

    public static LocalDateTime getStartOfMonth() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        return firstDayOfMonth.atStartOfDay(); // 해당 월의 첫째 날 00:00:00을 반환합니다.
    }

}
