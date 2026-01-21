package com.zzh.ecalculator.calculator

import com.zzh.ecalculator.calculator.operators.AddOperator
import com.zzh.ecalculator.calculator.operators.DivideOperator
import com.zzh.ecalculator.calculator.operators.ModOperator
import com.zzh.ecalculator.calculator.operators.MultiplyOperator
import com.zzh.ecalculator.calculator.operators.SubtractOperator

/**
 * 运算符注册中心
 * 
 * 这个类负责管理所有的运算符实例，提供注册、查找和获取运算符的功能。
 * 使用注册中心模式，实现了运算符的统一管理和动态扩展。
 * 
 * 设计特点：
 * 1. 单例模式：确保全局只有一个注册中心实例
 * 2. 策略模式：每个运算符都是一个策略
 * 3. 工厂模式：根据符号创建或获取运算符
 * 4. 开闭原则：新增运算符只需要注册，无需修改现有代码
 */
object OperatorRegistry {
    
    /**
     * 运算符存储映射
     * Key: 运算符符号 (如 "+", "-", "*", "/")
     * Value: 运算符实例
     */
    private val operators = mutableMapOf<String, Operator>()
    
    init {
        // 注册基础运算符
        registerDefaultOperators()
    }
    
    /**
     * 注册默认的基础运算符
     */
    private fun registerDefaultOperators() {
        register(AddOperator())
        register(SubtractOperator())
        register(MultiplyOperator())
        register(DivideOperator())
        register(ModOperator())
    }
    
    /**
     * 注册运算符
     * 
     * @param operator 要注册的运算符
     * @throws IllegalArgumentException 如果运算符符号已存在
     */
    fun register(operator: Operator) {
        if (operators.containsKey(operator.symbol)) {
            throw IllegalArgumentException("运算符 '${operator.symbol}' 已经注册")
        }
        operators[operator.symbol] = operator
    }
    
    /**
     * 强制注册运算符（覆盖已存在的运算符）
     * 
     * @param operator 要注册的运算符
     */
    fun forceRegister(operator: Operator) {
        operators[operator.symbol] = operator
    }
    
    /**
     * 根据符号获取运算符
     * 
     * @param symbol 运算符符号
     * @return 对应的运算符实例
     * @throws IllegalArgumentException 如果找不到对应的运算符
     */
    fun getOperator(symbol: String): Operator {
        return operators[symbol] 
            ?: throw IllegalArgumentException("未知的运算符: '$symbol'")
    }
    
    /**
     * 检查运算符是否已注册
     * 
     * @param symbol 运算符符号
     * @return 如果已注册返回 true，否则返回 false
     */
    fun isOperatorRegistered(symbol: String): Boolean {
        return operators.containsKey(symbol)
    }
    
    /**
     * 获取所有已注册的运算符符号
     * 
     * @return 所有运算符符号的集合
     */
    fun getAllOperatorSymbols(): Set<String> {
        return operators.keys.toSet()
    }
    
    /**
     * 获取所有已注册的运算符
     * 
     * @return 所有运算符的集合
     */
    fun getAllOperators(): Collection<Operator> {
        return operators.values
    }
    
    /**
     * 移除运算符
     * 
     * @param symbol 要移除的运算符符号
     * @return 如果成功移除返回 true，否则返回 false
     */
    fun unregister(symbol: String): Boolean {
        return operators.remove(symbol) != null
    }
    
    /**
     * 清空所有运算符（慎用）
     */
    fun clearAll() {
        operators.clear()
    }
    
    /**
     * 重置为默认运算符
     */
    fun resetToDefault() {
        clearAll()
        registerDefaultOperators()
    }
    
    /**
     * 获取运算符数量
     */
    fun getOperatorCount(): Int {
        return operators.size
    }
    
    /**
     * 根据优先级获取运算符（用于表达式解析）
     * 
     * @param precedence 优先级
     * @return 指定优先级的运算符列表
     */
    fun getOperatorsByPrecedence(precedence: Int): List<Operator> {
        return operators.values.filter { it.precedence == precedence }
    }
    
    /**
     * 获取最高优先级
     */
    fun getMaxPrecedence(): Int {
        return operators.values.maxOfOrNull { it.precedence } ?: 0
    }
    
    /**
     * 获取最低优先级
     */
    fun getMinPrecedence(): Int {
        return operators.values.minOfOrNull { it.precedence } ?: 0
    }
}
