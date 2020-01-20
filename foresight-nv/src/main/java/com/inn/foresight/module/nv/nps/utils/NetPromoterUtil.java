
package com.inn.foresight.module.nv.nps.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.nps.constants.NetPromoterConstant;




/** The Class NetPromoterUtil. */
public class NetPromoterUtil {
    
    private NetPromoterUtil() {
        
    }
    /** The logger. */
    private static Logger logger = LogManager.getLogger(NetPromoterUtil.class);

    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";

    /**
     * Generate week number.
     
     * @param endDate
     *            the end date
     * @return the int
     */
    
    public static DateFormat getDateFormat(String format) {
            return new SimpleDateFormat(format);
    }
    
    public static int generateWeekNumber(String endDate) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);
        Date date = null;
        try {
            date = df.parse(endDate);
        } catch (ParseException e) {
            logger.error("error in generateWeekNumber = {}", ExceptionUtils.getStackTrace(e));  
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
    
    public  static String isValidCustomerCount(Long count) {
        if(count != null) {
            return String.valueOf(count);
        }
        return null;
    }

    /**
     * Gets the end date.
     *
     * @param startDate
     *            the start date
     * @return the end date
     */
    public static String getEndDate(String startDate, Long days) {
        Integer year = Integer.parseInt(startDate.substring(0, ForesightConstants.FOUR));
        Integer month = Integer.parseInt(startDate.substring(ForesightConstants.FOUR, ForesightConstants.SIX));
        Integer date = Integer.parseInt(startDate.substring(ForesightConstants.SIX, NVConstant.EIGHT));
        LocalDate localDate = LocalDate.of(year, Month.of(month), date);
        LocalDate endDate = localDate.minusDays(days);
        return endDate.format(DateTimeFormatter.ofPattern(NetPromoterConstant.YYYYLLDD));
    }
    
    public static String getLastDates(String date, int noOfDays) {
        Date myDate = null;
        DateFormat dateFormat = getDateFormat(DATE_FORMAT_YYYYMMDD);
        try {
            myDate = dateFormat.parse(date);
            Date dayBefore = new Date(myDate.getTime() - noOfDays);
            return dateFormat.format(dayBefore);
        } catch (ParseException e) {
            logger.error("error in getLastDates = {}", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
    
    /**
     * Gets the 30 days date constant map.
     *
     * @param endDate
     *            the end date
     * @return the 30 days date constant map
     */
    public static Map<String, Long> get30daysDateConstantMap(String endDate) {
        Map<String, Long> contantMap = new HashMap<>();
        DateTimeFormatter formate = DateTimeFormatter.ofPattern(NetPromoterConstant.YYYYMMDD);
        LocalDate enddate = LocalDate.parse(endDate, formate);
        LocalDate startDate = enddate.plusDays(1L).minusDays(NVConstant.THIRTY);
        long numofDaysBetween = ChronoUnit.DAYS.between(startDate, enddate.plusDays(1L));
        java.util.List<LocalDate> dates = IntStream.iterate(0, i -> i + 1).limit(numofDaysBetween)
                .mapToObj(i -> startDate.plusDays(i)).collect(Collectors.toList());
        Collections.reverse(dates);
        for (LocalDate f : dates) {
             Date date = Date.from(f.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            contantMap.put(f.toString(), date.getTime());
            
        }
        
        return contantMap;
    }
    
    
    
    
    
    public static Map<String, Integer> get7daysDateConstantMap(String endDate) {
        Map<String, Integer> contantMap = new HashMap<>();
        DateTimeFormatter formate = DateTimeFormatter.ofPattern(NetPromoterConstant.YYYYMMDD);
        LocalDate enddate = LocalDate.parse(endDate, formate);
        LocalDate startDate = enddate.minusDays(ForesightConstants.SEVEN);

        int k = ForesightConstants.SIX;
        long numofDaysBetween = ChronoUnit.DAYS.between(startDate, enddate.plusDays(0L));
        java.util.List<LocalDate> data = IntStream.iterate(1, i -> i + 1).limit(numofDaysBetween)
                .mapToObj( i -> startDate.plusDays(i)).collect(Collectors.toList());
        for (LocalDate f : data) {
            contantMap.put(f.toString(), k);

            --k;
        }
        
        return contantMap;
    }
        
    public static Map<String, Integer> get7daysWeekConstantMap(String endDate) {
        Map<String, Integer> contantMap = new HashMap<>();
        List<Integer> weekList = generateWeekNoList(endDate,false);
        Collections.reverse(weekList);
        int k = ForesightConstants.SIX;
        for (Integer integer : weekList) {
            contantMap.put(integer.toString(), k);

            k--;
        }
        
        return contantMap;
    }
    
    public static List<String> getLastSevenDays(String startDate, Integer noOfDays) {
        List<String> dateList = new ArrayList<>();
        try {
            Date date = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD).parse(startDate);
            for (int i = 0; i < noOfDays; i++) {
                dateList.add(formatedDate(subtractDays(date, i)));
            }
        } catch (ParseException e) {
            logger.error("error in getLastSevenDays = {}", ExceptionUtils.getStackTrace(e));
        }
        return dateList;

    }

    public static String formatedDate(Date date) {
        return getDateFormat(DATE_FORMAT_YYYY_MM_DD).format(date);
    }

    public static Date subtractDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);
        return cal.getTime();
    }
    
    public static String getDateDifference(String processDate, String startDate) {
        Date date1 = null;
        Date date2 = null;
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD);
        try {
            date1 = format.parse(processDate);
            date2 = format.parse(startDate);
            long diff = date2.getTime() - date1.getTime();
            return String.valueOf(diff / (ForesightConstants.THOUSAND * ForesightConstants.SIXTY * ForesightConstants.SIXTY * ForesightConstants.TWENTY_FOUR));
        } catch (Exception e) {
            logger.error("error in getDateDifference = {}", ExceptionUtils.getStackTrace(e));
            return "";
        }

    }

    public static Map<String, String> loadKpiMap() {
        Map<String, String> emptyKpiMap = new HashMap<>();
        emptyKpiMap.put("0", "null");
        emptyKpiMap.put("1", "null");
        emptyKpiMap.put("2", "null");
        emptyKpiMap.put("3", "null");
        emptyKpiMap.put("4", "null");
        emptyKpiMap.put("5", "null");
        emptyKpiMap.put("6", "null");
        return emptyKpiMap;
    }

    public static Date getDateFromString(String date) {
        try {
            return getDateFormat(DATE_FORMAT_YYYY_MM_DD).parse(date);
        } catch (ParseException e) {
            logger.error("error in getDateFromString = {}", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    public static List<Integer> generateWeekNoList(String endDate, Boolean isKpi) {
        List<Integer> weekList = new ArrayList<>();
        Date date = null;
        try {
            if (isKpi) {
                date = getDateFormat(DATE_FORMAT_YYYY_MM_DD).parse(endDate);
            } else {
                date = getDateFormat(DATE_FORMAT_YYYYMMDD).parse(endDate);
            }
        } catch (ParseException e) {
            logger.error("error in generateWeekNoList = {}", ExceptionUtils.getStackTrace(e));
        }
        Calendar cal = new GregorianCalendar();
        for (int i = 0; i < ForesightConstants.SEVEN; i++) {
            cal.setTime(date);
            int week = cal.get(Calendar.WEEK_OF_YEAR);
            weekList.add(week);
            date = subtractDays(date, ForesightConstants.SEVEN);
        }
        return weekList;
    }
    
    
    
    public static int getWeekNoByDate(String endDate, boolean kpi) {
        Date date = null;
        if(kpi) {
            try {
                date = getDateFormat(DATE_FORMAT_YYYY_MM_DD).parse(endDate);
            } catch (ParseException e) {
                logger.error("error in getWeekNoByDate = {}", ExceptionUtils.getStackTrace(e));
            }
        }else {
            try {
                date = getDateFormat(DATE_FORMAT_YYYYMMDD).parse(endDate);
            } catch (ParseException e) {
                logger.error("error in getWeekNoByDate = {}", ExceptionUtils.getStackTrace(e));
            }
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -1);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
    
    public static int getWeekNumberForMap(String endDate, Integer weekNoOfWrapper,int counter) {
        Date date = null;
                 try {
                date = getDateFormat(DATE_FORMAT_YYYY_MM_DD).parse(endDate);
            } catch (ParseException e) {
                logger.error("error in getWeekNoByDate = {}", ExceptionUtils.getStackTrace(e));
            }
                 
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int weekNoOFYear = cal.get(Calendar.WEEK_OF_YEAR);
            
            if(weekNoOFYear>=weekNoOfWrapper) {
                return weekNoOFYear-weekNoOfWrapper;
                
            }else {
                int weekNo = getMaxWeekNoOfLastYear();
                weekNo = weekNo-1;
                weekNoOFYear=weekNo+counter;
                return weekNoOFYear-weekNoOfWrapper;
              
            }
        }
    
    public static int getMaxWeekNoOfLastYear() {
        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR)-1;
        Year previousYear = Year.of(year);
        int numberOfWeek = previousYear.atDay(previousYear.length()).get(WeekFields.ISO.weekOfYear()); // 53
        return numberOfWeek;   
    }
    
    public static void main(String[] args) {
        
        String endDate="2018-01-01";
        generateWeekNoList(endDate,true);
        System.out.println("weekno list"+generateWeekNoList(endDate,true));
        Integer integer=2;/*
        getMaxWeekNoOfLastYear(endDate);
        getWeekNumberForMap(endDate,integer);*/
    }
}
