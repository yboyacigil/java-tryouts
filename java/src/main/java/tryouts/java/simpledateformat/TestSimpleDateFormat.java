package tryouts.java.simpledateformat;

import sun.util.resources.cldr.ee.TimeZoneNames_ee;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TestSimpleDateFormat {

    private static final String dateString = "1978-09-04 00:00:00";

    public static void main(String... args) throws Exception {
        useSystemDefaultTimezoneAndTest();
        setDefaultTimezoneToCESTAndTest();
        setDefaultTimezoneToCETAndTestFormattingInGMT();
    }

    private static final void useSystemDefaultTimezoneAndTest() throws Exception {
        System.out.println("--- useSystemDefaultTimezoneAndTest");
        SimpleDateFormat sdfToParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sdfToParse.parse(dateString);
        printParsedDate(dateString, TimeZone.getDefault(), d);

        SimpleDateFormat sdfToFormat = new SimpleDateFormat("yyyy-MM-dd");
        printFormattedDate(sdfToFormat.getTimeZone(), sdfToFormat.format(d));
    }

    private static final void setDefaultTimezoneToCESTAndTest() throws Exception {
        System.out.println("--- setDefaultTimezoneToCESTAndTest");
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+2"));
        System.out.println("Default timezone set to (" + TimeZone.getDefault().getDisplayName() + ")");

        SimpleDateFormat sdfToParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sdfToParse.parse(dateString);
        printParsedDate(dateString, TimeZone.getDefault(), d);

        SimpleDateFormat sdfToFormat = new SimpleDateFormat("yyyy-MM-dd");
        printFormattedDate(sdfToFormat.getTimeZone(), sdfToFormat.format(d));
    }

    private static final void setDefaultTimezoneToCETAndTestFormattingInGMT() throws Exception {
        System.out.println("--- setDefaultTimezoneToCETAndTestFormattingInGMT");
        TimeZone.setDefault(TimeZone.getTimeZone("CET"));
        System.out.println("Default timezone set to (" + TimeZone.getDefault().getDisplayName() + ")");

        SimpleDateFormat sdfToParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sdfToParse.parse(dateString);
        printParsedDate(dateString, TimeZone.getDefault(), d);

        SimpleDateFormat sdfToFormat = new SimpleDateFormat("yyyy-MM-dd");
        sdfToFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        printFormattedDate(sdfToFormat.getTimeZone(), sdfToFormat.format(d));
    }

    private static void printParsedDate(String dateString, TimeZone tz, Date d) {
        System.out.println("Parsed date string from (" + dateString+ ") and in TZ (" + tz.getDisplayName() + "): " + d);
    }

    private static void printFormattedDate(TimeZone tz, String formatted) {
        System.out.println("Formatted date at TZ in (" + tz.getDisplayName()+ "): " + formatted);
    }

}
