package org.example.dto;

import lombok.Data;
import org.example.utils.TextUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
public class DayBE {
    private static final String SUNRISE = "זריחה:";
    private static final String SUNSET = "שקיעה:";
    private static final String CANDLE_LIGHTING = "הדלקת נרות:";
    private static final String TZAIS_SABATH = "צאת השבת:";
    private static final String TZAIS_YOM_TOV = "צאת החג:";

    Date date;
    String year;
    String yearHeb;
    String month;
    String monthHeb;
    String dayInWeek;
    String dayInWeekHeb;
    String dayInMonth;
    String dayInMonthHeb;
    String sunrise;
    String sunset;
    String parsha;
    String specialParsha;
    boolean isShabath;
    boolean isYomTov;
    String roshChodesh;
    String yomTovName;
    String candleLighting;
    String tzaisSabath;
    String tzaisYomTov;
    List<EventFE> eventFEList;
    boolean isCurrentYear;
    String dafYomiBavli;

    public void setYearHeb(String yearHeb) {
        this.yearHeb = TextUtils.setDirectionRtl(yearHeb);
    }

    public void setMonth(String month) {
        this.month = TextUtils.setDirectionRtl(month);
    }

    public void setMonthHeb(String monthHeb) {
        this.monthHeb = TextUtils.setDirectionRtl(monthHeb);
    }

    public void setDayInWeekHeb(String dayInWeekHeb) {
        this.dayInWeekHeb = TextUtils.setDirectionRtl(dayInWeekHeb);
    }

    public void setDayInMonthHeb(String dayInMonthHeb) {this.dayInMonthHeb = TextUtils.setDirectionRtl(dayInMonthHeb);}

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise + TextUtils.setDirectionRtl(SUNRISE);
    }

    public void setSunset(String sunset) {
        this.sunset = sunset + TextUtils.setDirectionRtl(SUNSET);
    }

    public void setCandleLighting(String candleLighting) {this.candleLighting = candleLighting + TextUtils.setDirectionRtl(CANDLE_LIGHTING);}

    public void setTzaisSabath(String tzaisSabath) {this.tzaisSabath = tzaisSabath + TextUtils.setDirectionRtl(TZAIS_SABATH);}

    public void setTzaisYomTov(String tzaisYomTov) {this.tzaisYomTov = tzaisYomTov + TextUtils.setDirectionRtl(TZAIS_YOM_TOV);}

    public void setParsha(String parsha) {this.parsha = TextUtils.setDirectionRtl(parsha);}

    public void setYomTovName(String yomTovName) {
        this.yomTovName = TextUtils.setDirectionRtl(yomTovName);
    }

    public void setSpecialParsha(String specialParsha) {
        this.specialParsha = TextUtils.setDirectionRtl(specialParsha);
    }

    public void setRoshChodesh(String roshChodesh) {
        this.roshChodesh = TextUtils.setDirectionRtl(roshChodesh);
    }

    public void setDafYomiBavli(String dafYomiBavli) {
        this.dafYomiBavli = TextUtils.setDirectionRtl(dafYomiBavli);
    }
}
