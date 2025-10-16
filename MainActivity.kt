import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {

    private lateinit var controlSwitch: Switch
    private lateinit var gasLevelProgressBar: ProgressBar
    private lateinit var gasLevelText: TextView
    private lateinit var heatmapText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the UI components
        controlSwitch = findViewById(R.id.controlSwitch)
        gasLevelProgressBar = findViewById(R.id.gasLevelProgressBar)
        gasLevelText = findViewById(R.id.gasLevelValue)
        heatmapText = findViewById(R.id.heatmapData)

        // Set up the Control Switch functionality
        controlSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Send data to the ESP8266 to turn on/off the detection or alarm based on the switch state
            toggleControl(isChecked)
        }

        // Periodically fetch gas level data from the ESP8266
        fetchGasLevelData()

        // Optionally, you could also periodically fetch heatmap data if applicable
        fetchHeatmapData()
    }

    private fun toggleControl(isChecked: Boolean) {
        // Here, we are sending a request to the ESP8266 to toggle the control (e.g., activate LED, alarm)
        val url = "http://<your_esp8266_ip>/control?state=${if (isChecked) "on" else "off"}"
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            // Handle response here (e.g., log or show a message)
        }, { error ->
            // Handle error here (e.g., log or display a message to the user)
        })

        queue.add(stringRequest)
    }

    private fun fetchGasLevelData() {
        // Fetch gas level data from the ESP8266 (replace with your actual URL or IP)
        val url = "http://<your_esp8266_ip>/gaslevel"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            // Assuming the response contains the gas level as an integer
            val gasLevel = response.toIntOrNull() ?: 0
            updateGasLevel(gasLevel)
        }, { error ->
            // Handle any error in fetching the data (e.g., display a message to the user)
        })

        queue.add(stringRequest)
    }

    private fun fetchHeatmapData() {
        // Fetch heatmap data if needed (optional)
        val url = "http://<your_esp8266_ip>/heatmap"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            // Parse and update the heatmap data
            heatmapText.text = response // Example: you can update with actual heatmap info
        }, { error ->
            // Handle error for heatmap data
        })

        queue.add(stringRequest)
    }

    private fun updateGasLevel(level: Int) {
        // Update the progress bar and the numeric value based on the gas level
        gasLevelProgressBar.progress = level
        gasLevelText.text = "$level"
    }
}
