package it.unive.dais.cevid.datadroid.lib.experimental;


public @interface Dlm {
	String[] secrecy() default "";
	String[] integrity() default "";
}
/*
@Dlm(secrecy	= "A -> B, C + D -> E",
	 integrity 	= "A <- C, E * B <- D, F")
static class Vector<T extends Serializable> {
	
	@Dlm(secrecy	= "A -> B, C",
		 integrity 	= "A <- C, E",
		 signature  = "a:{L1} b:{L2} -> L3") 
	public int m(int a, String b) {
		declassify(a);
		return n;
	}
}*/