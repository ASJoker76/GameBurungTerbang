package com.burung.terbang

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.burung.terbang.action.Action.moveDown
import com.burung.terbang.action.Action.moveLeft
import com.burung.terbang.action.Action.moveRight
import com.burung.terbang.action.Action.moveUp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CingGalaSin() {
    // Main Character
    var charY by remember { mutableStateOf(0f) }
    var charX by remember { mutableStateOf(0f) } // Posisi X karakter
    val coroutineScope = rememberCoroutineScope()
    var char by remember { mutableStateOf("idle") } // Ganti dengan URL atau nama file lokal Anda
    var currentRunFrame by remember { mutableStateOf(0) } // Frame animasi saat ini
    var isMoving by remember { mutableStateOf(false) } // Apakah karakter sedang bergerak

    // Daftar frame animasi berjalan
    val runFrames = listOf(
        R.drawable.run_00,
        R.drawable.run_01,
        R.drawable.run_02,
        R.drawable.run_03,
        R.drawable.run_04,
        R.drawable.run_05,
        R.drawable.run_06,
        R.drawable.run_07,
        R.drawable.run_08,
        R.drawable.run_09,
        R.drawable.run_10,
        R.drawable.run_11,
        R.drawable.run_12
    )

    // Jalankan animasi ketika bergerak
    LaunchedEffect(isMoving) {
        while (isMoving) {
            currentRunFrame = (currentRunFrame + 1) % runFrames.size
            delay(100L) // Ganti frame setiap 100ms
        }
    }


    // Mendapatkan informasi tentang dimensi layar
    val density = LocalDensity.current
    val screenHeight = LocalConfiguration.current.screenHeightDp // Dalam dp

    // Karakter penjaga
    var guardY by remember { mutableStateOf(0f) } // Posisi Y penjaga
    val guardSpeed = 5f // Kecepatan gerak penjaga
    var guardDirection by remember { mutableStateOf(1) } // 1 untuk ke bawah,
    // Update posisi penjaga secara vertikal
    LaunchedEffect(Unit) {
        while (true) {
            // Konversi screenHeight dari dp ke px
            val screenHeightPx = with(density) { screenHeight.dp.toPx() }
            // Update posisi penjaga berdasarkan arah
            guardY += guardSpeed * guardDirection

            // Jika mencapai batas bawah atau atas, ubah arah secara acak
            if (guardY >= screenHeightPx || guardY <= 0f) {
                guardDirection = if ((0..1).random() == 0) 1 else -1
            }

            delay(3L)
        }
    }

    // Ukuran penjaga dan karakter
    val characterSize = 150f
    val guardSize = 100f

    // Deteksi tabrakan
    var gameOver by remember { mutableStateOf(false) }
    LaunchedEffect(charX, charY, guardY) {
        gameOver = charX + characterSize > 0 &&
                charX < guardSize &&
                charY + characterSize > guardY &&
                charY < guardY + guardSize
    }

    // Layout Interface
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Garis horizontal
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.Center),
            color = Color.Black
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.TopCenter),
            color = Color.Black
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.BottomCenter),
            color = Color.Black
        )
        // Garis vertikal
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp)
                .align(Alignment.Center),
            color = Color.Black
        )
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp)
                .align(Alignment.TopStart),
            color = Color.Black
        )

        val charImage = when (char) {
            "idle" -> R.drawable.skeleton_idle
            "ko" -> R.drawable.skeleton_ko
            "run" -> runFrames[currentRunFrame]
            else -> R.drawable.skeleton_idle
        }

        Image(
            painter = painterResource(id = charImage),
            contentDescription = "Char",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomStart) // Pojok kiri bawah
                .offset(x = charX.dp, y = (-charY).dp) // Sedikit jarak dari tepi bawah dan kiri
        )

        // Gambar penjaga
        val guardImage = R.drawable.ghost_normal // Ganti dengan gambar penjaga yang sesuai
        Image(
            painter = painterResource(id = guardImage),
            contentDescription = "Guard",
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.TopCenter) // Penjaga berada di tengah
                .offset(x = 0.dp, y = guardY.dp)
        )

        // Tampilkan pesan game over
        if (gameOver) {
            Text(
                text = "Game Over!",
                color = Color.Red,
                fontSize = 30.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // joystik
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp), // Memberikan jarak dari bawah
            verticalArrangement = Arrangement.Center, // Menempatkan tombol berdampingan
            horizontalAlignment = Alignment.CenterHorizontally // Menempatkan konten di tengah horizontal
        ) {
            // Tombol geraj atas
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        isMoving = true
                        char = "run" // Set state ke run saat bergerak
                        moveUp(
                            updatePosition = { newY -> charY = newY }, // Update posisi Y
                            onFinish = {
                                char = "idle"
                                isMoving = false
                            },
                            startingPosition = charY // Posisi awal Y
                        )
                    }
                },
                modifier = Modifier
                    .size(70.dp) // Ukuran tombol
                    .padding(end = 16.dp), // Jarak antar tombol
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_top), // Ikon panah kiri
                        contentDescription = "Move Top"
                    )
                }
            )

            Row(
                modifier = Modifier
                    .padding(16.dp), // Memberikan jarak dari bawah
                horizontalArrangement = Arrangement.Center // Menempatkan tombol berdampingan
            ) {
                // Tombol gerak kiri
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            isMoving = true
                            char = "run" // Set state ke run saat bergerak
                            moveLeft(
                                updatePosition = { newX -> charX = newX },
                                onFinish = {
                                    char = "idle"
                                    isMoving = false // Reset isJumping setelah loncatan selesai
                                },
                                startingPosition = charX
                            )
                        }
                    },
                    modifier = Modifier
                        .size(70.dp) // Ukuran tombol
                        .padding(end = 16.dp), // Jarak antar tombol
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_left), // Ikon panah kiri
                            contentDescription = "Move Left"
                        )
                    }
                )

                // Tombol gerah bawah
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            isMoving = true
                            char = "run" // Set state ke run saat bergerak
                            moveDown(
                                updatePosition = { newY -> charY = newY }, // Update posisi Y
                                onFinish = {
                                    char = "idle"
                                    isMoving = false
                                },
                                startingPosition = charY // Posisi awal Y
                            )
                        }
                    },
                    modifier = Modifier
                        .size(70.dp) // Ukuran tombol
                        .padding(end = 16.dp), // Jarak antar tombol
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bottom), // Ikon panah kiri
                            contentDescription = "Move Down"
                        )
                    }
                )

                // Tombol gerak kanan
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            isMoving = true
                            char = "run" // Set state ke run saat bergerak
                            moveRight(
                                updatePosition = { newX -> charX = newX },
                                onFinish = {
                                    char = "idle"
                                    isMoving = false // Reset isJumping setelah loncatan selesai
                                },
                                startingPosition = charX
                            )
                        }
                    },
                    modifier = Modifier
                        .size(70.dp) // Ukuran tombol
                        .padding(end = 16.dp), // Jarak antar tombol
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_right), // Ikon panah kanan
                            contentDescription = "Move Right"
                        )
                    }
                )
            }
        }
    }
}