package com.designlife.setworkprovider

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.designlife.justdo_provider.SetworkProvider
import com.designlife.justdo_provider.data.ProviderTask

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Setwork Provider")
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {
                    SetworkProvider(this@MainActivity).addTask(ProviderTask(
                        title = "Widget Task",
                        description = "Description of widget task : lorem ipsum few lines here and there",
                        color = Color.Yellow,
                        time = System.currentTimeMillis()
                    ))
                }) {
                    Text("Task +")
                }

            }
        }
    }
}