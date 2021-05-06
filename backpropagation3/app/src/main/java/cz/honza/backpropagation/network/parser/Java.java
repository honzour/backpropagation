package cz.honza.backpropagation.network.parser;

public class Java {
	public static final String HEADER =
			"public class Main {\n" +
			"\tprivate static double sigma(double x) {\n" +
			"\t\treturn 1.0 / (1.0 + Math.exp(-x));\n" +
			"\t}\n" +
			"\tpublic static double[] calculate(double [] input) {\n" +
			"\t\tdouble[] prev;\n" +
			"\t\tdouble[] next = new double[input.length];\n\n";
	public static final String FOOTER = "        \n" +
			"\t\treturn output;\n" +
			"\t}\n" +
			"}";
}
