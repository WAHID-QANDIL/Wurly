package com.wahid.wurly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mrtdk.glass.GlassBox
import com.mrtdk.glass.GlassContainer
import com.wahid.wurly.ui.theme.WurlyTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WurlyTheme {
                GlassContainer(
                    content = {
                        Image(
                            painter = painterResource(R.drawable.image),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                    }
                ) {


                    GlassBox(
                        modifier = Modifier
                            .padding(50.dp)
                            .fillMaxSize()
                            .align(Alignment.BottomStart)
                            .wrapContentSize()
                            .padding(50.dp),
                        contentAlignment = Alignment.Center,
                        scale = .9f,
                        shape = RoundedCornerShape(16.dp),
                        darkness = .4f,
                        ) {
                        LazyColumn() {
                            repeat(10){
                                item {
                                    Text("Hello from text", Modifier.padding(30.dp))
                                }
                            }
                        }
                    }


                }

            }

        }

    }

}