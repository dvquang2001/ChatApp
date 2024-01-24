package jetpack.tutorial.firstattempt.presentation.util

import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.Locale

fun Timestamp.getShortDate():String{
    val ts = this.toDate().time
    //Get instance of calendar
    val calendar = Calendar.getInstance(Locale.getDefault())
    //get current date from ts
    calendar.timeInMillis = ts
    //return formatted date
    return android.text.format.DateFormat.format("E, dd MMM yyyy", calendar).toString()
}