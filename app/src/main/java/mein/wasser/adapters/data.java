package mein.wasser.adapters;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;

import mein.wasser.extras.userdataa;

public class data implements Parcelable {
    public static Parcelable.Creator<data> CREATOR = new Creator<data>() {
        @Override
        public data createFromParcel(Parcel parcel) {
            return new data(parcel);
        }

        @Override
        public data[] newArray(int i) {
            return new data[0];
        }
    };
    private String name;
    private String id;
    private String email;
    private String gender;
    private String dob;
    private String course;
    private String description;
    private String drink;
    private String hobbies;
    private String photos;
    private JsonObject chats;
    @Override
    public int describeContents() {
        return 0;
    }
    public data(Parcel p)
    {
        this.id = p.readString();
        this.name =  p.readString();
        this.email = p.readString();
        this.gender = p.readString();
        this.dob = p.readString();
        this.course = p.readString();
        this.description = p.readString();
        this.drink = p.readString();
        this.hobbies = p.readString();
        this.photos = p.readString();
    }
    public data(userdataa data)
    {
        this.id = data.id;
        this.name = data.name;
        this.email = data.email;
        this.gender = data.gender;
        this.dob = data.dob;
        this.course = data.course;
        this.description = data.description;
        this.drink = data.drink;
        this.hobbies = data.hobbies;
        this.photos = data.photos;
    }
    public String getId(){ return this.id; }
    public String getName() { return this.name; }
    public String getEmail() { return this.email; }
    public String getGender() { return this.gender; }
    public String getDob() { return this.dob; }
    public String getCourse() { return this.course; }
    public String getDescription() { return this.description; }
    public String getDrink() { return this.drink; }
    public String getHobbies() { return this.hobbies; }
    public String getPhotos() { return this.photos; }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(gender);
        parcel.writeString(dob);
        parcel.writeString(course);
        parcel.writeString(description);
        parcel.writeString(drink);
        parcel.writeString(hobbies);
        parcel.writeString(photos);
    }
}
