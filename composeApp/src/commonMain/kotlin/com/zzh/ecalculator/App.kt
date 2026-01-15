package com.zzh.ecalculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zzh.ecalculator.calculator.Calculator
import com.zzh.ecalculator.calculator.CalculationException

@Composable
@Preview
fun App() {
    MaterialTheme {
        CalculatorApp()
    }
}

@Composable
fun CalculatorApp() {
    val calculator = remember { Calculator() }
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("0") }
    var showHistory by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 标题
        Text(
            "ECalculator",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        
        // 显示屏区域
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // 表达式显示
                Text(
                    text = if (expression.isEmpty()) "输入表达式" else expression,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = FontFamily.Monospace
                    ),
                    color = if (expression.isEmpty()) 
                        MaterialTheme.colorScheme.outline 
                    else 
                        MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 结果显示
                Text(
                    text = result,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (errorMessage != null) 
                        MaterialTheme.colorScheme.error 
                    else 
                        MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
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
        
        // 按钮区域
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 计算按钮
            Button(
                onClick = {
                    try {
                        if (expression.isNotEmpty()) {
                            val calculationResult = calculator.calculate(expression)
                            result = formatNumber(calculationResult)
                            errorMessage = null
                        }
                    } catch (e: CalculationException) {
                        result = "错误"
                        errorMessage = e.message
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("计算 (=)")
            }
            
            // 清除按钮
            OutlinedButton(
                onClick = {
                    expression = ""
                    result = "0"
                    errorMessage = null
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("清除")
            }
        }
        
        // 输入区域
        TextField(
            value = expression,
            onValueChange = { 
                expression = it 
                errorMessage = null
            },
            label = { Text("输入数学表达式") },
            placeholder = { Text("例如: 2 + 3 * 4, (1 + 2) * 5") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true
        )
        
        // 历史记录按钮
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { showHistory = !showHistory },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (showHistory) "隐藏历史" else "显示历史")
            }
            
            OutlinedButton(
                onClick = { calculator.clearHistory() },
                modifier = Modifier.weight(1f)
            ) {
                Text("清除历史")
            }
        }
        
        // 内容区域
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    if (showHistory) "计算历史" else "示例表达式",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                if (showHistory) {
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
                        LazyColumn {
                            items(history.reversed()) { record ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp)
                                        .clickable { expression = record.expression },
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = record.expression,
                                            style = MaterialTheme.typography.bodyMedium.copy(
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
                } else {
                    val examples = listOf(
                        "2 + 3" to "基础加法",
                        "2 + 3 * 4" to "运算符优先级",
                        "(2 + 3) * 4" to "括号改变优先级",
                        "10 / 3" to "除法运算",
                        "((1 + 2) * 3 - 4) / 2" to "复杂表达式"
                    )
                    
                    LazyColumn {
                        items(examples) { (expr, desc) ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                                    .clickable { expression = expr },
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = expr,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = desc,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
