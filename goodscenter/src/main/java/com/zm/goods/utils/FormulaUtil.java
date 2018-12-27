package com.zm.goods.utils;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.zm.goods.log.LogUtil;

public class FormulaUtil {

	private static final String FUNCTION_NAME = "calRebate";
	private static final String FUNCTION_PRE = "function calRebate(rebate){return ";
	private static final String FUNCTION_FIX = "}";

	public static double calRebate(String formula, double rebate) throws ScriptException, NoSuchMethodException {
		LogUtil.writeLog("返佣公式=" + formula);
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
		Compilable compEngine = (Compilable) engine;
		CompiledScript script = compEngine.compile(FUNCTION_PRE + formula + FUNCTION_FIX);
		script.eval();
		Invocable invoke = (Invocable) engine;
		Object num = invoke.invokeFunction(FUNCTION_NAME, rebate);
		Double result = Double.valueOf(num.toString());
		LogUtil.writeLog("返佣结果=" + result);
		return result < 0 ? 0 : result;
	}

	public static void main(String[] args) throws NoSuchMethodException, ScriptException {
		System.out.println(calRebate("rebate-0.5", 0.2));
	}
}
