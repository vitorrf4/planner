package com.example.teste_api

import RetrofitClient
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.teste_api.databinding.ActivityMainBinding
import com.example.teste_api.models.Message
import com.example.teste_api.models.EVENT_STATUS
import com.example.teste_api.viewmodel.SSEViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = "TEST_SSE"
    private val viewModel = SSEViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSSEEvents()
        callSSE()

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
            }

            override fun onFailure(call: Call<Message>, t: Throwable) {
                val error = t.message
                binding.txtMessage.text = error
            }
        })
    }

    private fun callSSE() {
        viewModel.sseEvents.observe(this) {
            it?.let { event ->
                when(event.eventStatus) {
                    EVENT_STATUS.OPEN -> {
                        Log.d(TAG, "MAIN| Event open")
                    }

                    EVENT_STATUS.SUCCESS -> {
                        Log.d(TAG, "MAIN| Event successful")

                        var separatedData = event.message?.split(",")
                        separatedData?.forEach { s -> s.replace("\"", "") }

                        separatedData?.forEach { data -> binding.txtMessage.append("\n $data") }
                    }

                    EVENT_STATUS.ERROR -> {
                        Log.d(TAG, "MAIN| Event error")
                    }

                    EVENT_STATUS.CLOSED -> {
                        Log.d(TAG, "MAIN| Event closed")
                    }

                    else -> {}
                }
            }
        }

    }


}