package com.zzh.ecalculator.calculator

/**
 * 运算符抽象接口
 * 
 * 这个接口定义了所有运算符必须实现的基本行为，包括运算逻辑、优先级和结合律。
 * 通过接口抽象，我们实现了开闭原则：对扩展开放，对修改关闭。
 * 
 * 设计原则：
 * 1. 单一职责：每个运算符只负责自己的运算逻辑
 * 2. 开闭原则：新增运算符不需要修改现有代码
 * 3. 接口隔离：接口简洁明了，只包含必需的方法
 */
interface Operator {
    
    /**
     * 运算符的字符表示
     * 例如："+", "-", "*", "/", "^", "sin", "cos" 等
     */
    val symbol: String
    
    /**
     * 运算符的优先级
     * 数字越大优先级越高
     * 
     * 标准优先级约定：
     * - 1: 加法、减法 (+, -)
     * - 2: 乘法、除法 (*, /)
     * - 3: 指数运算 (^)
     * - 4: 函数运算 (sin, cos, log, etc.)
     */
    val precedence: Int
    
    /**
     * 结合律
     * - LEFT: 左结合（大多数运算符）
     * - RIGHT: 右结合（如指数运算符 ^）
     */
    val associativity: Associativity
    
    /**
     * 操作数数量
     * - 1: 一元运算符（如负号、sin、cos等）
     * - 2: 二元运算符（如 +、-、*、/ 等）
     */
    val operandCount: Int
    
    /**
     * 执行运算
     * 
     * @param operands 操作数列表，数量必须与 operandCount 匹配
     * @return 运算结果
     * @throws IllegalArgumentException 当操作数数量不匹配时
     * @throws ArithmeticException 当发生数学错误时（如除零）
     * @throws NumberFormatException 当操作数格式错误时
     */
    fun calculate(operands: List<Double>): Double
    
    /**
     * 验证操作数
     * 子类可以重写此方法来添加特定的验证逻辑
     * 
     * @param operands 待验证的操作数
     * @throws IllegalArgumentException 当操作数不符合要求时
     */
    fun validateOperands(operands: List<Double>) {
        if (operands.size != operandCount) {
            throw IllegalArgumentException(
                "运算符 '$symbol' 需要 $operandCount 个操作数，但提供了 ${operands.size} 个"
            )
        }
    }
}

/**
 * 结合律枚举
 */
enum class Associativity {
    LEFT,   // 左结合
    RIGHT   // 右结合
}
