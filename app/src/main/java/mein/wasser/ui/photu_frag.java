package mein.wasser.ui;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;

import mein.wasser.R;
import mein.wasser.adapters.hobbies_adapter;
import mein.wasser.adapters.personalAdapters;
import mein.wasser.extras.userdataa;
import mein.wasser.extras.wake_up;
import mein.wasser.tranforms.beeeeeeee;
import mein.wasser.tranforms.optimusprime;

public class photu_frag extends Fragment {
    private userdataa d;
    private String name = "";
    private String age = "";
    private String course = "";
    private String year = "";
    private String photots[];
    private ArrayList<String> hobbies;
    private ArrayList<String> photos;
    private String smoke = "";
    private FirebaseStorage fs;
    private String description = "";
    public photu_frag(userdataa data, FirebaseStorage sr) {
        this.d = data;
        this.fs = sr;
        this.photos = new ArrayList<>();
        convertor ob = new convertor();
        name = d.name;
        age = String.valueOf(ob.age(d.dob));
        course = ob.course(d.course);
        year = ob.year(d.course);
        Gson g =new Gson();
        photots = ob.initialize(g,d.photos);
        smoke = d.drink;
        hobbies = new ArrayList<String>();
        description = d.description;
        String[] temp  = ob.initialize(g,d.hobbies);
        for(int i=0;i<temp.length;i++)
        {
            hobbies.add(temp[i]);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.photu_fragment, container, false);
        StorageReference sr = fs.getReference();
        wake_up ob = new wake_up() {
            @Override
            public void heywakeup() {
                if(photos.size()==photots.length)
                    done_load(v);
            }
        };
        for(int i=0;i<photots.length;i++)
        {
            sr.child("images/"+photots[i]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    photos.add(uri.toString().trim());
                    ob.heywakeup();
                }
            });
        }
        return v;
    }
    private void done_load(View v)
    {
        TextView nameet = v.findViewById(R.id.name);
        TextView courseet = v.findViewById(R.id.course);
        nameet.setText(name+", "+age);
        String postfix = (Integer.parseInt(year)==1)?"st":((Integer.parseInt(year)==2)?"nd":"rd");
        courseet.setText(course+", "+year+postfix+" year");
        ViewPager2 viewPager2 = v.findViewById(R.id.viewpagerimages);
        TextView descriptionet = v.findViewById(R.id.description);
        descriptionet.setText(description);
        TextView habits = v.findViewById(R.id.habits);
        habits.setText(smoke);
        GridView hobbies = v.findViewById(R.id.hobbies);
        hobbies_adapter ada = new hobbies_adapter(requireContext(),R.layout.hobbies_card,this.hobbies,1);
        hobbies.setAdapter(ada);
        LottieAnimationView lottie = v.findViewById(R.id.swipeup);
        ScrollView sv = v.findViewById(R.id.scroll);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M)
            sv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                lottie.setVisibility(View.INVISIBLE);
            }
            });
        TabLayout tabs = v.findViewById(R.id.tabs);
        personalAdapters adapters = new personalAdapters(photos,requireContext());
        viewPager2.setPageTransformer(new beeeeeeee());
        viewPager2.setOffscreenPageLimit(6);
        viewPager2.setAdapter(adapters);
        new TabLayoutMediator(tabs,viewPager2,(tab,position) -> tab.setText("")).attach();
    }
}