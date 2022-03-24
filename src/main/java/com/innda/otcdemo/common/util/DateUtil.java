package com.innda.otcdemo.common.util;

import com.innda.otcdemo.common.exception.BusinessException;
import lombok.Getter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期格式封装
 */
public class DateUtil {

    @Getter
    public enum Format {
        //时间格式
        YYYY("yyyy", "\\d{4}"),
        YYYYMM("yyyyMM", "\\d{6}"),
        YYYYMMDD("yyyyMMdd", "\\d{8}"),
        YYYY_MM_DD("yyyy-MM-dd", "\\d{4}-\\d{2}-\\d{2}"),
        YYYY_MM("yyyy-MM", "\\d{4}-\\d{2}"),
        YYYYMMDDHHMMSS("yyyyMMddHHmmss", "\\d{14}"),
        YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss", "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"),
        HH_MM_SS("HH:mm:ss", "\\d{2}:\\d{2}:\\d{2}");
        private String format;
        private String reg;

        Format(String format, String reg) {
            this.format = format;
            this.reg = reg;
        }
    }

    /**
     * 根据字符串获取日期
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static Date convertByStr(String dateStr, Format format) throws ParseException {
        String reg = format.getReg();
        boolean valid = dateStr.matches(reg);
        if (valid) {
            DateFormat dateFormat = new SimpleDateFormat(format.getFormat());
            dateFormat.setLenient(false);
            return dateFormat.parse(dateStr);
        }
        throw new BusinessException("日期格式错误");
    }

    /**
     * 根据日期获取字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String convertByDate(Date date, Format format) {
        DateFormat dateFormat = new SimpleDateFormat(format.getFormat());
        return dateFormat.format(date);
    }

    /**
     * 严格检验时间格式
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static boolean checkDateStr(String dateStr, Format format) {
        try {
            convertByStr(dateStr, format);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}