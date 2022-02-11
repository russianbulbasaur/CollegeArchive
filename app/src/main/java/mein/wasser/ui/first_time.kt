package mein.wasser.ui

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.tabs.TabLayout
import mein.wasser.R;

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [first_time.newInstance] factory method to
 * create an instance of this fragment.
 */
class first_time : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_first_time, container, false);
        val viewpager:ViewPager = v.findViewById<ViewPager>(R.id.first_time_viewpager);
        val tabs = v.findViewById<TabLayout>(R.id.tabs);
        val login_text = v.findViewById<TextView>(R.id.login_text)
        val signup_text = v.findViewById<TextView>(R.id.signup_text)
        login_text.setOnClickListener(object: View.OnClickListener
        {
            override fun onClick(p0: View?) {
                val ob : fragment_commands by activityViewModels()
                val hash = HashMap<Int,Bundle>();
                val bundle = Bundle();
                bundle.putInt("code",0);
                hash[0] = bundle;
                ob.set_code(hash);
            }
        })
        tabs.setupWithViewPager(viewpager)
        viewpager.offscreenPageLimit = 4
        viewpager.adapter = intro_adapter(context as Context,fragmentManager as FragmentManager);
        return v
    }

    companion object {

    }
}
class intro_adapter(con: Context, manager: FragmentManager) : FragmentPagerAdapter(manager)
{
    lateinit var context: Context;
    init {
        this.context = con;
    }

    override fun getItem(position: Int): Fragment {
       return intro_anim1(position)
    }

    override fun getCount(): Int {
        return 4
    }
}
class intro_anim1(posi:Int) : Fragment()
{
    lateinit var lottie:LottieAnimationView;
    var position:  Int = 0
    init {
        this.position = posi
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        if(position==0 && FullscreenActivity.start==1)
        {
            lottie.progress = 0.49f
            lottie.pauseAnimation()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v: View? = null;
        if(position==0)
        {
            v = inflater.inflate(R.layout.intro_anim1,container,false);
            lottie = v.findViewById(R.id.lottie_anim1);
            lottie.setAnimation("jump.json")
            lottie.repeatCount = 200
            lottie.playAnimation()
            val handler = object:Handler()
            {
                override fun handleMessage(msg: Message) {
                    super.handleMessage(msg)
                    lottie.pauseAnimation()
                    FullscreenActivity.start = 1;
                }
            }
            object:CountDownTimer(2130,1)
            {
                override fun onTick(p0: Long) {

                }

                override fun onFinish() {
                    handler.sendEmptyMessage(0)
                }
            }.start()
        }
        else if(position==1) {
            v = inflater.inflate(R.layout.intro_anim2, container, false);
            lottie = v.findViewById(R.id.lottie_anim1);
            lottie.setAnimation("talking_friends.json")
            lottie.repeatCount = 200
            lottie.playAnimation()
        }
        else if(position==2) {
            v = inflater.inflate(R.layout.intro_anim3, container, false);
            lottie = v.findViewById(R.id.lottie_anim1);
            lottie.setAnimation("encryption.json")
            lottie.repeatCount = 200
            lottie.playAnimation()
        }
        else if(position==3) {
            v = inflater.inflate(R.layout.intro_anim4, container, false);
            lottie = v.findViewById(R.id.lottie_anim1);
            lottie.setAnimation("intro.json")
            lottie.repeatCount = 200
            lottie.playAnimation()
        }
        return v
    }
}