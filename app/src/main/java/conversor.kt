import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Conversor {
    object Moedas {
        fun retornaMoedas() : Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://api.exchangeratesapi.io/latest")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
    }
}