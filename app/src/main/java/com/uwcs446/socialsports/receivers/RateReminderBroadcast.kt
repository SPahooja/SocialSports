package com.uwcs446.socialsports.receivers

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.uwcs446.socialsports.R
import com.uwcs446.socialsports.ui.matchrating.MatchRatingActivity

class RateReminderBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Create an Intent for the activity you want to start
        val resultIntent = Intent(context, MatchRatingActivity::class.java)
        resultIntent.putExtra("MatchId", intent.getSerializableExtra("MatchId"))
        // Create the TaskStackBuilder
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        // we use notification compat builder to construct the details of our notification
        val builder = NotificationCompat.Builder(
            context,
            context.getString(R.string.match_rating_notification_channel_id)
        )
            .setContentIntent(resultPendingIntent)
            .setSmallIcon(R.drawable.ic_baseline_sports_24)
            .setContentTitle("Rate your match!")
            .setContentText("How was your game?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // call the static function from(Context) to get a NotificationManagerCompat object, and then call one of its methods to post or cancel notifications.
        val notificationManager = NotificationManagerCompat.from(context)

        // Post a notification to be shown in the status bar, stream, etc.
        notificationManager.notify(200, builder.build())
    }
}
