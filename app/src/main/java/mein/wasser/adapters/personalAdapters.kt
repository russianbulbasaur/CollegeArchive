package mein.wasser.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.os.Handler
import android.os.Message
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import com.bumptech.glide.Glide
import mein.wasser.R
import mein.wasser.blurry.Blurry
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class personalAdapters(images:ArrayList<String>,c:Context): RecyclerView.Adapter<personalAdapters.ViewHolder>(){
    lateinit var imageURLs:ArrayList<String>;
    lateinit var context:Context;
    init {
        this.imageURLs = images;
        this.context =c;
    }
        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            var image: ImageView;
            var pd : ProgressBar;
            init {
                this.pd = v.findViewById(R.id.loading);
                this.image = v.findViewById(R.id.im);
            }
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.addOnLayoutChangeListener(object:View.OnLayoutChangeListener
        {
            override fun onLayoutChange(
                p0: View?,
                p1: Int,
                p2: Int,
                p3: Int,
                p4: Int,
                p5: Int,
                p6: Int,
                p7: Int,
                p8: Int
            ) {
                holder.pd.visibility = View.INVISIBLE
            }
        })
        Glide.with(context).load(imageURLs.get(position)).into(holder.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.images,parent,false);
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return imageURLs.size;
    }
}