package com.zzh.ecalculator

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ECalculator - 跨平台计算器",
        state = WindowState(
            position = WindowPosition.Aligned(androidx.compose.ui.Alignment.Center),
            size = DpSize(width = 400.dp, height = 700.dp)
        ),
        resizable = true,
        // 设置最小窗口尺寸，确保按键不会过度压缩
        // 注意：minSize 在某些版本中可能不可用，如果编译错误可以移除这行
        // minSize = DpSize(width = 320.dp, height = 500.dp)
    ) {
        App()
    }
}