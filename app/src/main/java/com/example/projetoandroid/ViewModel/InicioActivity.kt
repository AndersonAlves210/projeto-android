package com.example.projetoandroid.ViewModel

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.projetoandroid.databinding.ActivityTelaInicioBinding
import com.example.projetoandroid.model.CurrentResponseApi
import com.github.matteobattilana.weather.PrecipType
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.graphics.Color
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projetoandroid.Adapter.ForecastAdapter
import com.example.projetoandroid.R
import com.example.projetoandroid.model.ForecastResponseApi
import com.google.firebase.auth.FirebaseAuth
import eightbitlab.com.blurview.RenderScriptBlur
import java.util.Calendar

class InicioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTelaInicioBinding
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val calendar by lazy { Calendar.getInstance() }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val forecastAdapter by lazy { ForecastAdapter() }

    private var isMenuVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        binding.apply {
            addCity.setOnClickListener {
                isMenuVisible = false // Oculta o menu
                invalidateOptionsMenu() // Atualiza o menu
                startActivity(Intent(this@InicioActivity, CityListActivity::class.java))
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Verifica se há dados passados pelo Intent (cidade selecionada)
        val lat = intent.getDoubleExtra("lat", 0.0)
        val lon = intent.getDoubleExtra("lon", 0.0)
        val name = intent.getStringExtra("name")

        if (lat != 0.0 && lon != 0.0) {
            binding.cityTxt.text = name ?: "Cidade desconhecida"
            loadWeatherData(lat, lon)
        } else {
            getLocationAndLoadWeather()
        }
    }

    // Método para preparar o menu antes de ser exibido
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.menu_logout)?.isVisible = isMenuVisible // Mostra ou oculta o menu
        return super.onPrepareOptionsMenu(menu)
    }

    private fun getLocationAndLoadWeather() {
        // Verifica se a permissão de localização foi concedida
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Solicita permissão se ainda não foi concedida
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)
            return
        }

        // Obtém a localização atual
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val lon = location.longitude

                // Carrega as informações de clima com base na localização atual
                loadWeatherData(lat, lon)
            } else {
                Toast.makeText(this, "Não foi possível obter a localização", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadWeatherData(lat: Double, lon: Double) {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            weatherViewModel.loadCurrentWeather(lat, lon, "metric").enqueue(object :
                Callback<CurrentResponseApi> {
                override fun onResponse(
                    call: Call<CurrentResponseApi>,
                    response: Response<CurrentResponseApi>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        progressBar.visibility = View.GONE
                        detailLayout.visibility = View.VISIBLE
                        data?.let {
                            cityTxt.text = it.name ?: "-"
                            statusTxt.text = it.weather?.get(0)?.main ?: "-"
                            windTxt.text = it.wind?.speed?.let { Math.round(it).toString() } + "Km/h"
                            humidityTxt.text = it.main?.humidity?.toString() + "%"
                            currentTempTxt.text = it.main?.temp?.let { Math.round(it).toString() } + "°"
                            maxTempTxt.text = it.main?.tempMax?.let { Math.round(it).toString() } + "°"
                            minTempTxt.text = it.main?.tempMin?.let { Math.round(it).toString() } + "°"

                            val drawable = if (isNightNow()) R.drawable.night_bg else {
                                setDynamicalWallpaper(it.weather?.get(0)?.icon ?: "-")
                            }
                            bgImage.setImageResource(drawable)
                            setEffectRainSnow(it.weather?.get(0)?.icon ?: "-")
                        }
                    }
                }

                override fun onFailure(call: Call<CurrentResponseApi>, t: Throwable) {
                    Toast.makeText(this@InicioActivity, t.toString(), Toast.LENGTH_SHORT).show()
                }
            })

            var radius=10f
            val decorView=window.decorView
            val rootView=(decorView.findViewById(android.R.id.content) as ViewGroup?)
            val windowBackgroud=decorView.background

            rootView?.let {
                blueView.setupWith(it, RenderScriptBlur(this@InicioActivity))
                    .setFrameClearDrawable(windowBackgroud)
                    .setBlurRadius(radius)
                blueView.outlineProvider= ViewOutlineProvider.BACKGROUND
                blueView.clipToOutline=true
            }

            weatherViewModel.loadForecastWeather(lat, lon, "metric").enqueue(object :  Callback<ForecastResponseApi>{
                override fun onResponse(
                    call: Call<ForecastResponseApi>,
                    response: Response<ForecastResponseApi>
                ) {
                    if(response.isSuccessful){
                        val data=response.body()
                        blueView.visibility=View.VISIBLE

                        data?.let {
                            forecastAdapter.differ.submitList(it.list)
                            forecastView.apply {
                                layoutManager= LinearLayoutManager(this@InicioActivity, LinearLayoutManager.HORIZONTAL, false)
                                adapter=forecastAdapter
                            }

                        }
                    }
                }

                override fun onFailure(call: Call<ForecastResponseApi>, t: Throwable) {

                }

            })
        }
    }

    private fun isNightNow(): Boolean {
        return calendar.get(Calendar.HOUR_OF_DAY) >= 18
    }

    private fun setDynamicalWallpaper(icon: String): Int {
        return when (icon.dropLast(1)) {
            "01" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.snow_bg
            }
            "02", "03", "04" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.cloudy_bg
            }
            "09", "10", "11" -> {
                initWeatherView(PrecipType.RAIN)
                R.drawable.rainy_bg
            }
            "13" -> {
                initWeatherView(PrecipType.SNOW)
                R.drawable.snow_bg
            }
            "50" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.haze_bg
            }
            else -> 0
        }
    }

    private fun setEffectRainSnow(icon: String) {
        when (icon.dropLast(1)) {
            "01" -> {
                initWeatherView(PrecipType.CLEAR)
            }
            "02", "03", "04" -> {
                initWeatherView(PrecipType.CLEAR)
            }
            "09", "10", "11" -> {
                initWeatherView(PrecipType.RAIN)
            }
            "13" -> {
                initWeatherView(PrecipType.SNOW)
            }
            "50" -> {
                initWeatherView(PrecipType.CLEAR)
            }
        }
    }

    private fun initWeatherView(type: PrecipType) {
        binding.weatherView.apply {
            setWeatherData(type)
            angle = -20
            emissionRate = 100.0f
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_default, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                // Lógica de logout aqui
                logoutUser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logoutUser() {
        // Limpar dados de sessão do SharedPreferences
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        // Fazer o logout no FirebaseAuth
        FirebaseAuth.getInstance().signOut()

        // Redireciona para a MainActivity (tela de login)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        // Finaliza a atividade atual
        finish()
    }
}
