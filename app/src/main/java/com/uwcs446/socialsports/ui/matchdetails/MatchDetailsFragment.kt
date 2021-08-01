package com.uwcs446.socialsports.ui.matchdetails

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.uwcs446.socialsports.R
import com.uwcs446.socialsports.databinding.FragmentMatchDetailsBinding
import com.uwcs446.socialsports.receivers.RateReminderBroadcast
import com.uwcs446.socialsports.ui.matchlist.MatchListUtils
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MatchDetailsFragment : Fragment() {

    private val args: MatchDetailsFragmentArgs by navArgs()
    private var _binding: FragmentMatchDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentMatchDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        createNotificationChannel()
        // Placeholder find the match that matches the passed in ID
        val matches = MatchListUtils.genFakeMatchData(args.matchId!!.toInt())

        val match = matches.find { match -> match.id == args.matchId }

        // Views
        val titleTextView: TextView = binding.matchTitle
        val matchSportIconImageView = binding.matchSportIcon
        val matchTypeTextView: TextView = binding.matchType
        val playerCountTextView: TextView = binding.matchPlayerCount
        val dateTextView: TextView = binding.matchDate
        val timeTextView: TextView = binding.matchTime
        val locationNameTextView: TextView = binding.matchLocationName
        val addressTextView: TextView = binding.matchAddress
        val descriptionTextView: TextView = binding.matchDescription
        val hostNameTextView: TextView = binding.matchHostName

        val joinMatchButton: Button = binding.joinMatchButton

        joinMatchButton.setOnClickListener {
            Toast.makeText(context, "TYPICAL", Toast.LENGTH_SHORT).show()
            Toast.makeText(context, match?.id, Toast.LENGTH_SHORT).show()
            val intent = Intent(context, RateReminderBroadcast::class.java)
            intent.putExtra("MatchId", match?.id)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

            val timeAtButtonClick: Long = System.currentTimeMillis()

            // 1 second is 1000 millis
            val tenSecondsInMillis: Long = 1000 * 10

            alarmManager?.set(AlarmManager.RTC_WAKEUP,
            timeAtButtonClick + tenSecondsInMillis,
             pendingIntent)

        }

        if (match != null) {
            titleTextView.setText(match.title)
            matchSportIconImageView.setImageResource(match.sport.imageResource)
            matchTypeTextView.setText(match.sport.toString())
            playerCountTextView.setText("${match.currentPlayerCount()} / ${match.maxPlayerCount()}")
            dateTextView.setText(match.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
            timeTextView.setText(match.time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)))
            locationNameTextView.setText("High Park") // TODO: add location name field
            addressTextView.setText("1873 Bloor St W, Toronto, ON M6R 2Z") // TODO: add location address field
            descriptionTextView.setText(match.description)
            hostNameTextView.setText("John Smith") // TODO: Use names from user
        }

        return root
    }

    // All this does is set up the channel
    //  Links it to the channel id, and the channel name and importance
    //  Creates the notification manager, by getting the systemService notificationManager
    //  Adds the notification channel we just created to the notification manager
    // private fun createNotificationChannel(channelId: String, channelName: String) {
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.match_rating_notification_channel_name)
            val descriptionText = getString(R.string.match_rating_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(getString(R.string.match_rating_notification_channel_id), name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
