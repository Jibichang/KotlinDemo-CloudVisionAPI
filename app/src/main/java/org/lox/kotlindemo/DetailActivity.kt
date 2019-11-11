package org.lox.kotlindemo

import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.api.client.extensions.android.json.AndroidJsonFactory
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.util.IOUtils
import com.google.api.services.vision.v1.Vision
import com.google.api.services.vision.v1.VisionRequestInitializer
import com.google.api.services.vision.v1.model.*

import kotlinx.android.synthetic.main.activity_detail.*
import java.io.IOException
import java.lang.reflect.Array

class DetailActivity : AppCompatActivity() {
    private lateinit var listWord :List<TextDetection>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setVisionRequestInitializer()
    }

    private fun setVisionRequestInitializer() {
        Log.d("setVisionRequest", "setVisionRequestInitializer")
        val visionBuilder = Vision.Builder(
            NetHttpTransport(),
            AndroidJsonFactory(),
            null)
        visionBuilder.setVisionRequestInitializer(
            VisionRequestInitializer("AIzaSyDkG8DBYTw25v4hnLvyEYgYaAuS0Mn1gto"))
        val vision : Vision = visionBuilder.build()

        AsyncTask.execute(Runnable {
            val inputStream = resources.openRawResource(R.raw.photo)
            try {
                Log.d("setVisionRequest", "try")
                val photoDate = inputStream.readBytes()
                inputStream.close()

                val inputImage = Image()
                inputImage.encodeContent(photoDate)

                val  desiredFeature = Feature()
                desiredFeature.type = "TEXT_DETECTION"

                val request = AnnotateImageRequest()
                request.image = inputImage
                request.features = listOf(desiredFeature)

                val batchRequest = BatchAnnotateImagesRequest()
                batchRequest.requests = listOf(request)

                val batchResponse =
                    vision.images().annotate(batchRequest).execute()

                val result = batchResponse.responses[0].fullTextAnnotation
                /*listWord = arrayListOf()
                for (res in result) {
                    for (annotation in res.textAnnotations) {
//                        Log.d("setVisionRequest", "Text: ${annotation.description}")
//                        Log.d("setVisionRequest", "Position : ${annotation.boundingPoly}")
                        val bag = arrayListOf<String>(":", "TAX", "ID", "SUB")
                        if (!bag.contains(annotation.description)){
                            var vertex0 :Vertex = annotation.boundingPoly.vertices[0]

                            val p0 = "${vertex0.x}:${vertex0.y}"
                            listWord = listWord + TextDetection(annotation.description, p0)
                        }
                    }
                }

                for (item in listWord){
                    Log.d("setVisionRequest", "${item.item} : ${item.position}")
                }*/

                runOnUiThread(Runnable {
                    val textView = this.findViewById<TextView>(R.id.text_detail)
                    textView.text = result.text
                    Log.d("setVisionRequest", result.text)
                })

            } catch(e : IOException){
                e.printStackTrace()
            }
        })
    }
}
