
public class ClassA {

	public void run(){
		System.out.println("ClassA.run() called");
		ClassB classb = new ClassB();
		classb.run();
	}

}
