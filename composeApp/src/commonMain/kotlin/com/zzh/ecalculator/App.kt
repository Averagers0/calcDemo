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
           
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        // 显示屏区域 - 占用更少空间
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // 表达式显示
                Text(
                    text = if (expression.isEmpty()) "0" else expression,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 20.sp
                    ),
                    color = if (expression.isEmpty()) 
                        MaterialTheme.colorScheme.outline 
                    else 
                        MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 结果显示
                Text(
                    text = result,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
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
                            .padding(top = 4.dp)
                    )
                }
            }
        }
        
        // 按键区域
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            // 第一行: AC, DEL, (, )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    text = "AC",
                    onClick = { clearAll() },
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.errorContainer,
                    textColor = MaterialTheme.colorScheme.onErrorContainer
                )
                
                CalculatorButton(
                    text = "DEL",
                    onClick = { deleteLast() },
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    textColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
                
                CalculatorButton(
                    text = "(",
                    onClick = { addToExpression("(") },
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                    textColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
                
                CalculatorButton(
                    text = ")",
                    onClick = { addToExpression(")") },
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                    textColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
            
            // 第二行: 7, 8, 9, /
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    text = "7",
                    onClick = { addToExpression("7") },
                    modifier = Modifier.weight(1f)
                )
                
                CalculatorButton(
                    text = "8",
                    onClick = { addToExpression("8") },
                    modifier = Modifier.weight(1f)
                )
                
                CalculatorButton(
                    text = "9",
                    onClick = { addToExpression("9") },
                    modifier = Modifier.weight(1f)
                )
                
                CalculatorButton(
                    text = "÷",
                    onClick = { addToExpression(" / ") },
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            // 第三行: 4, 5, 6, *
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    text = "4",
                    onClick = { addToExpression("4") },
                    modifier = Modifier.weight(1f)
                )
                
                CalculatorButton(
                    text = "5",
                    onClick = { addToExpression("5") },
                    modifier = Modifier.weight(1f)
                )
                
                CalculatorButton(
                    text = "6",
                    onClick = { addToExpression("6") },
                    modifier = Modifier.weight(1f)
                )
                
                CalculatorButton(
                    text = "×",
                    onClick = { addToExpression(" * ") },
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            // 第四行: 1, 2, 3, -
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    text = "1",
                    onClick = { addToExpression("1") },
                    modifier = Modifier.weight(1f)
                )
                
                CalculatorButton(
                    text = "2",
                    onClick = { addToExpression("2") },
                    modifier = Modifier.weight(1f)
                )
                
                CalculatorButton(
                    text = "3",
                    onClick = { addToExpression("3") },
                    modifier = Modifier.weight(1f)
                )
                
                CalculatorButton(
                    text = "−",
                    onClick = { addToExpression(" - ") },
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            // 第五行: 0, 历史, +, =
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    text = "0",
                    onClick = { addToExpression("0") },
                    modifier = Modifier.weight(1f)
                )
                
                CalculatorButton(
                    text = if (showHistory) "隐藏" else "历史",
                    onClick = { showHistory = !showHistory },
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 16
                )
                
                CalculatorButton(
                    text = "+",
                    onClick = { addToExpression(" + ") },
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                CalculatorButton(
                    text = "=",
                    onClick = { performCalculation() },
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary
                )
            }
            
            // 历史记录区域 (可选显示)
            if (showHistory) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "计算历史",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            TextButton(
                                onClick = { calculator.clearHistory() }
                            ) {
                                Text("清除历史")
                            }
                        }
                        
                        val history = calculator.getHistory()
                        
                        if (history.isEmpty()) {
                            Text(
                                "暂无历史记录",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.weight(1f)
                            ) {
                                items(history.reversed()) { record ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp)
                                            .clickable { 
                                                expression = record.expression
                                                showHistory = false
                                            },
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                        )
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Text(
                                                text = record.expression,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontFamily = FontFamily.Monospace
                                                ),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = "= ${formatNumber(record.result)}",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontFamily = FontFamily.Monospace,
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// 计算器按钮组件
@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    fontSize: Int = 20
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center
            )
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
