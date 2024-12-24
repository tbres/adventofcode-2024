package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;

public class DayTwentyFour {
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DayTwentyFour.class.getResource("input-day24.txt").toURI()));
		Map<String, Integer> values = new HashMap<>();
		Map<String, Operation> operations = new HashMap<>();
		parse(lines, values, operations);

		// System.out.println("digraph {");
		// operations.forEach((k, v)-> {
		// 	System.out.println("\t" + k + " -> " + v.inputA );
		// 	System.out.println("\t" + k + " -> " + v.inputB );
		// });
		// System.out.println("}");

		System.out.println("Part 1: " + partOne(values, operations));

		System.out.println("Part 2: ");
	}

	private static long partOne(Map<String, Integer> initialValues, Map<String, Operation> operations) {
		Map<String, Integer> values = new HashMap<>(initialValues);

		Set<String> outputs = getOutputs(operations);
		for (String output: outputs) {
			solve(output, values, operations);
		}

		String result = "";
		for (int i = 0; i < 100; i++) {
			String z = "z";
			if (i < 10) {
				z += "0";
			}
			z += i;

			Integer zi = values.get(z);
			if (zi != null) {
				result = zi + result;
			} else {
				break;
			}
		}

		

		return Long.parseLong(result, 2);
	}

	private static Integer solve(String output, Map<String, Integer> values, Map<String, Operation> operations) {
		if (values.containsKey(output)) {
			return values.get(output);
		}

		Operation operation = operations.get(output);
		Integer inputA = solve(operation.inputA, values, operations);
		Integer inputB = solve(operation.inputB, values, operations);
		Integer result = operation.operation.apply(inputA, inputB);

		values.put(output, result);
		return result;
	}

	private static Set<String> getOutputs(Map<String, Operation> operations) {
		Set<String> outputs = new TreeSet<>();
		for (Operation operation: operations.values()) {
			outputs.add(operation.output);
		}
		for (Operation operation: operations.values()) {
			outputs.remove(operation.inputA);
			outputs.remove(operation.inputB);
		}
		return outputs;
	}

	private static void parse(List<String> lines, Map<String, Integer> values, Map<String, Operation> operations) {
		for (String line: lines) {
			if (line.contains(":")) {
				String[] split = line.split(":");
				values.put(split[0], Integer.valueOf(split[1].trim()));
			} else if (!line.trim().isEmpty()) {
				String[] split = line.split("\\s");
				String inputA = split[0];
				String operation = split[1];
				String inputB = split[2];
				String output = split[4];

				if ("AND".equals(operation)) {
					operations.put(output, new Operation(AND, inputA, inputB, output));
				} else if ("OR".equals(operation)) {
					operations.put(output, new Operation(OR, inputA, inputB, output));
				} else if ("XOR".equals(operation)) {
					operations.put(output, new Operation(XOR, inputA, inputB, output));
				} else {
					throw new IllegalArgumentException("Unknown operation: " + operation); 
				}
			}
		}
	}

	private static final BiFunction<Integer, Integer, Integer> AND = (a, b) -> (a & b);
	private static final BiFunction<Integer, Integer, Integer> OR = (a, b) -> (a | b);
	private static final BiFunction<Integer, Integer, Integer> XOR = (a, b) -> (a ^ b);

	private static record Operation(BiFunction<Integer, Integer, Integer> operation, String inputA, String inputB, String output) {}
}
