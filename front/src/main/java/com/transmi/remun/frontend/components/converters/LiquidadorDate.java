package com.transmi.remun.frontend.components.converters;

import java.io.Serializable;

public class LiquidadorDate implements Serializable {
   private String day;
   private String weekday;
   private String date;

   public String getDate() {
      return date;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public String getDay() {
      return day;
   }

   public void setDay(String day) {
      this.day = day;
   }

   public String getWeekday() {
      return weekday;
   }

   public void setWeekday(String weekday) {
      this.weekday = weekday;
   }

}//LiquidadorDate