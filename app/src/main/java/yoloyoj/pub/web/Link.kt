package yoloyoj.pub.web

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import yoloyoj.pub.models.Message
import yoloyoj.pub.models.User

@Suppress("unused")
public interface Link {
    @GET("putmessage")
    fun putMessage(
        @Query("text") text: String,
        @Query("sender") sender: Int,
        @Query("chatid") chatid: Int
    ): Call<ResponseBody?>?

    @GET("getmessages")
    fun getMessages(
        @Query("chatid") chatid: Int,
        @Query("after") after: Int
    ): Call<List<Message>?>?

    @GET("getuser")
    fun getUser(
        @Query("userid") userid: Int = 0,
        @Query("telephone") telephone: String = ""
    ): Call<User?>

    @GET("getaction")
    fun getAction(): Call<String?>?
}