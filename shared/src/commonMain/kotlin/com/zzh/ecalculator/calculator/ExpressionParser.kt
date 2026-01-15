package com.zzh.ecalculator.calculator

/**
 * 表达式解析器
 * 
 * 负责将中缀表达式转换为后缀表达式（逆波兰表达式），以便于后续计算。
 * 使用调度场算法（Shunting Yard Algorithm）实现。
 * 
 * 支持的功能：
 * 1. 运算符优先级处理
 * 2. 括号支持
 * 3. 结合律处理
 * 4. 数字识别
 * 5. 空格忽略
 * 
 * 算法思路：
 * 1. 从左到右扫描中缀表达式
 * 2. 操作数直接输出到结果队列
 * 3. 运算符根据优先级和结合律进入栈或输出到结果队列
 * 4. 左括号直接入栈
 * 5. 右括号触发栈内运算符弹出直到遇到左括号
 */
class ExpressionParser {
    
    /**
     * Token类型枚举
     */
    private enum class TokenType {
        NUMBER,     // 数字
        OPERATOR,   // 运算符
        LEFT_PAREN, // 左括号
        RIGHT_PAREN // 右括号
    }
    
    /**
     * Token数据类
     */
    private data class Token(
        val type: TokenType,
        val value: String
    )
    
    /**
     * 将中缀表达式转换为后缀表达式
     * 
     * @param expression 中缀表达式字符串
     * @return 后缀表达式的Token列表
     * @throws IllegalArgumentException 当表达式格式错误时
     */
    fun parseToPostfix(expression: String): List<String> {
        if (expression.isBlank()) {
            throw IllegalArgumentException("表达式不能为空")
        }
        
        val tokens = tokenize(expression)
        return convertToPostfix(tokens)
    }
    
    /**
     * 将表达式字符串分解为Token列表
     * 
     * @param expression 表达式字符串
     * @return Token列表
     */
    private fun tokenize(expression: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var i = 0
        
        while (i < expression.length) {
            val char = expression[i]
            
            when {
                char.isWhitespace() -> {
                    // 跳过空格
                    i++
                }
                char.isDigit() || char == '.' -> {
                    // 处理数字（包括小数）
                    val number = parseNumber(expression, i)
                    tokens.add(Token(TokenType.NUMBER, number.first))
                    i = number.second
                }
                char == '(' -> {
                    tokens.add(Token(TokenType.LEFT_PAREN, "("))
                    i++
                }
                char == ')' -> {
                    tokens.add(Token(TokenType.RIGHT_PAREN, ")"))
                    i++
                }
                else -> {
                    // 处理运算符
                    val operator = parseOperator(expression, i)
                    tokens.add(Token(TokenType.OPERATOR, operator.first))
                    i = operator.second
                }
            }
        }
        
        return tokens
    }
    
    /**
     * 从指定位置解析数字
     * 
     * @param expression 表达式字符串
     * @param startIndex 开始位置
     * @return Pair<解析出的数字字符串, 结束位置>
     */
    private fun parseNumber(expression: String, startIndex: Int): Pair<String, Int> {
        var i = startIndex
        val number = StringBuilder()
        var hasDot = false
        var hasDigits = false
        
        while (i < expression.length) {
            val char = expression[i]
            when {
                char.isDigit() -> {
                    hasDigits = true
                    number.append(char)
                    i++
                }
                char == '.' && !hasDot -> {
                    hasDot = true
                    number.append(char)
                    i++
                }
                else -> break
            }
        }
        
        val numberStr = number.toString()
        
        // 验证数字格式
        if (!hasDigits) {
            throw IllegalArgumentException("无效的数字格式: '$numberStr'")
        }
        
        if (numberStr == ".") {
            throw IllegalArgumentException("无效的数字格式: '$numberStr'")
        }
        
        // 检查解析停止后是否还有连续的小数点
        if (i < expression.length && expression[i] == '.') {
            throw IllegalArgumentException("数字中包含多个小数点")
        }
        
        // 最终验证数字格式
        try {
            numberStr.toDouble()
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("无效的数字格式: '$numberStr'")
        }
        
        return Pair(numberStr, i)
    }
    
    /**
     * 从指定位置解析运算符
     * 
     * @param expression 表达式字符串
     * @param startIndex 开始位置
     * @return Pair<解析出的运算符字符串, 结束位置>
     */
    private fun parseOperator(expression: String, startIndex: Int): Pair<String, Int> {
        val char = expression[startIndex].toString()
        
        // 检查是否为已注册的运算符
        if (!OperatorRegistry.isOperatorRegistered(char)) {
            throw IllegalArgumentException("未知的运算符: '$char'")
        }
        
        return Pair(char, startIndex + 1)
    }
    
    /**
     * 使用调度场算法将Token列表转换为后缀表达式
     * 
     * @param tokens Token列表
     * @return 后缀表达式的字符串列表
     */
    private fun convertToPostfix(tokens: List<Token>): List<String> {
        val output = mutableListOf<String>()  // 输出队列
        val operatorStack = mutableListOf<String>()  // 运算符栈
        
        // 基础验证：检查表达式结构
        validateTokenSequence(tokens)
        
        for (token in tokens) {
            when (token.type) {
                TokenType.NUMBER -> {
                    // 数字直接输出
                    output.add(token.value)
                }
                
                TokenType.OPERATOR -> {
                    val currentOperator = OperatorRegistry.getOperator(token.value)
                    
                    // 根据优先级和结合律处理栈中的运算符
                    while (operatorStack.isNotEmpty() && 
                           operatorStack.last() != "(" &&
                           shouldPopOperator(currentOperator, operatorStack.last())) {
                        output.add(operatorStack.removeLastOrNull() ?: break)
                    }
                    
                    operatorStack.add(token.value)
                }
                
                TokenType.LEFT_PAREN -> {
                    // 左括号直接入栈
                    operatorStack.add("(")
                }
                
                TokenType.RIGHT_PAREN -> {
                    // 右括号：弹出栈中的运算符直到遇到左括号
                    var foundLeftParen = false
                    while (operatorStack.isNotEmpty()) {
                        val operator = operatorStack.removeLastOrNull()
                        if (operator == "(") {
                            foundLeftParen = true
                            break
                        } else if (operator != null) {
                            output.add(operator)
                        }
                    }
                    
                    if (!foundLeftParen) {
                        throw IllegalArgumentException("括号不匹配：缺少左括号")
                    }
                }
            }
        }
        
        // 弹出栈中剩余的运算符
        while (operatorStack.isNotEmpty()) {
            val operator = operatorStack.removeLastOrNull()
            if (operator == "(" || operator == ")") {
                throw IllegalArgumentException("括号不匹配")
            } else if (operator != null) {
                output.add(operator)
            }
        }
        
        return output
    }
    
    /**
     * 验证token序列的基础合法性
     */
    private fun validateTokenSequence(tokens: List<Token>) {
        if (tokens.isEmpty()) {
            throw IllegalArgumentException("表达式为空")
        }
        
        // 检查表达式不能以运算符开头或结尾（除了左右括号）
        if (tokens.first().type == TokenType.OPERATOR) {
            throw IllegalArgumentException("表达式不能以运算符开头")
        }
        
        if (tokens.last().type == TokenType.OPERATOR) {
            throw IllegalArgumentException("表达式不能以运算符结尾")
        }
        
        // 检查连续的运算符
        for (i in 0 until tokens.size - 1) {
            val current = tokens[i]
            val next = tokens[i + 1]
            
            if (current.type == TokenType.OPERATOR && next.type == TokenType.OPERATOR) {
                throw IllegalArgumentException("不能有连续的运算符")
            }
        }
    }
    
    /**
     * 判断是否应该弹出栈顶运算符
     * 
     * @param currentOperator 当前运算符
     * @param stackTopOperator 栈顶运算符符号
     * @return true 如果应该弹出栈顶运算符
     */
    private fun shouldPopOperator(currentOperator: Operator, stackTopOperator: String): Boolean {
        if (stackTopOperator == "(") return false
        
        val stackOperator = OperatorRegistry.getOperator(stackTopOperator)
        
        return when {
            stackOperator.precedence > currentOperator.precedence -> true
            stackOperator.precedence == currentOperator.precedence && 
            currentOperator.associativity == Associativity.LEFT -> true
            else -> false
        }
    }
}
