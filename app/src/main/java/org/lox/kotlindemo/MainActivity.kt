package org.lox.kotlindemo

import android.app.*
import android.app.Notification.EXTRA_NOTIFICATION_ID
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
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import org.lox.kotlindemo.model.AdapterList
import org.lox.kotlindemo.model.Data
import org.lox.kotlindemo.model.OnItemClickListener

class MainActivity : AppCompatActivity() {
    lateinit var textView: TextView
    lateinit var list: List<Data?>
    lateinit var recycleView: RecyclerView
    private val CHANNEL_ID = "ch1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

        }

        val data = Data("Tom", "Kid");
        data.Type = "Old"
        data.Sum = 10

        var msg: String = ""
        msg += data.print()
//        msg = "name : ${dataStr.Name} type : ${dataStr.Type} sum : ${dataStr.Sum}"

        setupList()
        textView = findViewById(R.id.text)
        textView.text = msg
    }

    fun setupList(){
        list = arrayListOf(Data("Tom", "Kid"))
        list = list + Data("Jerry", "Adult", 35)
        list = list + Data("Jane", null, 32)

        setupRecycle()
    }

    fun setupRecycle(){
        val adapter = AdapterList(list, this.applicationContext)
        recycleView = findViewById(R.id.recycle)
        recycleView.layoutManager = LinearLayoutManager(this.applicationContext)
        recycleView.adapter = adapter

        adapter.mOnItemClickListener = object : OnItemClickListener {
            override fun onItemClick(data: Data?) {
                Toast.makeText(this@MainActivity,"output : ${data?.print()} ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.app_name)
            val descriptionText = "Test Notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("ch1", name, importance)
            mChannel.description = descriptionText

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    fun notifyChannel(title: String, text: String){
        createNotificationChannel()
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.kotlin_logo)
            .setContentTitle(title)
            .setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText(text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }

    fun settingNotification(){
        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL_ID)
        }
        startActivity(intent)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, dataStr: Intent?) {
//        super.onActivityResult(requestCode, resultCode, dataStr)
//        // When an Image is picked
//        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == Activity.RESULT_OK
//            && null != dataStr
//        ) {
//            if (dataStr.getClipData() != null) {
//                var count = dataStr.clipData.itemCount
//                for (i in 0..count - 1) {
//                    var imageUri: Uri = dataStr.clipData.getItemAt(i).uri
//                    getPathFromURI(imageUri)
////                    Log.e("imagePath Multi", dataStr.dataStr.path);
//                }
//            } else if (dataStr.getData() != null) {
//                var imagePath: String = dataStr.dataStr.path
//                Log.e("imagePath", imagePath);
//            }
////            displayImageData()
////            textView.text = msg
//        }
//    }

}
