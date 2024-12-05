package ezen.team.ezenbookstore.util;

public class ParseUtils {

    public static Long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("잘못된 형식의 데이터입니다: " + value);
        }
    }

    public static Integer parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("잘못된 형식의 데이터입니다: " + value);
        }
    }

}
