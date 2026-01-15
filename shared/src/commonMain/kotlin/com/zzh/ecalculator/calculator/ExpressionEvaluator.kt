package com.zzh.ecalculator.calculator

/**
 * 表达式求值器
 * 
 * 负责计算后缀表达式（逆波兰表达式）的结果。
 * 使用栈数据结构实现后缀表达式的计算。
 * 
 * 算法思路：
 * 1. 从左到右扫描后缀表达式
 * 2. 遇到操作数时，压入栈中
 * 3. 遇到运算符时，从栈中弹出相应数量的操作数进行计算
 * 4. 将计算结果压入栈中
 * 5. 最终栈中只剩一个元素，即为计算结果
 */
class ExpressionEvaluator {
    
    /**
     * 计算后缀表达式
     * 
     * @param postfixExpression 后缀表达式的Token列表
     * @return 计算结果
     * @throws IllegalArgumentException 当表达式格式错误时
     * @throws ArithmeticException 当发生数学错误时
     */
    fun evaluate(postfixExpression: List<String>): Double {
        if (postfixExpression.isEmpty()) {
            throw IllegalArgumentException("后缀表达式不能为空")
        }
        
        val stack = mutableListOf<Double>()
        
        for (token in postfixExpression) {
            when {
                isNumber(token) -> {
                    // 操作数：压入栈中
                    try {
                        stack.add(token.toDouble())
                    } catch (e: NumberFormatException) {
                        throw IllegalArgumentException("无效的数字: '$token'")
                    }
                }
                
                OperatorRegistry.isOperatorRegistered(token) -> {
                    // 运算符：执行计算
                    val operator = OperatorRegistry.getOperator(token)
                    val result = performOperation(operator, stack)
                    stack.add(result)
                }
                
                else -> {
                    throw IllegalArgumentException("无效的Token: '$token'")
                }
            }
        }
        
        // 验证最终结果
        return when (stack.size) {
            0 -> throw IllegalArgumentException("表达式为空或无效")
            1 -> stack[0]
            else -> throw IllegalArgumentException("表达式格式错误：栈中剩余 ${stack.size} 个元素")
        }
    }
    
    /**
     * 执行运算操作
     * 
     * @param operator 运算符
     * @param stack 操作数栈
     * @return 计算结果
     * @throws IllegalArgumentException 当操作数不足时
     */
    private fun performOperation(operator: Operator, stack: MutableList<Double>): Double {
        val operandCount = operator.operandCount
        
        // 检查栈中是否有足够的操作数
        if (stack.size < operandCount) {
            throw IllegalArgumentException(
                "运算符 '${operator.symbol}' 需要 $operandCount 个操作数，但栈中只有 ${stack.size} 个"
            )
        }
        
        // 从栈中弹出操作数
        val operands = mutableListOf<Double>()
        repeat(operandCount) {
            if (stack.isNotEmpty()) {
                operands.add(0, stack.removeLastOrNull() ?: 0.0)  // 注意顺序：后入先出，但计算时要保持原顺序
            }
        }
        
        // 执行计算
        return try {
            operator.calculate(operands)
        } catch (e: ArithmeticException) {
            throw ArithmeticException("计算错误: ${e.message}")
        } catch (e: Exception) {
            throw IllegalArgumentException("运算符 '${operator.symbol}' 计算失败: ${e.message}")
        }
    }
    
    /**
     * 判断字符串是否为数字
     * 
     * @param token 待判断的字符串
     * @return true 如果是有效数字
     */
    private fun isNumber(token: String): Boolean {
        return try {
            token.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }
    
    /**
     * 计算简单的双操作数表达式（用于快速计算）
     * 
     * @param left 左操作数
     * @param operator 运算符符号
     * @param right 右操作数
     * @return 计算结果
     */
    fun evaluateSimple(left: Double, operator: String, right: Double): Double {
        val operatorInstance = OperatorRegistry.getOperator(operator)
        return operatorInstance.calculate(listOf(left, right))
    }
    
    /**
     * 计算单操作数表达式（用于一元运算符）
     * 
     * @param operator 运算符符号
     * @param operand 操作数
     * @return 计算结果
     */
    fun evaluateUnary(operator: String, operand: Double): Double {
        val operatorInstance = OperatorRegistry.getOperator(operator)
        if (operatorInstance.operandCount != 1) {
            throw IllegalArgumentException("运算符 '$operator' 不是一元运算符")
        }
        return operatorInstance.calculate(listOf(operand))
    }
}
