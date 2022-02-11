package mein.wasser.extras;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Locale;

public class userdataa {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("gender")
    public String gender;
    @SerializedName("dob")
    public String dob;
    @SerializedName("course")
    public String course;
    @SerializedName("description")
    public String description;
    @SerializedName("drink")
    public String drink;
    @SerializedName("hobbies")
    public String hobbies;
    @SerializedName("photos")
    public String photos;
    @SerializedName("chats")
    public JsonObject chats;
}
