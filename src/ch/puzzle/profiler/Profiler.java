package ch.puzzle.profiler;

import java.lang.instrument.Instrumentation;

public class Profiler {
	public static void premain(String agentArgs, Instrumentation inst) {
		System.out.println("premain");
		inst.addTransformer(new MethodCallClassFileTransformer());
	}
}
