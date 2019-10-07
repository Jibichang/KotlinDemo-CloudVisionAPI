package org.lox.kotlindemo

import android.app.*
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.NotificationCompat
import android.support.v4.app.RemoteInput
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.lox.kotlindemo.model.AdapterList
import org.lox.kotlindemo.model.Data
import org.lox.kotlindemo.model.FeedReaderDbHelper
import org.lox.kotlindemo.model.OnItemClickListener
import java.util.*
import android.app.NotificationManager as NotificationManager1

class MainActivity : AppCompatActivity() {
    lateinit var textView: TextView
    lateinit var list: List<Data?>
    lateinit var recycleView: RecyclerView
    lateinit var adapter: AdapterList
    private val CHANNEL_ID = "ch1"
    private val KEY_TEXT_REPLY = "key_text_reply"

    private val KEY_MESSAGE_ID = "key_message_id"
    private val KEY_NOTIFY_ID = "key_notify_id"
    lateinit var newMessageNotification :Notification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val dbHelper = FeedReaderDbHelper(applicationContext)
//        val db = dbHelper.writableDatabase

//        val values = ContentValues().apply {
//            put(FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_NAME, "test")
//            put(FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_TYPE, "dsfd")
//            put(FeedReaderDbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_NUMBER, 13.toString())
//        }
//
//        val newRowId = db?.insert(FeedReaderDbHelper.FeedReaderContract.FeedEntry.TABLE_NAME, null, values)

        fab.setOnClickListener { view ->
            notifyChannel(getGreetingMessage())
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val data = Data("Tom", "Kid");
        data.Type = "Old"
        data.Sum = 10

        var msg: String = ""
        msg += data.print()

        setupList()
        textView = findViewById(R.id.text)
        textView.text = msg
    }

    private fun getGreetingMessage():String{
        val c :Calendar = Calendar.getInstance()
        return when (c.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            in 16..20 -> "Good Evening"
            in 21..23 -> "Good Night"
            else -> {
                "Hello"
            }
        }
    }

    private fun notifyChannel(title: String) {
        val replyLabel = "Ex. Name:Type.Number"
        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
            .setLabel(replyLabel)
            .build()
        val intentReply = Intent(this, MainActivity::class.java)
        intentReply.action = "org.lox.kotlindemo"
        intentReply.putExtra(KEY_MESSAGE_ID, 1)
        intentReply.putExtra(KEY_NOTIFY_ID, 101)

        val resultPendingIntent : PendingIntent = PendingIntent.getActivity(
            this,
            1,
            intentReply,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val replyAction :NotificationCompat.Action =  NotificationCompat.Action.Builder(
            R.drawable.kotlin_logo, "Reply", resultPendingIntent)
            .addRemoteInput(remoteInput)
            .setAllowGeneratedReplies(true)
            .build()

        val targetTime = Calendar.getInstance()
        targetTime.add(Calendar.MINUTE, +1)

        newMessageNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.kotlin_logo)
            .setContentTitle(title)
            .addAction(replyAction)
            .setColor(resources.getColor(R.color.colorPrimary))
            .setWhen(targetTime.timeInMillis)
            .setOngoing(true)
            .build()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager1

        notificationManager.notify(
            101,
            newMessageNotification
        )
    }

    private fun handleIntent() {
        var intent :Intent = this.intent
        val remoteInput :Bundle? = RemoteInput.getResultsFromIntent(intent)
        if (remoteInput != null) {
            val inputString :String = remoteInput?.getCharSequence(
                KEY_TEXT_REPLY
            ).toString()
            Log.d("handleIntent", inputString)

            val indexCo :Int = inputString.indexOf(':')
            val indexNum :Int = inputString.indexOf('.', indexCo)

            Log.d("handleIntent", inputString.substring(0, indexCo) +
                    " - ${inputString.substring(indexNum+1)}" +
                    " - ${inputString.substring(indexCo+1, indexNum)}")

            val newItem :Data? = Data(inputString.substring(0, indexCo)
                , inputString.substring(indexCo+1, indexNum)
                , (inputString.substring(indexNum+1)).toInt())
            adapter.addItem(newItem, adapter.itemCount)
            Log.d("handleIntent", "${adapter.itemCount}")

            clearExistingNotifications()
            finish()
        }
    }

    private fun clearExistingNotifications() {
        val notificationId = intent.getIntExtra(KEY_NOTIFY_ID, 0)
        val manager:NotificationManager1 = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager1
        manager.cancel(notificationId)
    }

    override fun onStart() {
        super.onStart()
        handleIntent()
    }

    private fun setupList() {
        list = arrayListOf(Data("Tom", "Kid"))
        list = list + Data("Jerry", "Adult", 35)
        list = list + Data("Jane", null, 32)

        setupRecycle()
    }

    private fun setupRecycle() {
        adapter = AdapterList(list, this.applicationContext)
        recycleView = findViewById(R.id.recycle)
        recycleView.layoutManager = LinearLayoutManager(this.applicationContext)
        recycleView.adapter = adapter

        adapter.mOnItemClickListener = object : OnItemClickListener {
            override fun onItemClick(data: Data?) {
                Toast.makeText(this@MainActivity, "output => ${data?.print()} ", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


}
