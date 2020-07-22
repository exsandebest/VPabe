package yoloyoj.pub.models

class Event(
    var eventid: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var date: Date? = null,
    var like: Like? = null,
    var authorid: Int? = null,
    var place: String? = null,
    var lat: Int? = null,
    var lng: Int? = null
) {
    override fun toString(): String {
        return "{ \n" +
                "eventid=${this.eventid},\n" +
                "place=${this.place},\n" +
                "name=${this.name},\n" +
                "description=${this.description},\n" +
                "date=${this.date.toString().replace("\n", "\n\t")},\n" +
                "like=${this.like.toString().replace("\n", "\n\t")},\n" +
                "authorid=${this.authorid}\n" +
                " }"
    }
}
