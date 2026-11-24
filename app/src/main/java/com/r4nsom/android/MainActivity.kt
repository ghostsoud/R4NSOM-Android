package com.r4nsom.android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.graphics.Color
import android.view.Gravity

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(12, 12, 16)
        window.navigationBarColor = Color.rgb(12, 12, 16)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 40, 32, 40)
            setBackgroundColor(Color.rgb(12, 12, 16))
        }
        val title = TextView(this).apply {
            text = "R4NSOM"
            textSize = 34f
            setTextColor(Color.rgb(235, 40, 48))
            gravity = Gravity.CENTER
        }
        val info = TextView(this).apply {
            text = "Найди 5 монет, спрятанных поверх экранов телефона.\n\nДля игрового режима нужно разрешить отображение поверх других приложений. Приложение не блокирует телефон и не изменяет личные файлы."
            textSize = 16f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 28, 0, 28)
        }
        val start = Button(this).apply {
            text = "НАЧАТЬ ИГРУ"
            setOnClickListener { beginGame() }
        }
        root.addView(title)
        root.addView(info)
        root.addView(start)
        setContentView(root)
    }

    private fun beginGame() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivity(intent)
            return
        }
        val service = Intent(this, CoinOverlayService::class.java)
        if (Build.VERSION.SDK_INT >= 26) startForegroundService(service) else startService(service)
        finish()
    }

    override fun onResume() {
        super.onResume()
    }
}
