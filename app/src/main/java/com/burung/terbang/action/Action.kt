package com.burung.terbang.action

import kotlinx.coroutines.delay

object Action {

    suspend fun moveLeft(
        updatePosition: (Float) -> Unit,
        onFinish: () -> Unit,
        startingPosition: Float = 0f
    ) {
        val moveDistance = 50f // Jarak pergerakan ke kiri
        val moveDuration = 300 // Durasi pergerakan (ms)
        val frameRate = 60 // 60 FPS
        val frameDuration = 1000 / frameRate

        val step = moveDistance / (moveDuration / frameDuration) // Perubahan posisi per frame
        var currentX = startingPosition

        // Gerakan ke kiri
        while (currentX > startingPosition - moveDistance) {
            currentX -= step
            updatePosition(currentX)
            delay(frameDuration.toLong())
        }

        updatePosition(startingPosition - moveDistance) // Posisi akhir
        onFinish() // Callback ketika pergerakan selesai
    }

    suspend fun moveRight(
        updatePosition: (Float) -> Unit,
        onFinish: () -> Unit,
        startingPosition: Float = 0f
    ) {
        val moveDistance = 50f // Jarak pergerakan ke kanan
        val moveDuration = 300 // Durasi pergerakan (ms)
        val frameRate = 60 // 60 FPS
        val frameDuration = 1000 / frameRate

        val step = moveDistance / (moveDuration / frameDuration) // Perubahan posisi per frame
        var currentX = startingPosition

        // Gerakan ke kanan
        while (currentX < startingPosition + moveDistance) {
            currentX += step
            updatePosition(currentX)
            delay(frameDuration.toLong())
        }

        updatePosition(startingPosition + moveDistance) // Posisi akhir
        onFinish() // Callback ketika pergerakan selesai
    }

    suspend fun moveDown(
        updatePosition: (Float) -> Unit,
        onFinish: () -> Unit,
        startingPosition: Float = 0f
    ) {
        val moveDistance = 50f // Jarak pergerakan ke atas
        val moveDuration = 300 // Durasi pergerakan (ms)
        val frameRate = 60 // 60 FPS
        val frameDuration = 1000 / frameRate

        val step = moveDistance / (moveDuration / frameDuration) // Perubahan posisi per frame
        var currentY = startingPosition

        // Gerakan ke atas (koordinat Y berkurang)
        while (currentY > startingPosition - moveDistance) {
            currentY -= step
            updatePosition(currentY)
            delay(frameDuration.toLong())
        }

        updatePosition(startingPosition - moveDistance) // Posisi akhir
        onFinish() // Callback ketika pergerakan selesai
    }

    suspend fun moveUp(
        updatePosition: (Float) -> Unit,
        onFinish: () -> Unit,
        startingPosition: Float = 0f
    ) {
        val moveDistance = 50f // Jarak pergerakan ke bawah
        val moveDuration = 300 // Durasi pergerakan (ms)
        val frameRate = 60 // 60 FPS
        val frameDuration = 1000 / frameRate

        val step = moveDistance / (moveDuration / frameDuration) // Perubahan posisi per frame
        var currentY = startingPosition

        // Gerakan ke bawah (koordinat Y bertambah)
        while (currentY < startingPosition + moveDistance) {
            currentY += step
            updatePosition(currentY)
            delay(frameDuration.toLong())
        }

        updatePosition(startingPosition + moveDistance) // Posisi akhir
        onFinish() // Callback ketika pergerakan selesai
    }


    suspend fun jumpAnimation(
        updatePosition: (Float) -> Unit,
        onFinish: (Boolean) -> Unit,
        startingHeight: Float = 0f // Tambahkan parameter untuk menerima posisi awal
    ) {
        val baseJumpHeight = 150f // Tinggi dasar lompatan
        val jumpMultiplier = if (startingHeight > 0f) 1.5f else 1f // Jika sudah melompat, buat loncatan kedua lebih tinggi
        val jumpHeight = baseJumpHeight * jumpMultiplier + startingHeight // Variasi tinggi loncatan
        val upDuration = 300 // Durasi naik (ms)
        val downDuration = 300 // Durasi turun (ms)
        val frameRate = 60 // 60 FPS
        val frameDuration = 1000 / frameRate

        val upStep = jumpHeight / (upDuration / frameDuration) // Perubahan posisi naik per frame
        val downStep = jumpHeight / (downDuration / frameDuration) // Perubahan posisi turun per frame

        var currentY = startingHeight // Mulai dari posisi yang diterima (startingHeight)

        // Naik
        while (currentY < startingHeight + jumpHeight) {
            currentY += upStep
            updatePosition(currentY)
            delay(frameDuration.toLong())
        }

        // Turun
        while (currentY > startingHeight) {
            currentY -= downStep
            updatePosition(currentY)
            delay(frameDuration.toLong())
        }

        updatePosition(startingHeight) // Kembalikan ke posisi semula
        onFinish(true) // Callback ketika animasi selesai
    }

    suspend fun shoot(
        startX: Float, // Posisi X awal peluru
        startY: Float, // Posisi Y awal peluru
        updateProjectilePosition: (Float, Float) -> Unit, // Callback untuk memperbarui posisi peluru
        onHit: () -> Unit, // Callback saat peluru mengenai musuh
        targetX: Float, // Posisi X target (musuh)
        speed: Float = 10f // Kecepatan peluru
    ) {
        var currentX = startX
        var currentY = startY

        // Animasi peluru menuju target secara horizontal
        while (currentX < targetX) {
            currentX += speed // Peluru bergerak ke kanan
            updateProjectilePosition(currentX, currentY)

            if (currentX >= targetX) { // Jika peluru mencapai target
                onHit()
                break
            }

            delay(16L) // 16ms untuk simulasi 60 FPS
        }
    }
}