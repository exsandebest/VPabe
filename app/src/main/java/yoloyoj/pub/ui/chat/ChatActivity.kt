package yoloyoj.pub.ui.chat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_chat.*
import yoloyoj.pub.R
import yoloyoj.pub.web.apiClient
import yoloyoj.pub.web.handlers.MessageSender
import java.io.File


const val MY_USER_ID = 1

const val EXTRA_CHATID = "chatid"

const val CODE_GET_PICTURE = 1

class ChatActivity : AppCompatActivity() {

    private lateinit var viewModel: ChatViewModel
    private lateinit var messages: MessagesData

    private lateinit var messageSender: MessageSender

    private var chatid: Int? = null

    private var attachmentLinks: MutableList<String> = mutableListOf()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            1 -> putImage(data!!.data!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatid = intent.getIntExtra(EXTRA_CHATID, 0)

        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        viewModel.chatid = chatid

        messages = viewModel.messages
    }

    override fun onStart() {
        viewModel.messageGetter.start( chatid!!, 0)

        messageSender = MessageSender(sendButton)

        messages.observeForever { loadAdapter() }

        messagesView.layoutManager = LinearLayoutManager(this)

        loadOnClicks()

        super.onStart()
    }

    private fun loadAdapter() {
        try {
            messagesView?.adapter = ChatAdapter(
                messages.value!!
            )
            messagesView.scrollToPosition(
                messagesView.adapter!!.itemCount - 1
            )
        } catch (e: Exception) {
        }
    }

    private fun loadOnClicks() {
        sendButton.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        apiClient.putMessage(
            editMessage.text.toString(),
            MY_USER_ID,
            chatid!!,
            attachmentLinks.joinToString(";")
        )?.enqueue(messageSender)
        editMessage.text.clear()

        onImageSent()
    }

    @Suppress("UNUSED_PARAMETER")
    fun addAttachment(view: View) {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }

        startActivityForResult(
            Intent.createChooser(intent, "Select picture"),
            CODE_GET_PICTURE
        )
    }

    private fun putImage(uri: Uri) {
        val file = File(uri.path)

        val storage = FirebaseStorage.getInstance()
        val storageReference = storage
            .getReferenceFromUrl("gs://vpabe-75c05.appspot.com") // TODO: remove hardcode
            .child("${file.hashCode()}.${uri.path!!.split(".").last()}")

        storageReference.putFile(uri)
        storageReference.downloadUrl.addOnSuccessListener {
            onImagePutted(it.path!!)
        }
    }

    private fun onImagePutted(link: String) {
        addAttachment.drawable.setTint(resources.getColor(R.color.colorAccent))

        attachmentLinks.add("https://firebasestorage.googleapis.com$link?alt=media")
    }

    private fun onImageSent() {
        addAttachment.drawable.setTint(resources.getColor(R.color.colorAccentBored))

        attachmentLinks.clear()
    }
}
