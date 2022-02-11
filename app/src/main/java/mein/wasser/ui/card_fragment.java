package mein.wasser.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.viewpager2.widget.ViewPager2;

import mein.wasser.adapters.*;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import mein.wasser.R;
import mein.wasser.extras.OutBound;
import mein.wasser.extras.cardAdapterToCardFrag;
import mein.wasser.extras.swipedin;
import mein.wasser.extras.userdataa;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class card_fragment extends Fragment {
    private ArrayList<userdataa> people;
    private data userdata;
    private String token;
    public card_fragment(ArrayList<userdataa> p,data userdata, String token)
    {
        this.userdata = userdata;
        this.token = token;
        this.people = p;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ImageLoader il = new ImageLoader(Volley.newRequestQueue(getContext()), new ImageLoader.ImageCache() {
            LruCache<String,Bitmap> cache = new LruCache<>(20);
            @Nullable
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url,bitmap);
            }
        });
        View v = inflater.inflate(R.layout.cards,container,false);
        Retrofit retoradapter = new Retrofit.Builder().baseUrl(getString(R.string.url)).addConverterFactory(ScalarsConverterFactory.create()).build();
        OutBound interf = retoradapter.create(OutBound.class);
        SwipePlaceHolderView holderView = v.findViewById(R.id.placeholder);
        holderView.getBuilder().setDisplayViewCount(people.size()).setSwipeDecor(new SwipeDecor().setPaddingTop(0)
                .setRelativeScale(0.01f));
        userdataa ob;
        String year = "";
        StringBuilder course = new StringBuilder();
        photo_clicked obb = new ViewModelProvider(requireActivity()).get(photo_clicked.class);
        for(int i=0;i<people.size();i++)
        {
            cardadapter c = new cardadapter(people.get(i).name,people.get(i).course,people.get(i).photos, FirebaseStorage.getInstance(),getContext(),people.get(i).id,people.get(i).dob,i);
            c.ob = new cardAdapterToCardFrag() {
                @Override
                public void clicked(int itemNumber) {
                    obb.clicked(itemNumber);
                }
            };
            c.swipeob = new swipedin() {
                @Override
                public void swipedin(String swipedid) {
                   interf.swiped(userdata.getId(),token,userdata.getEmail(),swipedid).enqueue(new Callback<String>() {
                       @Override
                       public void onResponse(Call<String> call, Response<String> response) {
                           Toast.makeText(requireContext(),response.body().toString(),Toast.LENGTH_LONG).show();
                           try{
                               int res = Integer.parseInt(response.body());
                           }catch(Exception e)
                           {
                               Toast.makeText(requireContext(),e.toString(),Toast.LENGTH_LONG).show();
                           }
                       }

                       @Override
                       public void onFailure(Call<String> call, Throwable t) {

                       }
                   });
                }
            };
            holderView.addView(c);
        }
        v.findViewById(R.id.reject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holderView.doSwipe(false);
            }
        });
        v.findViewById(R.id.like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LottieAnimationView  l = v.findViewById(R.id.hearts);
                l.playAnimation();
            }
        });
        v.findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holderView.doSwipe(true);
            }
        });
        return v;
    }
}