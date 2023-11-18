package com.example.teste_api

import RetrofitClient
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.teste_api.databinding.ActivityMainBinding
import com.example.teste_api.models.Message
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


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

}