package mein.wasser.extras;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class json_decode {
    public userdataa decode(String ob)
    {
        Gson g = new Gson();
        return g.fromJson(ob,userdataa.class);
    }
}