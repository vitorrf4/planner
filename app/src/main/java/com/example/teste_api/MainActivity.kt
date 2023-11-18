package com.example.teste_api

import RetrofitClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.teste_api.databinding.ActivityMainBinding
import com.example.teste_api.models.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = "TEST_SSE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sse()

        binding.btnSend.setOnClickListener {
            var msg = Message(binding.edtMessage.text.toString())

            sendMessage(msg)
        }

    }

    private fun sendMessage(msg : Message) {
        var service = RetrofitClient.createMessageService()
        var call: Call<Message> = service.addMessage(msg)

        call.enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>, r: Response<Message>) {
                var response = Message(r.body()?.message ?: "")

                binding.txtMessage.text = response.message
            }

            override fun onFailure(call: Call<Message>, t: Throwable) {
                val error = t.message
                binding.txtMessage.text = error
            }
        })
    }

    private fun sse() {
        val eventSourceListener = object : EventSourceListener() {
        override fun onOpen(eventSource: EventSource, response: okhttp3.Response) {
            super.onOpen(eventSource, response)
            Log.d(TAG, "Connection Opened")
        }

        override fun onClosed(eventSource: EventSource) {
            super.onClosed(eventSource)
            Log.d(TAG, "Connection Closed")
        }

        override fun onEvent(
            eventSource: EventSource,
            id: String?,
            type: String?,
            data: String
        ) {
            super.onEvent(eventSource, id, type, data)
            Log.d(TAG, "On Event Received! Data : $data")
            binding.txtMessage.text = binding.txtMessage.text.toString().plus(data);
        }

        override fun onFailure(eventSource: EventSource, t: Throwable?, response: okhttp3.Response?) {
            super.onFailure(eventSource, t, response)
            Log.d(TAG, "On Failure : ${response?.body}")
        }
    }

        val client = OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
            .build()

        val request = Request.Builder()
            .url("https://hot-parents-exist.loca.lt")
            .header("Accept", "application/json; q=0.5")
            .addHeader("Accept", "text/event-stream")
            .build()

        EventSources.createFactory(client)
            .newEventSource(request = request, listener = eventSourceListener)

        lifecycleScope.launchWhenCreated {
            withContext(Dispatchers.IO) {
                client.newCall(request).enqueue(responseCallback = object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        Log.e(TAG, "API Call Failure ${e.localizedMessage}")
                    }

                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        var data = response.body
                        Log.d(TAG, "Message sent! Data: $data")
                        binding.txtMessage.text = binding.txtMessage.text.toString().plus(data)
                    }
                })
            }
        }

    }

}