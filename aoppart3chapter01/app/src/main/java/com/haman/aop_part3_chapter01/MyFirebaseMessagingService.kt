package com.haman.aop_part3_chapter01

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationBuilderWithBuilderAccessor
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 *                  foreground          background
 * 알림 메세지 :     onMessageReceived       작업 표시줄
 * 데이터 메세지 :    onMessageReceived   onMessageReceived
 * 모든 메세지는 수신된 지 20초(마시멜로는 10초) 이내의 반응을 보여주어야 합니다.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * 새 기기에서 앱 복원
     * 사용자가 앱 삭제/재설치
     * 사용자가 앱 데이터 소거
     * 등 여러 가지 이유로 토큰이 재발급 되는 경우가 있다.
     * -> 실제 라이브 서비스를 운영할 때는 onNewToken 이 호출될 때마다(새로 발급될 때마다)
     * 새로운 token 을 프로젝트 서버에 업데이트 해준다.
     */
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    /**
     * 해당 어플리케이션의 토큰 번호로 메세지가 보내지면,
     * onMessageReceived 메소드가 받는다.
     */
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        createNotificationChannel()

        val type = message.data["type"]
            ?.let {
                // ex. type = "Expandable" 이면 NotificationType 의 EXPANDABLE 로 바인딩
                NotificationType.valueOf(it)
            }
        val title = message.data["title"]
        val content = message.data["message"]

        type ?: return // NotificationType enum 과 일치하는 타입이 없는 경우

        // 알림 생성 (Builder 패턴)
        NotificationManagerCompat.from(this)
            .notify(type.id, createNotification(title,content,type))
    }
    /**
     * debug : stop 찍어두고, Run > Attach Debugger to Android Process
     */
    /**
     * 테스트 메세지 보내기
     * firebase > documents > references > REST > Cloud Messaging > Send > Try this Message
     */

    /**
     * 알림 기능은 보안상의 문제로 빈번하게 업데이트가 이루어진 다는 점 주의하자! developer documents 참조
     * 8.0 부터는 알림 생성 전에 채널을 생성하고, 알림을 채널에 포함시켜야 합니다.
     * 또한 채널별로 중요도를 명시하여, 중요도에 따른 행동은 제한할 수 있습니다.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 8.0(오레오) 이상이여야 한다.
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.description = CHANNEL_DESCRIPTION
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
            // app info 에 들어가면 channel 이 생성된 것을 알 수 있다.
        }
    }

    private fun createNotification(
        title: String?,
        content: String?,
        type: NotificationType
    ) : Notification {

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("notificationType", "${type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            /**
             * 기존의 activity 처리 형식은 기본적인 stack 자료구조이다.
             * ex. A > A,B > A,B,B(onCreate)
             * single top 으로 flag 값을 주면 동일한 activity 는 stack 자료 구조에 하나만 존재하며,
             * 새로 호출될 때는 onCreate 가 아닌, onNewIntent 가 호출된다.
             * ex. A > A,B > A,B(onNewIntent)
             */
        }

        /**
         * pending intent : 내 앱이 직접 intent 를 다루는 것이 아닌, 다른 앱이나 운영체제에서 intent 를 실행시키고자 할 때 사용
         * pending intent 를 생성할 때 전달해둔 값이 같을 경우에는 하나의 pending intent 를 사용한다.
         * 각 id 별로 다른 pending intent 를 생성하고 싶다면 requestCode( 여기서는 type.id )를 다르게 전달하면 된다.
         */
        val pendingIntent = PendingIntent.getActivity(this, type.id, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // 8.0 미만
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        when (type) {
            NotificationType.NORMAL -> Unit // 아무것도 수행하지 않을 떄
            NotificationType.EXPANDABLE -> {
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                            "\uD83D\uDE00 \uD83D\uDE03 \uD83D\uDE04 \uD83D\uDE01 \uD83D\uDE06 " +
                                    "\uD83D\uDE05 \uD83D\uDE02 \uD83E\uDD23 \uD83E\uDD72 ☺️ \uD83D\uDE0A " +
                                    "\uD83D\uDE07 \uD83D\uDE42 \uD83D\uDE43 \uD83D\uDE09 \uD83D\uDE0C " +
                                    "\uD83D\uDE0D \uD83E\uDD70 \uD83D\uDE18 \uD83D\uDE17 \uD83D\uDE19 " +
                                    "\uD83D\uDE1A \uD83D\uDE0B \uD83D\uDE1B \uD83D\uDE1D \uD83D\uDE1C " +
                                    "\uD83E\uDD2A \uD83E\uDD28 \uD83E\uDDD0 \uD83E\uDD13 \uD83D\uDE0E " +
                                    "\uD83E\uDD78 \uD83E\uDD29 \uD83E\uDD73 \uD83D\uDE0F \uD83D\uDE12 " +
                                    "\uD83D\uDE1E \uD83D\uDE14 \uD83D\uDE1F \uD83D\uDE15 \uD83D\uDE41 ☹️ " +
                                    "\uD83D\uDE23 \uD83D\uDE16 \uD83D\uDE2B"
                        )
                )
            }
            NotificationType.CUSTOM -> {
                /** remoteView : 나의 앱이 다른 다른 앱 또는 운영체제에서 관리하는 View 를 구현할 때 주로 사용한다.
                 * CustomNotification, Widget 에 주로 사용된다.
                 * */
                notificationBuilder
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomBigContentView(
                        RemoteViews(
                            packageName,
                            R.layout.view_custom_notification
                        ).apply {
                            setTextViewText(R.id.txt_title, title)
                            setTextViewText(R.id.txt_message, content)
                        }
                    )
            }
        }

        return notificationBuilder.build()
    }

    companion object {
        // 채널 정보 세팅
        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"
        private const val CHANNEL_ID = "Channel Id"
    }
}