package mein.wasser.ui;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class convertor {
    public String[] initialize(Gson g,String hobbies)
    {
        StringBuilder temp = new StringBuilder();
        hobbies = hobbies.replace(" ","").replace("[","").replace("]","").trim();
        hobbies = '"'+hobbies + '"';
        for(int i=0;i<hobbies.length();i++)
        {
            if(hobbies.charAt(i)==',')
            {
                temp.append('"');
                temp.append(hobbies.charAt(i));
                temp.append('"');
                continue;
            }
            temp.append(hobbies.charAt(i));
        }
        hobbies = "["+temp.toString()+"]";
        return g.fromJson(hobbies,String[].class);
    }
    public int age(String d)
    {
        try{
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        int currentyear = cal.get(Calendar.YEAR);
        int currentmonth = cal.get(Calendar.MONTH);
        int currentdate = cal.get(Calendar.DAY_OF_MONTH);
        cal = (Calendar) cal.clone();
        cal.setTime(date.parse(d));
        int enteredyear = cal.get(Calendar.YEAR);
        int enteredmonth  = cal.get(Calendar.MONTH);
        int enteredday = cal.get(Calendar.DAY_OF_MONTH);
        return (currentyear-enteredyear+((currentmonth-enteredmonth)/12));
        }catch (Exception e){
            return -1;
        }
    }
    public String course(String c)
    {
        StringBuilder course = new StringBuilder();
        for(int s=0;s<c.length()-1;s++)
        {
            course.append(c.charAt(s));
        }
        return course.toString();
    }
    public String year(String c)
    {
        return String.valueOf(c.charAt(c.length()-1));
    }
}
