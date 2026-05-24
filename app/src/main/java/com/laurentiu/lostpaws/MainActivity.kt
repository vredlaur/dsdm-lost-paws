package com.laurentiu.lostpaws

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.laurentiu.lostpaws.navigation.LostPawsNavGraph
import com.laurentiu.lostpaws.ui.theme.LostPawsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LostPawsTheme {
                LostPawsNavGraph()
            }
        }
    }
}
