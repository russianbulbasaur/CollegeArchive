package mein.wasser.extras

import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface OutBound {
    @FormUrlEncoded
    @POST("/people.php")
    fun people(@Field("id") id:String, @Field("idtoken") idtoken:String, @Field("email") email:String):Call<String>
    @FormUrlEncoded
    @POST("/swipedin.php")
    fun swiped(@Field("id") id:String, @Field("idtoken") idtoken:String, @Field("email") email:String,@Field("swipedid") swipedid:String):Call<String>
}