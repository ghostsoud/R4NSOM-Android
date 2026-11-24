package com.r4nsom.android

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class CoinOverlayService : Service() {
    private lateinit var wm: WindowManager
    private val views = mutableListOf<TextView>()
    private var found = 0
    private val total = 5

    override fun onCreate() {
        super.onCreate()
        createNotification()
        startForeground(41, notification())
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        placeCoins()
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel("r4nsom_game", "R4NSOM game", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun notification(): Notification {
        val builder = if (Build.VERSION.SDK_INT >= 26) Notification.Builder(this, "r4nsom_game") else Notification.Builder(this)
        return builder.setContentTitle("R4NSOM запущен")
            .setContentText("Игровые монеты активны. Нажми, чтобы вернуться в игру.")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .build()
    }

    private fun placeCoins() {
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        val occupied = mutableListOf<Pair<Int, Int>>()
        repeat(total) { index ->
            var x: Int
            var y: Int
            var attempts = 0
            do {
                x = Random.nextInt(28, (width - 90).coerceAtLeast(29))
                y = Random.nextInt(100, (height - 180).coerceAtLeast(101))
                attempts++
            } while (occupied.any { (ox, oy) -> kotlin.math.abs(ox - x) < 100 && kotlin.math.abs(oy - y) < 100 } && attempts < 40)
            occupied.add(x to y)

            val coin = TextView(this).apply {
                text = "🪙"
                textSize = 34f
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                setShadowLayer(12f, 0f, 0f, Color.YELLOW)
                setOnClickListener {
                    if (parent != null) wm.removeView(this)
                    views.remove(this)
                    found++
                    Toast.makeText(this@CoinOverlayService, "Монета найдена: $found/$total", Toast.LENGTH_SHORT).show()
                    if (found == total) {
                        Toast.makeText(this@CoinOverlayService, "Все 5 монет найдены. Финал разблокирован!", Toast.LENGTH_LONG).show()
                        stopSelf()
                    }
                }
            }
            val type = if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE
            val params = WindowManager.LayoutParams(
                64, 64, type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.START
                x = x
                y = y
            }
            wm.addView(coin, params)
            views.add(coin)
        }
    }

    override fun onDestroy() {
        views.forEach { try { wm.removeView(it) } catch (_: Exception) { } }
        views.clear()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
