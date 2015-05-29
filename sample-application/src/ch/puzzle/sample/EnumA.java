package ch.puzzle.sample;

public enum EnumA {
	VALUE1(1),
	VALUE22(2);
	
	private int code;

	private EnumA(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
}