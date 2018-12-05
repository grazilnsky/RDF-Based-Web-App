/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 *
 * @author Veronica Marinescu
 */
public class Record {
    private String name, type, location, coordinates, date, time, duration, counter;
    
    public Record(String n, String t, String l, String c, String d, String ti, String dur){
        this.name=n;
        this.type=t;
        this.location=l;
        this.coordinates=c;
        this.date=d;
        this.time=ti;
        this.duration=dur;
    }

    public Record(String s, String counter) {
        this.location = s;
        this.counter = counter;
    }
    public String getName(){
        return this.name;
    }
    
    public String getType(){
        return this.type;
    }
    
    public String getLocation(){
        return this.location;
    }
    
    public String getCoordinates(){
        return this.coordinates;
    }
    
    public String getDate(){
        return this.date;
    }
    
    public String getTime(){
        return this.time;
    }
    
    public String getDuration(){
        return this.duration;
    }
    
    public String getCounter(){
        return this.counter;
    }
    
    public boolean isThisWeek() throws ParseException {
        //Calendar.getInstance().clear();
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        String inputDate = sm.format(sm.parse(this.date));
        String now = sm.format(Calendar.getInstance().getTime());
        
        Calendar dayBeforeThisWeek = Calendar.getInstance();
        dayBeforeThisWeek.add(Calendar.DATE, -7);
        String lastWeek = sm.format(dayBeforeThisWeek.getTime());

        //System.out.println(lastWeek.replace("-", "") + " then " + inputDate.replace("-", "") + " then " + now.replace("-", ""));
        if(Integer.parseInt(inputDate.replace("-", "")) <= Integer.parseInt(now.replace("-", "")) && Integer.parseInt(inputDate.replace("-", "")) >= Integer.parseInt(lastWeek.replace("-", ""))){
            //System.out.println("TRUE");
            return true;}
        else{
            //System.out.println("FALSE");
            return false;}

    }
    
    
}
