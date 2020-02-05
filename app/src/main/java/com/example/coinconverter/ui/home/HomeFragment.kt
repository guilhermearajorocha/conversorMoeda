package com.example.coinconverter.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.coinconverter.R
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    fun retornaMoedas() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.exchangeratesapi.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val btnConverter: Button = root.findViewById(R.id.btnConverter)

        btnConverter.setOnClickListener{
            val txt: TextView = root.findViewById(R.id.txtRetorno)
            val edt: EditText = root.findViewById(R.id.txtValor)
            var vs: valoresService = retornaMoedas().create(valoresService::class.java)
            val moedas = vs.fetchValores();
            moedas.enqueue(object: Callback<Valores>{
                override fun onResponse(call: Call<Valores>, response: Response<Valores>) {
                    if (edt.text.toString().toDouble() > 0){
                        val euro = response.body()?.rates?.brl
                        val resultado: Double = edt.text.toString().toDouble() * euro.toString().toDouble()

                        txt.text = "Valor em euros EUR " + "%.2f".format(resultado)

                        // val historicoDBHelper: HistoricoDBHelper = HistoricoDBHelper(requireContext())

                        // val valores: ValoresHistorico = ValoresHistorico(edt.text.toString().toDouble(), euro.toString().toDouble())

                        // historicoDBHelper.inserirValor(valores);
                    }
                    else{
                        // Não sei como declarar o context
                        // Toast.makeText(root, "Erro", Toast.LENGTH_LONG)
                    }
                }

                override fun onFailure(call: Call<Valores>, t: Throwable) {
                    txt.text = "Erro ao retornar o valor.";
                }
            })
        }

        return root
    }
}