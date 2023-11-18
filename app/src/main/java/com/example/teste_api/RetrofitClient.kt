import com.example.teste_api.services.MessageService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class RetrofitClient {
    companion object {
        private const val BASE_URL = "  https://every-swans-wish.loca.lt"
        private lateinit var INSTANCE : Retrofit

        private fun getRetrofitInstance() : Retrofit{
            if(!::INSTANCE.isInitialized) {
                val HTTP = OkHttpClient.Builder()

                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(HTTP.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return INSTANCE
        }

        fun createMessageService(): MessageService {
            return getRetrofitInstance().create(MessageService::class.java)
        }
    }
}