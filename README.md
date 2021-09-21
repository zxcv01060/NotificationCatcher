# 手機通知記錄器 - Android

## 起因

這個App會把所有通知記錄下來，因為Android不像iOS，Line或Telegram長按可以偷看聊天室，所以就自己寫一個已讀不回的小工具

因為我的美工等級是負數的，所以整個畫面完全沒有設計過，反正只是我自己用爽的

## 專案簡介

* 程式語言：Kotlin 1.5.30
* Android版本要求： 最低21(Android 5.0.0 Lollipop)
* 套件
    * Kotlin Coroutines
    * Android Jetpack Room 2.3.0

## 功能

1. 自動擷取收到的通知，第一次啟動需要允許通知擷取
2. 顯示已抓到通知的App
    * 可用右下角的按鈕啟用排序功能
    * 可長按App列表的項目隱藏不想看到的App通知
    * 右側會顯示有幾個通知是新的
3. 顯示通知歷史記錄

## APK

[點此下載](NotificationCatcher-1.0.0.apk)
