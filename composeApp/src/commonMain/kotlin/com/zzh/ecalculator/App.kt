package com.zzh.ecalculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzh.ecalculator.calculator.Calculator
import com.zzh.ecalculator.calculator.CalculationException
import com.zzh.ecalculator.calculator.CalculationRecord

@Composable
@Preview
fun App() {
    MaterialTheme {
        // 为桌面端添加响应式容器
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CalculatorApp(
                modifier = Modifier
                    .widthIn(max = 500.dp) // 设置最大宽度
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
fun CalculatorApp(modifier: Modifier = Modifier) {
    val calculator = remember { Calculator() }
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("0") }
    var showHistory by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var lastResult by remember { mutableStateOf<Double?>(null) }
    
    // 计算函数
    fun performCalculation() {
        try {
            if (expression.isNotEmpty()) {
                val calculationResult = calculator.calculate(expression)
                result = formatNumber(calculationResult)
                lastResult = calculationResult
                errorMessage = null
            }
        } catch (e: CalculationException) {
            result = "错误"
            errorMessage = e.message
        }
    }
    
    // 添加字符到表达式
    fun addToExpression(value: String) {
        expression += value
        errorMessage = null
    }
    
    // 清除所有
    fun clearAll() {
        expression = ""
        result = "0"
        errorMessage = null
        lastResult = null
    }
    
    // 删除最后一个字符
    fun deleteLast() {
        if (expression.isNotEmpty()) {
            expression = expression.dropLast(1)
            errorMessage = null
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // 主计算器界面
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 显示屏区域 - 优化后的设计
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // 表达式显示
                    Text(
                        text = if (expression.isEmpty()) "0" else expression,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 18.sp,
                            lineHeight = 22.sp
                        ),
                        color = if (expression.isEmpty()) 
                            MaterialTheme.colorScheme.outline 
                        else 
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    // 结果显示
                    Text(
                        text = result,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 40.sp
                        ),
                        color = if (errorMessage != null) 
                            MaterialTheme.colorScheme.error 
                        else 
                            MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    // 错误信息
                    errorMessage?.let { error ->
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            textAlign = TextAlign.End,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 按键区域 - 现在总是占用固定空间
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 第一行: 清除和功能按钮
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "AC",
                        onClick = { clearAll() },
                        modifier = Modifier.weight(2f),
                        buttonType = ButtonType.FUNCTION
                    )
                    
                    CalculatorButton(
                        text = "DEL",
                        onClick = { deleteLast() },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.FUNCTION
                    )
                    
                    CalculatorButton(
                        text = "÷",
                        onClick = { addToExpression(" / ") },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.OPERATOR
                    )
                }
                
                // 第二行: 数字和运算符
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "7",
                        onClick = { addToExpression("7") },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.NUMBER
                    )
                    
                    CalculatorButton(
                        text = "8",
                        onClick = { addToExpression("8") },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.NUMBER
                    )
                    
                    CalculatorButton(
                        text = "9",
                        onClick = { addToExpression("9") },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.NUMBER
                    )
                    
                    CalculatorButton(
                        text = "×",
                        onClick = { addToExpression(" * ") },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.OPERATOR
                    )
                }
                
                // 第三行: 数字和运算符
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "4",
                        onClick = { addToExpression("4") },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.NUMBER
                    )
                    
                    CalculatorButton(
                        text = "5",
                        onClick = { addToExpression("5") },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.NUMBER
                    )
                    
                    CalculatorButton(
                        text = "6",
                        onClick = { addToExpression("6") },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.NUMBER
                    )
                    
                    CalculatorButton(
                        text = "−",
                        onClick = { addToExpression(" - ") },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.OPERATOR
                    )
                }
                
                // 第四行: 数字和运算符
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "1",
                        onClick = { addToExpression("1") },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.NUMBER
                    )
                    
                    CalculatorButton(
                        text = "2",
                        onClick = { addToExpression("2") },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.NUMBER
                    )
                    
                    CalculatorButton(
                        text = "3",
                        onClick = { addToExpression("3") },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.NUMBER
                    )
                    
                    CalculatorButton(
                        text = "+",
                        onClick = { addToExpression(" + ") },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.OPERATOR
                    )
                }
                
                // 第五行: 特殊功能行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "(",
                        onClick = { addToExpression("(") },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.FUNCTION
                    )
                    
                    CalculatorButton(
                        text = "0",
                        onClick = { addToExpression("0") },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.NUMBER
                    )
                    
                    CalculatorButton(
                        text = ")",
                        onClick = { addToExpression(")") },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.FUNCTION
                    )
                    
                    CalculatorButton(
                        text = "%",
                        onClick = { addToExpression(" % ") },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.OPERATOR
                    )
                }
                
                // 第六行: 底部功能行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = if (showHistory) "隐藏" else "历史",
                        onClick = { showHistory = !showHistory },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.SECONDARY,
                        fontSize = 14
                    )
                    
                    CalculatorButton(
                        text = ".",
                        onClick = { addToExpression(".") },
                        modifier = Modifier.weight(1f),
                        buttonType = ButtonType.FUNCTION
                    )
                    
                    CalculatorButton(
                        text = "=",
                        onClick = { performCalculation() },
                        modifier = Modifier.weight(2f),
                        buttonType = ButtonType.EQUALS
                    )
                }
            }
        }
        
        // 全屏历史记录覆盖层
        if (showHistory) {
            HistoryOverlay(
                calculator = calculator,
                onSelectExpression = { expr ->
                    expression = expr
                    showHistory = false
                },
                onDismiss = { showHistory = false }
            )
        }
    }
}

// 按钮类型枚举
enum class ButtonType {
    NUMBER,     // 数字按钮
    OPERATOR,   // 运算符按钮  
    FUNCTION,   // 功能按钮
    SECONDARY,  // 次要功能按钮
    EQUALS      // 等号按钮
}

// 优化后的计算器按钮组件
@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonType: ButtonType = ButtonType.NUMBER,
    fontSize: Int = 20
) {
    val (backgroundColor, textColor, elevation) = when (buttonType) {
        ButtonType.NUMBER -> Triple(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.onSurface,
            4.dp
        )
        ButtonType.OPERATOR -> Triple(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.onPrimary,
            6.dp
        )
        ButtonType.FUNCTION -> Triple(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            4.dp
        )
        ButtonType.SECONDARY -> Triple(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            2.dp
        )
        ButtonType.EQUALS -> Triple(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
            MaterialTheme.colorScheme.onPrimary,
            8.dp
        )
    }
    
    Card(
        modifier = modifier
            .height(64.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation,
            pressedElevation = elevation + 4.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = fontSize.sp,
                fontWeight = when (buttonType) {
                    ButtonType.EQUALS -> FontWeight.Bold
                    ButtonType.OPERATOR -> FontWeight.SemiBold
                    else -> FontWeight.Medium
                },
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

// 全屏历史记录覆盖层
@Composable
fun HistoryOverlay(
    calculator: Calculator,
    onSelectExpression: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 半透明背景
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onDismiss() }
    ) {
        // 历史记录卡片
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .clickable { }, // 阻止点击事件传递到背景
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // 标题栏
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "计算历史",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 清除历史按钮
                        OutlinedButton(
                            onClick = { calculator.clearHistory() },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(
                                "清除全部",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        // 关闭按钮
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Text(
                                "×",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 历史记录列表
                val history = calculator.getHistory()
                
                if (history.isEmpty()) {
                    // 空状态
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "📝",
                                style = MaterialTheme.typography.displaySmall,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Text(
                                "暂无历史记录",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.outline,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "开始计算后，历史记录会显示在这里",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                } else {
                    // 历史记录列表
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        reverseLayout = true // 最新的记录在顶部
                    ) {
                        items(history) { record ->
                            HistoryItem(
                                record = record,
                                onClick = { onSelectExpression(record.expression) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// 历史记录条目组件
@Composable
fun HistoryItem(
    record: CalculationRecord,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 表达式和结果
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = record.expression,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = FontFamily.Monospace
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "= ${formatNumber(record.result)}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                
                // 时间戳
                Text(
                    text = formatTimestamp(record.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            // 使用按钮
            TextButton(
                onClick = onClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    "使用",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private fun formatNumber(number: Double): String {
    return if (number == number.toLong().toDouble()) {
        number.toLong().toString()
    } else {
        // 使用跨平台兼容的方法格式化小数
        val rounded = (number * 1000000).toLong().toDouble() / 1000000
        val str = rounded.toString()
        // 移除尾随的零和小数点
        if (str.contains('.')) {
            str.trimEnd('0').trimEnd('.')
        } else {
            str
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    // 简单的时间格式化，显示相对时间
    val now = com.zzh.ecalculator.platform.TimeProvider.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60000 -> "刚刚"
        diff < 3600000 -> "${diff / 60000}分钟前"
        diff < 86400000 -> "${diff / 3600000}小时前"
        else -> "${diff / 86400000}天前"
    }
}
