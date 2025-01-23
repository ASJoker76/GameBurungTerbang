package com.burung.terbang

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.burung.terbang.action.Action.jumpAnimation
import com.burung.terbang.action.Action.moveLeft
import com.burung.terbang.action.Action.moveRight
import com.burung.terbang.action.Action.shoot
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PlaygoundActivity() {
    // Main Character
    var charY by remember { mutableStateOf(0f) }
    var charX by remember { mutableStateOf(0f) } // Posisi X karakter
    val coroutineScope = rememberCoroutineScope()
    var char by remember { mutableStateOf("idle") } // Ganti dengan URL atau nama file lokal Anda
    var isJumping by remember { mutableStateOf(false) } // Menandakan apakah karakter sedang melompat
    var jumpCount by remember { mutableStateOf(0) } // Menghitung jumlah loncatan berturut-turut
    var currentJumpFrame by remember { mutableStateOf(0) } // Frame animasi saat ini
    var currentRunFrame by remember { mutableStateOf(0) } // Frame animasi saat ini
    var isMoving by remember { mutableStateOf(false) } // Apakah karakter sedang bergerak
    var isShoot by remember { mutableStateOf(false) } // Apakah karakter sedang bergerak
    var charProjectiles by remember { mutableStateOf(listOf<Pair<Float, Float>>()) } // Posisi peluru karakter

    // Musuh
    var enemyX by remember { mutableStateOf(0f) } // Posisi X musuh
    var enemyY by remember { mutableStateOf(0f) } // Posisi Y musuh
    var enemyHitCount by remember { mutableStateOf(0) } // Jumlah peluru yang mengenai musuh
    var enemyState by remember { mutableStateOf("normal") } // Status musuh: "normal" atau "ko"


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

    val jumpFrames = listOf(
        R.drawable.jump_00,
        R.drawable.jump_01,
        R.drawable.jump_02,
        R.drawable.jump_03,
        R.drawable.jump_04,
        R.drawable.jump_05,
        R.drawable.jump_06,
        R.drawable.jump_07,
        R.drawable.jump_08,
        R.drawable.jump_09,
        R.drawable.jump_10
    )

    // Jalankan animasi ketika bergerak
    LaunchedEffect(isMoving, isJumping, isShoot) {
        while (isMoving) {
            currentRunFrame = (currentRunFrame + 1) % runFrames.size
            delay(100L) // Ganti frame setiap 100ms
        }

        while (isJumping){
            currentJumpFrame = (currentJumpFrame + 1) % jumpFrames.size
            delay(100L) // Ganti frame setiap 100ms
        }

        while (isShoot){
            charProjectiles = charProjectiles.map { (projX, projY) ->
                Pair(projX + 10f, projY) // Tambahkan ke posisi X untuk gerakan peluru
            }.filter { (projX, _) ->
                projX < 1000f // Hapus peluru jika keluar dari layar
            }
            delay(16L) // Update setiap 16ms (60 FPS)
        }
    }

    // Gambar peluru karakter
    charProjectiles.forEach { (projX, projY) ->
        Image(
            painter = painterResource(id = R.drawable.slime_hit), // Gambar peluru
            contentDescription = "Projectile",
            modifier = Modifier
                .size(100.dp)
                .offset(x = projX.dp, y = projY.dp)
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Gambar bulan dan awan
        Image(
            painter = painterResource(id = R.drawable.bg_mountain_moon_cloud),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Atur scaling agar sesuai dengan layar
        )

        // Gambar Gunung
        Image(
            painter = painterResource(id = R.drawable.bg_mountain),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Atur scaling agar sesuai dengan layar
        )

        // Gambar Gunung Kecil
        Image(
            painter = painterResource(id = R.drawable.bg_mountain_small),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Atur scaling agar sesuai dengan layar
        )

        // Gambar Pohon Jauh
        Image(
            painter = painterResource(id = R.drawable.bg_trees_far),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Atur scaling agar sesuai dengan layar
        )

        // Gambar Pohon Kecil
        Image(
            painter = painterResource(id = R.drawable.bg_trees_small),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Atur scaling agar sesuai dengan layar
        )

        // Gambar burung berdasarkan state
        val charImage = when (char) {
            "idle" -> R.drawable.skeleton_idle
            "jump" -> jumpFrames[currentJumpFrame]
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

        // Gambar peluru karakter
        charProjectiles.forEach { (projX, projY) ->
            Image(
                painter = painterResource(id = R.drawable.slime_hit), // Ganti dengan gambar peluru
                contentDescription = "Projectile",
                modifier = Modifier
                    .size(20.dp)
                    .offset(x = projX.dp, y = projY.dp)
            )
        }

        // Gambar musuh
        val enemyImage = when (enemyState) {
            "normal" -> R.drawable.ghost_normal // Gambar musuh normal
            "ko" -> R.drawable.ghost_dead // Gambar musuh KO
            else -> R.drawable.ghost_normal
        }

        Image(
            painter = painterResource(id = enemyImage),
            contentDescription = "Enemy",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomEnd)
                .offset(x = enemyX.dp, y = enemyY.dp)
        )


        // Tombol bulat untuk melompat
        FloatingActionButton(
            onClick = {
                if (jumpCount < 2 && !isJumping) {  // Pastikan karakter bisa loncat hanya 2 kali
                    coroutineScope.launch {
                        isJumping = true // Set isJumping ke true saat mulai melompat
                        char = "jump"
                        jumpAnimation(
                            startingHeight = charY, // Mulai dari posisi Y terakhir
                            updatePosition = { newY -> charY = newY },
                            onFinish = { finished ->
                                if (finished) {
                                    jumpCount += 1 // Increment loncatan
                                    if (jumpCount == 2) {
                                        // Setelah 2x lompat, reset
                                        jumpCount = 0
                                    }
                                    char = "idle"
                                    isJumping = false // Reset isJumping setelah loncatan selesai
                                }
                            }
                        )
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd) // Posisi di pojok kanan bawah
                .padding(16.dp), // Memberikan jarak dari tepi kanan dan bawah
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_jump), // Ganti dengan ikon lompatan
                    contentDescription = "Jump Button"
                )
            }
        )

        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    isShoot = true
                    shoot(
                        startX = charX + 50f, // Posisi awal peluru (sedikit di depan karakter)
                        startY = charY, // Posisi Y sama dengan karakter
                        updateProjectilePosition = { newX, newY ->
                            charProjectiles = charProjectiles + Pair(newX, newY) // Tambahkan peluru ke daftar
                        },
                        onHit = {
                            enemyHitCount += 1 // Tambahkan hit count
                            if (enemyHitCount >= 5) {
                                enemyState = "ko" // Ubah status musuh menjadi KO jika terkena 5x
                            }
                        },
                        targetX = enemyX, // Posisi X target (musuh)
                        speed = 20f // Kecepatan peluru
                    )
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter) // Posisi tombol di tengah bawah
                .padding(16.dp),
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_shoot), // Ikon peluru
                    contentDescription = "Shoot Button"
                )
            }
        )


        // Baris untuk tombol kiri dan kanan
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
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
                    .padding(end = 16.dp) // Jarak antar tombol
                    .alpha(0.7f), // Transparansi tombol
                containerColor = Color(0xFF000000).copy(alpha = 0.3f), // Latar belakang tombol transparan
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_left), // Ikon panah kiri
                        contentDescription = "Move Left"
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
                    .alpha(0.7f), // Transparansi tombol
                containerColor = Color(0xFF000000).copy(alpha = 0.3f), // Latar belakang transparan
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