package yoloyoj.pub.models

class Message {
    var _rowid_: Int? = null
    var text: String? = null
    var chatid: Int? = null
    var sender: String? = null
    var avatar: String? = null

    fun hasNulls(): Boolean {
        return (text == null) or (sender == null)
    }
}
