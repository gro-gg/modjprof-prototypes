package ch.puzzle.transformed;

import ch.puzzle.sample.ClassB;

public class ClassA {

	public void run(){
//		System.out.println(" > ClassA.run()");
		System.out.println("ClassA.run() called");
		ClassB classb = new ClassB();
		classb.run();
//		System.out.println(" < ClassA.run()");
	}

}
