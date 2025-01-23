package com.burung.terbang

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun BurungTerbang() {

    var birdY by remember { mutableStateOf(0f) }
    var birdVelocity by remember { mutableStateOf(0f) }
    var pipes by remember { mutableStateOf(listOf(Pipe(600f, 300f))) }
    var isGameOver by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var birdState by remember { mutableStateOf("idle") } // Ganti dengan URL atau nama file lokal Anda

    LaunchedEffect(Unit) {
        while (true) {
            if (!isGameOver) {
                // Gravitasi
                birdVelocity += 1f
                birdY += birdVelocity

                // Update posisi pipa
                pipes = pipes.map { it.copy(x = it.x - 8) }
                    .filter { it.x > -100 } // Hapus pipa yang keluar dari layar

                // Tambahkan pipa baru
                if (pipes.isEmpty() || pipes.last().x < 400f) {
                    pipes = pipes + Pipe(
                        x = 800f,
                        gapY = Random.nextInt(300, 500).toFloat()
                    )
                    score++
                }

                // Cek tabrakan
                if (birdY > 800 || birdY < 0 || pipes.any { it.collidesWith(birdY) }) {
                    isGameOver = true
                    birdState = "ko" // Ubah state ke KO jika tabrakan
                }

                delay(16L) // 60 FPS
            } else {
                delay(100L) // Tunggu sebentar saat game over
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(isGameOver) {
                detectTapGestures {
                    if (isGameOver) {
                        // Reset game setelah game over
                        birdY = 400f
                        birdVelocity = 0f
                        pipes = listOf(Pipe(600f, 300f))
                        isGameOver = false
                        score = 0
                        birdState = "idle" // Reset ke idle
                    } else {
                        birdVelocity = -15f // Naik ketika disentuh
                        birdState = "jump" // Ubah state ke jump
                    }
                }
            }
    ) {
        // Gambar latar belakang
        Image(
            painter = painterResource(id = R.drawable.bg_mountain_moon_cloud),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Atur scaling agar sesuai dengan layar
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            // Gambar burung
//            drawCircle(
//                color = Color.Yellow,
//                radius = 40f,
//                center = androidx.compose.ui.geometry.Offset(x = 200f, y = birdY)
//            )

            // Gambar pipa
            pipes.forEach { pipe ->
                drawRoundRect(
                    color = Color(0xFFD47984),
                    topLeft = androidx.compose.ui.geometry.Offset(pipe.x, 0f),
                    size = androidx.compose.ui.geometry.Size(100f, pipe.gapY - 150)
                )
                drawRoundRect(
                    color = Color(0xFFD47984),
                    topLeft = androidx.compose.ui.geometry.Offset(pipe.x, pipe.gapY + 150),
                    size = androidx.compose.ui.geometry.Size(100f, size.height - pipe.gapY - 150)
                )
            }
        }

        // Gambar burung berdasarkan state
        val birdImage = when (birdState) {
            "idle" -> R.drawable.skeleton_idle
            "jump" -> R.drawable.skeleton_jump
            "ko" -> R.drawable.skeleton_ko
            else -> R.drawable.skeleton_idle
        }

        Image(
            painter = painterResource(id = birdImage),
            contentDescription = "Bird",
            modifier = Modifier
                .size(80.dp)
                .offset { IntOffset(200, birdY.toInt()) } // Posisi burung
        )

        // Tampilkan skor
        BasicText(
            text = "Score: $score",
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.TopCenter)
                .padding(16.dp),
            style = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 24.sp)
        )

        // Game Over
        if (isGameOver) {
            BasicText(
                text = "Game Over! Tap to Restart",
                modifier = Modifier.align(androidx.compose.ui.Alignment.Center),
                style = androidx.compose.ui.text.TextStyle(color = Color.Red, fontSize = 32.sp)
            )
        }
    }
}

data class Pipe(val x: Float, val gapY: Float) {
    fun collidesWith(birdY: Float): Boolean {
        return x in 100f..300f && (birdY < gapY - 150 || birdY > gapY + 150)
    }
}