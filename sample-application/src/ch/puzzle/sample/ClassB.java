package ch.puzzle.sample;

public class ClassB extends AbstractClassB {

	public void run(){
		System.out.println("ClassB.run() called");
		ClassC.run("", 0, 0);
		System.out.println("ClassB.run() finished");
	}
	
}
