package ch.puzzle.sample;

public class ClassA implements RunnableClass {

	@Override
    @AnnotationA(enabled=false)
	public void run(){
		System.out.println("ClassA.run() called");
		ClassB classb = new ClassB();
		classb.concreteMethodInAbstractClass();
		classb.run();
        classb.run();

        classb.concreteMethodInAbstractClass();
        classb.run();

		int code = EnumA.VALUE1.getCode();
		System.out.println("Code of EnumA.VALUE1 is " + code);

	}

}
