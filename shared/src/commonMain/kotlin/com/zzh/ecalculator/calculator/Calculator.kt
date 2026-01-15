package com.zzh.ecalculator.calculator

import com.zzh.ecalculator.platform.TimeProvider

/**
 * 计算器统一入口类
 * 
 * 这是计算器的门面类，提供简洁的API供外部调用。
 * 内部协调ExpressionParser和ExpressionEvaluator完成完整的计算流程。
 * 
 * 设计特点：
 * 1. 门面模式：隐藏内部复杂性，提供简单易用的接口
 * 2. 单一职责：只负责协调各组件，不包含具体计算逻辑
 * 3. 异常处理：统一处理和包装异常信息
 * 4. 状态管理：支持计算历史记录（可选）
 * 5. 跨平台时间：使用expect/actual机制确保兼容性
 * 
 * 使用示例：
 * ```
 * val calculator = Calculator()
 * val result = calculator.calculate("2 + 3 * 4")  // 返回 14.0
 * ```
 */
class Calculator {
    
    private val parser = ExpressionParser()
    private val evaluator = ExpressionEvaluator()
    
    /**
     * 计算历史记录（可选功能）
     */
    private val history = mutableListOf<CalculationRecord>()
    
    /**
     * 获取当前时间毫秒数，使用跨平台expect/actual机制
     */
    private fun currentTimeMillis(): Long {
        return TimeProvider.currentTimeMillis()
    }
    
    /**
     * 计算表达式
     * 
     * @param expression 数学表达式字符串
     * @return 计算结果
     * @throws CalculationException 当计算过程出现错误时
     */
    fun calculate(expression: String): Double {
        return try {
            val cleanExpression = expression.trim()
            
            // 记录开始时间（用于性能监控）
            val startTime = currentTimeMillis()
            
            // 第一步：解析表达式为后缀表达式
            val postfixExpression = parser.parseToPostfix(cleanExpression)
            
            // 第二步：计算后缀表达式
            val result = evaluator.evaluate(postfixExpression)
            
            // 记录结束时间
            val endTime = currentTimeMillis()
            val calculationTime = endTime - startTime
            
            // 添加到历史记录
            addToHistory(cleanExpression, result, calculationTime)
            
            result
            
        } catch (e: IllegalArgumentException) {
            throw CalculationException("表达式格式错误: ${e.message}", e)
        } catch (e: ArithmeticException) {
            throw CalculationException("数学计算错误: ${e.message}", e)
        } catch (e: Exception) {
            throw CalculationException("计算失败: ${e.message}", e)
        }
    }
    
    /**
     * 计算并返回详细结果
     * 
     * @param expression 数学表达式字符串
     * @return 详细的计算结果
     */
    fun calculateDetailed(expression: String): CalculationResult {
        return try {
            val cleanExpression = expression.trim()
            val startTime = currentTimeMillis()
            
            // 解析为后缀表达式
            val postfixExpression = parser.parseToPostfix(cleanExpression)
            
            // 计算结果
            val result = evaluator.evaluate(postfixExpression)
            
            val endTime = currentTimeMillis()
            val calculationTime = endTime - startTime
            
            val calculationResult = CalculationResult(
                originalExpression = cleanExpression,
                postfixExpression = postfixExpression,
                result = result,
                calculationTimeMs = calculationTime,
                success = true
            )
            
            // 添加到历史记录
            addToHistory(cleanExpression, result, calculationTime)
            
            calculationResult
            
        } catch (e: Exception) {
            CalculationResult(
                originalExpression = expression.trim(),
                postfixExpression = emptyList(),
                result = Double.NaN,
                calculationTimeMs = 0,
                success = false,
                errorMessage = e.message
            )
        }
    }
    
    /**
     * 验证表达式格式
     * 
     * @param expression 待验证的表达式
     * @return 验证结果
     */
    fun validateExpression(expression: String): ValidationResult {
        return try {
            parser.parseToPostfix(expression.trim())
            ValidationResult(true)
        } catch (e: Exception) {
            ValidationResult(false, e.message ?: "未知错误")
        }
    }
    
    /**
     * 获取计算历史记录
     * 
     * @param limit 返回记录数量限制，默认为10条
     * @return 历史记录列表
     */
    fun getHistory(limit: Int = 10): List<CalculationRecord> {
        return history.takeLast(limit)
    }
    
    /**
     * 清除历史记录
     */
    fun clearHistory() {
        history.clear()
    }
    
    /**
     * 获取运算符信息
     * 
     * @return 所有已注册运算符的信息
     */
    fun getOperatorInfo(): List<OperatorInfo> {
        return OperatorRegistry.getAllOperators().map { operator ->
            OperatorInfo(
                symbol = operator.symbol,
                precedence = operator.precedence,
                associativity = operator.associativity,
                operandCount = operator.operandCount
            )
        }
    }
    
    /**
     * 注册新运算符
     * 
     * @param operator 要注册的运算符
     */
    fun registerOperator(operator: Operator) {
        OperatorRegistry.register(operator)
    }
    
    /**
     * 添加记录到历史
     */
    private fun addToHistory(expression: String, result: Double, calculationTime: Long) {
        val record = CalculationRecord(
            expression = expression,
            result = result,
            timestamp = currentTimeMillis(),
            calculationTimeMs = calculationTime
        )
        history.add(record)
        
        // 限制历史记录数量，避免内存泄漏
        if (history.size > 100) {
            history.removeAt(0)
        }
    }
}

/**
 * 计算异常
 */
class CalculationException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * 计算结果数据类
 */
data class CalculationResult(
    val originalExpression: String,
    val postfixExpression: List<String>,
    val result: Double,
    val calculationTimeMs: Long,
    val success: Boolean,
    val errorMessage: String? = null
)

/**
 * 验证结果数据类
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

/**
 * 计算历史记录数据类
 */
data class CalculationRecord(
    val expression: String,
    val result: Double,
    val timestamp: Long,
    val calculationTimeMs: Long
)

/**
 * 运算符信息数据类
 */
data class OperatorInfo(
    val symbol: String,
    val precedence: Int,
    val associativity: Associativity,
    val operandCount: Int
)
