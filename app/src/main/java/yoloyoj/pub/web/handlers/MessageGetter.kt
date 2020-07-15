package yoloyoj.pub.web.handlers

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yoloyoj.pub.models.Message
import yoloyoj.pub.web.apiClient

class MessageGetter : Callback<List<Message>?> {
    var messageUpdater: ((List<Message>) -> Unit)? = null

    fun start(
        chatid: Int, after: Int
    ) {
        apiClient.getMessages(
            chatid, after
        )?.enqueue(this)
    }

    override fun onFailure(call: Call<List<Message>?>, t: Throwable) {
        Log.i("onFailure", t.localizedMessage)
        messageUpdater?.let { it(emptyList()) }
    }

    override fun onResponse(call: Call<List<Message>?>, response: Response<List<Message>?>) {
        messageUpdater?.let { it(response.body()?.filter { !it.hasNulls() }!!) }
    }
}