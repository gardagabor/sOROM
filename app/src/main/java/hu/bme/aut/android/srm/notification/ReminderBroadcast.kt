package hu.bme.aut.android.srm.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import hu.bme.aut.android.srm.R
import hu.bme.aut.android.srm.RecipeListActivity

class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val openIntent = Intent(context, RecipeListActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, openIntent, 0)

        var builder = context?.let {
            NotificationCompat.Builder(it, "sÖRÖM")
                .setSmallIcon(R.mipmap.icon)
                .setContentTitle("Dry-hop notification")
                .setContentText("Its time to dry hop")
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText("Its time to dry hop"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }

        var notificationManager = context?.let { NotificationManagerCompat.from(it) }

        builder?.let { notificationManager?.notify(100, it.build()) }
    }
}