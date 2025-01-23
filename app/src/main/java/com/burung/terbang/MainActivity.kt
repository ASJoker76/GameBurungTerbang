package com.burung.terbang

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.burung.terbang.action.Action.moveDown
import com.burung.terbang.action.Action.moveLeft
import com.burung.terbang.action.Action.moveRight
import com.burung.terbang.ui.theme.BurungTerbangTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "menu") {
                composable("menu") {
                    MenuGame(onNavigate = { target ->
                        navController.navigate(target)
                    })
                }
                composable("game1") {
                    BurungTerbang()
                }
                composable("game2") {
                    PlaygoundActivity()
                }
                composable("game3") {
                    CingGalaSin()
                }
            }
        }
    }
}

@Composable
fun MenuGame(onNavigate: (String) -> Unit){
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center), // Menempatkan tombol di tengah
            horizontalAlignment = Alignment.CenterHorizontally, // Membuat tombol sejajar secara horizontal ,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tombol gerak kiri
            Button(
                onClick = {
                    Log.d("TAG", "MenuGame: game1")
                    onNavigate("game1")
                },
                modifier = Modifier
                    .fillMaxWidth() // Ukuran tombol // Ukuran tombol
                    .padding(end = 16.dp), // Jarak antar tombol
                content = {
                    Text(
                        text = "Game Loncat", // Teks yang ingin ditampilkan di tombol
                        style = MaterialTheme.typography.displayLarge, // Menggunakan gaya teks button
                        color = Color.White // Warna teks
                    )
                }
            )

            // Tombol gerah bawah
            Button(
                onClick = {
                    Log.d("TAG", "MenuGame: game2")
                    onNavigate("game2")
                },
                modifier = Modifier
                    .fillMaxWidth() // Ukuran tombol // Ukuran tombol
                    .padding(end = 16.dp), // Jarak antar tombol
                content = {
                    Text(
                        text = "Game Loncat dan Gerak", // Teks yang ingin ditampilkan di tombol
                        style = MaterialTheme.typography.displayLarge, // Menggunakan gaya teks button
                        color = Color.White // Warna teks
                    )
                }
            )

            // Tombol gerak kanan
            Button(
                onClick = {
                    Log.d("TAG", "MenuGame: game3")
                    onNavigate("game3")
                },
                modifier = Modifier
                    .fillMaxWidth() // Ukuran tombol
                    .padding(end = 16.dp), // Jarak antar tombol
                content = {
                    Text(
                        text = "Game Cing-Gala-Sin", // Teks yang ingin ditampilkan di tombol
                        style = MaterialTheme.typography.displayLarge, // Menggunakan gaya teks button
                        color = Color.White // Warna teks
                    )
                }
            )
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 640, // Lebar layar dalam dp
    heightDp = 360 // Tinggi layar dalam dp (lebih kecil untuk landscape)
)
@Composable
fun GreetingPreview() {
    BurungTerbangTheme {
        MenuGame(onNavigate = { /* Do nothing for preview */ })
    }
}