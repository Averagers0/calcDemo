package com.zzh.ecalculator.calculator.operators

import com.zzh.ecalculator.calculator.Associativity
import com.zzh.ecalculator.calculator.Operator

/**
 * 乘法运算符实现
 * 
 * 特点：
 * - 符号：*
 * - 优先级：2（中等）
 * - 结合律：左结合
 * - 操作数：2个
 */
class MultiplyOperator : Operator {
    override val symbol = "*"
    override val precedence = 2
    override val associativity = Associativity.LEFT
    override val operandCount = 2
    
    override fun calculate(operands: List<Double>): Double {
        validateOperands(operands)
        return operands[0] * operands[1]
    }
}
