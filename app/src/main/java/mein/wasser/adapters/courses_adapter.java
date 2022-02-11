package mein.wasser.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import mein.wasser.R;

public class courses_adapter extends ArrayAdapter<String> {
    private ArrayList<String> courses_list;
    private ArrayList iconlist;
    private Context con;
    private Bitmap b[];
    public courses_adapter(@NonNull Context context, int resource, ArrayList<String> courses,ArrayList icons) {
        super(context, resource);
        this.iconlist = icons;
        this.courses_list = courses;
        this.con = context;
        this.b = new Bitmap[iconlist.size()];
        for(int i=0;i<iconlist.size();i++)
        {
            b[i] = BitmapFactory.decodeResource(con.getResources(), (Integer) iconlist.get(i));
        }
    }

    @Override
    public int getCount() {
        return courses_list.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        course_card_holder holder;
        if(convertView==null)
        {
         convertView = LayoutInflater.from(con).inflate(R.layout.course_card,parent,false);
         holder = new course_card_holder();
         holder.tv = convertView.findViewById(R.id.coursename);
         holder.im = convertView.findViewById(R.id.courseimage);
         holder.im.setImageBitmap(b[position]);
         holder.tv.setText(courses_list.get(position));
         convertView.setTag(holder);
        }
        else
        {
            holder = (course_card_holder) convertView.getTag();
            holder.tv.setText(courses_list.get(position));
            holder.im.setImageBitmap(b[position]);
        }
        return convertView;
    }
}
class course_card_holder
{
    ImageView im;
    TextView tv;
}
