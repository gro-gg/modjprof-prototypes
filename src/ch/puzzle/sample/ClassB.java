package ch.puzzle.sample;

public class ClassB {

	public void run(){
		System.out.println("ClassB.run() called");
		ClassC.run();
	}
	
}
