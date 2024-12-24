package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DayTwentyFour {
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DayTwentyFour.class.getResource("input-day24.txt").toURI()));
		Map<String, Integer> values = new HashMap<>();
		Map<String, Operation> operations = new HashMap<>();
		parse(lines, values, operations);

		printDotGraphviz(operations);

		System.out.println("Part 1: " + partOne(new HashMap<>(values), operations));

		System.out.println("Part 2: "+ partTwo(new HashMap<>(values), operations));
	}

	private static long partOne(Map<String, Integer> values, Map<String, Operation> operations) {
		Set<String> outputs = getOutputs(operations);
		for (String output: outputs) {
			solve(output, values, operations);
		}
		
		String result = extractNumber("z", values);
		return Long.parseLong(result, 2);
	}
	
	/**
	 * Had to solve this one visually (using `dot` to show the graph) and some guesswork!
	 */
	private static String partTwo(Map<String, Integer> values, Map<String, Operation> operations) {
		String valueX = extractNumber("x", values);
		String valueY = extractNumber("y", values);
		String expectedResult = Long.toBinaryString(Long.parseLong(valueX, 2) + Long.parseLong(valueY, 2));
		
		Map<String, String> switched = Map.of(
			"z13", "vcv",
			"z19", "vwp",
			"z25", "mps",
			"cqm", "vjv"
		);

		switched.forEach((a, b) -> {
			operations.put(a, operations.put(b, operations.get(a)));
		});

		for (int i = 0; i < 100; i++) {
			String output = String.format("z%02d", i);
			if (operations.containsKey(output)) {
				int result = solve(output, values, operations);

				int expected = Integer.parseInt("" + expectedResult.charAt(expectedResult.length()- i -1));
				if (result != expected) {
					System.out.println("Problem at " + output +  ": expected="+expected + " result=" + result);
				}

				if (operations.get(output).operation != XOR) {
					System.out.println("Unexpected operation: " + operations.get(output));
				}
			}
		}

		String result = extractNumber("x", values);

		System.out.println("x =      " + valueX);
		System.out.println("y =      " + valueY);
		System.out.println("x + y = " + expectedResult);
		System.out.println("actual= " + result);

		return switched.entrySet().stream()
			.flatMap(entry -> Stream.of(entry.getKey(), entry.getValue()))
			.sorted()
			.collect(Collectors.joining(","));
	}


	private static String extractNumber(String name, Map<String, Integer> values) {
		String result = "";
		for (int i = 0; i < 100; i++) {
			String n = String.format(name + "%02d", i);
			
			Integer ni = values.get(n);
			if (ni != null) {
				result = ni + result;
			}
		}
		return result;
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
	
	private static void printDotGraphviz(Map<String, Operation> operations) {
		System.out.println("digraph {");
		operations.forEach((k, v)-> {
			if (v.operation == AND) {
				System.out.println("\t" + k + " [shape=box, color=blue]");
			} else if (v.operation == OR) {
				System.out.println("\t" + k + " [shape=house, color=red]");
			} else {
				System.out.println("\t" + k + " [shape=egg]");
			}
			System.out.println("\t" + k + " -> " + v.inputA );
			System.out.println("\t" + k + " -> " + v.inputB );
		});
		System.out.println("}");
	}
}
