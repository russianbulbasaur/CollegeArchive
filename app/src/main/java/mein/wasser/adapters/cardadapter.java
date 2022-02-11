package mein.wasser.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import java.util.ArrayList;
import mein.wasser.R;
import mein.wasser.blurry.Blurry;
import mein.wasser.extras.cardAdapterToCardFrag;
import mein.wasser.extras.swipedin;
import mein.wasser.extras.wake_up;
import mein.wasser.tranforms.beeeeeeee;
import mein.wasser.ui.convertor;

@Layout(R.layout.card)
public class cardadapter {
    private String name;
    private String course;
    @View(R.id.name)
    private TextView nametv;
    @View(R.id.course)
    private TextView coursetv;
    @View(R.id.loading)
    private ProgressBar pb;
    private int s = 0;
    @View(R.id.tindercard)
    private MaterialCardView card;
    public  swipedin swipeob;
    private StorageReference ref;
    @View(R.id.loading)
    private ProgressBar loading;
    private String[] photos;
    private Context con;
    private wake_up w;
    private DatabaseReference parentRef;
    private String uid;
    private int itemNumber = 0;
    private ImageLoader il;
    public cardAdapterToCardFrag ob;
    private String age = "";
    private String year = "";
    private final ArrayList<String> imageURLs;
    @View(R.id.image)
    public ImageView image;
    public cardadapter(String n, String c, String p, FirebaseStorage sr, Context con,String id,String d,int i)
    {
        this.itemNumber = i;
        imageURLs = new ArrayList<>();
        this.con = con;
        this.uid = id;
        this.name = n;
        convertor obc = new convertor();
        this.photos = obc.initialize(new Gson(),p);
        ref = sr.getReference();
        course =  obc.course(c);
        year = obc.year(c);
        age = String.valueOf(obc.age("12/10/2002"));
        parentRef = FirebaseDatabase.getInstance(con.getString(R.string.databaseurl)).getReference();
    }
    @Resolve
    public void bind()
    {
        nametv.setText(name+", "+age);
        String postfix = (Integer.parseInt(year)==1)?"st":((Integer.parseInt(year)==2)?"nd":"rd");
        coursetv.setText(course+", "+year+postfix+" year");
        w = new wake_up() {
            @Override
            public void heywakeup() {
                if(s==(photos.length-1))
                    loadImages();
                else
                    s++;
            }
        };
        for(int i=0;i<photos.length;i++)
        {
            getImages(i);
        }
    }
    public void getImages(int i)
    {
        ref.child("images/" + photos[i]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imageURLs.add(uri.toString().trim());
                w.heywakeup();
            }
        });
    }
    public void loadImages()
    {
        Glide.with(con).load(imageURLs.get(0)).into(image);
        image.addOnLayoutChangeListener(new android.view.View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(android.view.View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if(image.getDrawable()!=null) {
                    loading.setVisibility(android.view.View.INVISIBLE);
                    image.setOnClickListener(new android.view.View.OnClickListener() {
                        @Override
                        public void onClick(android.view.View view) {
                            ob.clicked(itemNumber);
                        }
                    });
                }
            }
        });
    }
    @SwipeOutState
    public void swipingout()
    {

    }
    @SwipeInState
    public void swipingin()
    {

    }
    @SwipeOut
    public void swipedout()
    {
        swipeob.swipedin(uid);
    }
    @SwipeIn
    public void swipedin()
    {

    }
}