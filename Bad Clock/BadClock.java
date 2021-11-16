package messingAround;

import java.util.*;
import java.io.*;
public class BadClock{
    public static void main(String[] args) 
        throws Exception
        {
            long millis, seconds, minutes, hours, days, years, hoursAMPM, isLeapYear, bonusDaysLeap, daysOfTheMonth, timeZone, yearStarted;
            String months       = "if this message shows up the months checker broke";
            String isPM         = "if this message shows up the isPM checker broke";
            String daysSuffix   = "if this shows up the daysSuffix checker broke";
            String zerosMillis  = "if this shows up the zerosMillis checker broke";
            String zerosSeconds = "if this shows up the zerosSeconds checker broke";
            String zerosMinutes = "if this shows up the zerosMinutes checker broke";
            String daySuffix    = "if this shows up the daySuffix checker broke";
            String dayOfTheWeek = "if this shows up the dayOfTheWeek checker broke";
            timeZone      = -5;   //-5 is for how many hours CDT is away from UTC
            yearStarted   = 1970; //this is because System.currentTimeMillis starts counting at Jan 1st 1970
            bonusDaysLeap = 11;   //this could be anything, this is just so it's instantialized for the first run (the first output will always be wrong)
            
            BufferedWriter clockPrinter=new BufferedWriter(new OutputStreamWriter(System.out));
            //todo: make a gui
            while (true)
        {
            long currentTime = (System.currentTimeMillis()+(timeZone*3600000L)+((yearStarted)*31536000000L)+((-bonusDaysLeap)*86400000L));
            years   = (currentTime/(31536000000L));               //(1000*60*60*24*355)
            bonusDaysLeap = (int)(years-1968)/4;
            isLeapYear = (years-1972)%4;
            millis  = (currentTime%(1000L       )) / (1       );  //(1000)              / (1)
            seconds = (currentTime%(60000L      )) / (1000    );  //(1000*60)           / (1000)
            minutes = (currentTime%(3600000L    )) / (60000   );  //(1000*60*60)        / (60*1000)
            hours   = (currentTime%(86400000L   )) / (3600000 );  //(1000*60*60*24)     / (60*60*1000)
            if (isLeapYear == 0){
                days    = (currentTime%(31536000000L)) / (86400000) + 2; //(1000*60*60*24*365) / (60*60*24*1000)
                                                              //+ 1 for leap + 1 for counting from 0
            }else {
                days    = (currentTime%(31536000000L)) / (86400000) + 1; //(1000*60*60*24*365) / (60*60*24*1000)
                                                                      //+ 1 because mod starts counting at 0
            }
            daysOfTheMonth = days; //Maybe the if statements that contain all possibilities won't happen! We gotta make sure by instantializing it here
            if (isLeapYear == 0){
                if (days>=1 && days<=31) {
                    months = "January"; //this is for a LEAP year
                    daysOfTheMonth = days;
                    }
                if (days>31 && days<=60) {
                    months = "February"; //this is for a LEAP year
                    daysOfTheMonth = days - 31;
                    }
                if (days>60 && days<=91) {
                    months = "March"; //this is for a LEAP year
                    daysOfTheMonth = days-60;
                    }
                if (days>91 && days<=121) {
                    months = "April"; //this is for a LEAP year
                    daysOfTheMonth = days-91;
                    }
                if (days>121 && days<=152) {
                    months = "May"; //this is for a LEAP year
                    daysOfTheMonth = days-121;
                    }
                if (days>152 && days<=182) {
                    months = "June"; //this is for a LEAP year
                    daysOfTheMonth = days-152;
                    }
                if (days>182 && days<=213) {
                    months = "July"; //this is for a LEAP year
                    daysOfTheMonth = days-182;
                    }
                if (days>213 && days<=244) {
                    months = "August"; //this is for a LEAP year
                    daysOfTheMonth = days-213;
                    }
                if (days>244 && days<=274) {
                    months = "September"; //this is for a LEAP year
                    daysOfTheMonth = days-244;
                    }
                if (days>274 && days<=305) {
                    months = "October"; //this is for a LEAP year
                    daysOfTheMonth = days-274;
                    }
                if (days>305 && days<=335) {
                    months = "November"; //this is for a LEAP year
                    daysOfTheMonth = days-305;
                    }
                if (days>335 && days<=366) {
                    months = "December"; //this is for a LEAP year
                    daysOfTheMonth = days-335;
                    }
            } else{ 
                if (days>=1 && days<=31) {
                    months = "January"; //this is for a NON-LEAP year
                    daysOfTheMonth = days;
                    }
                if (days>31 && days<=59) {
                    months = "February"; //this is for a NON-LEAP year
                    daysOfTheMonth = days-31;
                    }
                if (days>59 && days<=90) {
                    months = "March"; //this is for a NON-LEAP year
                    daysOfTheMonth = days-59;
                    }
                if (days>90 && days<=120) {
                    months = "April"; //this is for a NON-LEAP year
                    daysOfTheMonth = days-90;
                    }
                if (days>120 && days<=151) {
                    months = "May"; //this is for a NON-LEAP year
                    daysOfTheMonth = days-120;
                    }
                if (days>151 && days<=181) {
                    months = "June"; //this is for a NON-LEAP year
                    daysOfTheMonth = days-151;
                    }
                if (days>181 && days<=212) {
                    months = "July"; //this is for a NON-LEAP year
                    daysOfTheMonth = days-181;
                    }
                if (days>212 && days<=243) {
                    months = "August"; //this is for a NON-LEAP year
                    daysOfTheMonth = days-212;
                    }
                if (days>243 && days<=273) {
                    months = "September"; //this is for a NON-LEAP year
                    daysOfTheMonth = days-243;
                    }
                if (days>273 && days<=304) {
                    months = "October"; //this is for a NON-LEAP year
                    daysOfTheMonth = days-273;
                    }
                if (days>304 && days<=334) {
                    months = "November"; //this is for a NON-LEAP year
                    daysOfTheMonth = days-304;
                    }
                if (days>334 && days<=365) {
                    months = "December"; //this is for a NON-LEAP year
                    daysOfTheMonth = days-334;
                    }
            }
            if (millis<10) {
                    zerosMillis = "00";
                }
             if (millis>=10 && millis<100) {
                    zerosMillis = "0";
                }
                if (millis>=100) {
                    zerosMillis = "";
                }
                if (seconds<10){
                    zerosSeconds = "0";
                }
                if (seconds>=10) {
                    zerosSeconds = "";
                }
                if (minutes<10){
                    zerosMinutes = "0";
                }
                if (minutes>=10) {
                    zerosMinutes = "";
                }
                hoursAMPM = hours; //gets mad at you if you put it outside the while(true) loop
                if (hours>12) {
                    hoursAMPM = hours - 12;
                    isPM = "PM";
                    }
                if (hours<12 && hours!=0) {
                    hoursAMPM = hours;
                    isPM = "AM";
                }
                if (hours == 12) {
                    hoursAMPM = hours;
                    isPM = "PM";
                }
                if (hours == 0) {
                    hoursAMPM = 12;
                    isPM = "AM";
                }
                if (hours==4 && minutes==20) {
                    System.out.println("It is the dank funny irony weed number"); //do NOT remove this function, program WILL stop working
                    Thread.sleep(60000);
                }
                if (daysOfTheMonth%10 == 1) {
                    daySuffix = "st"; //for 1st and 21st and 31st days
                }
                if (daysOfTheMonth%10 == 2) {
                    daySuffix = "nd"; //for 2nd and 22nd days
                }
                if (daysOfTheMonth%10 == 3) {
                    daySuffix = "rd"; //for 3rd and 23rd days
                }
                if (daysOfTheMonth >=4 && daysOfTheMonth <= 20 || daysOfTheMonth >=24 && daysOfTheMonth <= 30) {
                    daySuffix = "th"; //for 4th-20th days and 24th-30th days
                }
                if (days%7==0){
                    dayOfTheWeek = "Thursday";
                }
                if (days%7==1){
                    dayOfTheWeek = "Friday";
                }
                if (days%7==2){
                    dayOfTheWeek = "Saturday";
                }
                if (days%7==3){
                    dayOfTheWeek = "Sunday";
                }
                if (days%7==4){
                    dayOfTheWeek = "Monday";
                }
                if (days%7==5){
                    dayOfTheWeek = "Tuesday";
                }
                if (days%7==6){
                    dayOfTheWeek = "Wednesday";
                }
                    System.out.print("It is " + hoursAMPM + ":" + zerosMinutes + minutes + ":" +
                    zerosSeconds + seconds + ":" + zerosMillis + millis + isPM +
                    " " +dayOfTheWeek + " " + months + " " + daysOfTheMonth + daySuffix + " " + years + "\n");
                    Thread.sleep(5);
                //m
        }
    }
}