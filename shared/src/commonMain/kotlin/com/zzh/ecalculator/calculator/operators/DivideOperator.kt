package com.zzh.ecalculator.calculator.operators

import com.zzh.ecalculator.calculator.Associativity
import com.zzh.ecalculator.calculator.Operator

/**
 * 除法运算符实现
 * 
 * 特点：
 * - 符号：/
 * - 优先级：2（中等）
 * - 结合律：左结合
 * - 操作数：2个
 * - 特殊处理：检查除零错误
 */
class DivideOperator : Operator {
    override val symbol = "/"
    override val precedence = 2
    override val associativity = Associativity.LEFT
    override val operandCount = 2
    
    override fun calculate(operands: List<Double>): Double {
        validateOperands(operands)
        
        val dividend = operands[0]  // 被除数
        val divisor = operands[1]   // 除数
        
        if (divisor == 0.0) {
            throw ArithmeticException("除数不能为零")
        }
        
        return dividend / divisor
    }
    
    override fun validateOperands(operands: List<Double>) {
        super.validateOperands(operands)
        
        // 额外的除法特定验证
        if (operands.size >= 2 && operands[1] == 0.0) {
            throw ArithmeticException("除数不能为零")
        }
    }
}
