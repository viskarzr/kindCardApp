package com.example.kindcardapp
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import kotlin.random.Random

class NotificationHelper(private val context: Context) {

    // Companion object — параметры, общие для всех экземпляров
    companion object {
        const val CHANNEL_ID = "general_channel"           // ID обычного канала
        const val HEADS_UP_CHANNEL_ID = "heads_up_channel" // ID канала для Heads-up
        var NOTIFICATION_ID = 1001                   // Уникальный ID уведомления
    }

    // Метод для создания каналов уведомлений (только для Android 8+)
    fun createChannels() {
        // Проверяем, что версия Android >= Oreo (API 26)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Создаём обычный канал с важностью DEFAULT
            val generalChannel = NotificationChannel(
                CHANNEL_ID,                                    // ID канала
                "Общий канал",                                 // Название для пользователя
                NotificationManager.IMPORTANCE_DEFAULT         // Уровень важности
            ).apply {
                description = "Все обычные уведомления"        // Описание канала (видно в настройках)
                enableVibration(true)                          // Включаем вибрацию
            }

            // Создаём канал для Heads-up с важностью HIGH (обязательно для всплывающих)
            val headsUpChannel = NotificationChannel(
                HEADS_UP_CHANNEL_ID,
                "Важные уведомления",
                NotificationManager.IMPORTANCE_HIGH            // HIGH — для Heads-up
            ).apply {
                description = "Уведомления, которые появляются поверх приложения"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500) // Вибрация: пауза, 500 мс, пауза,    500 мс
                        enableLights(true)                             // Включаем светодиод
                lightColor = Color.RED                         // Цвет светодиода
            }

            // Получаем системный менеджер уведомлений
            val manager = context.getSystemService(NotificationManager::class.java)
            // Регистрируем каналы в системе
            manager?.createNotificationChannel(generalChannel)
            manager?.createNotificationChannel(headsUpChannel)
        }
    }

    // Метод для показа Heads-up уведомления
    fun showHeadsUpNotification(title: String, message: String) {

        // Создаём Intent для перехода в MainActivity при нажатии на уведомление
        val intent = Intent(context, MainActivity::class.java).apply {
            // Устанавливаем флаги: создаём новую задачу и очищаем стек
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // Создаём PendingIntent для открытия Activity по нажатию
        val pendingIntent = PendingIntent.getActivity(
            context,                                          // Контекст
            0,                                                // Код запроса (уникальный для разных Intent)
            intent,                                           // Intent
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    // FLAG_UPDATE_CURRENT — обновляет существующий PendingIntent,  FLAG_IMMUTABLE — неизменяемый
        )

        // Создаём FullScreenIntent — для принудительного показа Heads-up поверх активного приложения
        val fullScreenIntent = PendingIntent.getActivity(
            context,
            1,                                                // Другой код запроса
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // Строим уведомление с помощью NotificationCompat.Builder
        val builder = NotificationCompat.Builder(context, HEADS_UP_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)  // Иконка в статус-баре   (обязательно!)
        .setContentTitle(title)                           // Заголовок
            .setContentText(message)                          // Текст уведомления
            .setPriority(NotificationCompat.PRIORITY_HIGH)    // Приоритет для старых версий
            .setContentIntent(pendingIntent)                  // Действие при нажатии на уведомление
            .setFullScreenIntent(fullScreenIntent, true)      // Принудительный Heads-up (true —   уведомление исчезнет после)
        .setAutoCancel(true)                              // Закрывается при нажатии
            // .setSound(...) — закомментировано, т.к. файл отсутствует
            .setVibrate(longArrayOf(0, 500, 200, 500))        // Вибрация
            .addAction(                                       // Добавляем кнопку действия
                R.drawable.ic_launcher_foreground,            // Иконка кнопки
                "Открыть",                                    // Текст кнопки
                pendingIntent                                 // PendingIntent для кнопки
            )

        //Рандомим id уведомления
        NOTIFICATION_ID = Random.nextInt()
        // Проверяем разрешение POST_NOTIFICATIONS для Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
                // Если разрешение есть — отправляем уведомление
                NotificationManagerCompat.from(context).notify(NOTIFICATION_ID,
                    builder.build())
            } else {
                // Если разрешения нет — уведомление не отправляется (можно запросить или сохранить)
            }
        } else {
            // Для версий ниже Android 13 разрешение не требуется
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
        }

    }

}