package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class DaySeven {
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DaySeven.class.getResource("input-day07.txt").toURI()));
		List<List<Long>> equations = new ArrayList<>();
		lines.forEach(line-> {
			String[] split = line.split("\\s");
			List<Long> equation = new ArrayList<>();
			for(String number : split) {
				if (number.endsWith(":")) {
					number = number.substring(0, number.length() - 1);
				}
				equation.add(Long.valueOf(number));
			}
			equations.add(equation);
		});

		Long partOne = equations.stream()
			.filter(line -> solve(line, List.of(DaySeven::sum, DaySeven::multiply)))
			.mapToLong(list -> list.get(0))
			.sum();

		System.out.println("Part 1: " + partOne);

		Long partTwo = equations.stream()
			.filter(line -> solve(line, List.of(DaySeven::sum, DaySeven::multiply, DaySeven::concat)))
			.mapToLong(list -> list.get(0))
			.sum();

		System.out.println("Part 2: " + partTwo);

	}

	private static Long sum(Long previous, Long current) {
		return previous + current;
	}
	
	private static Long multiply(Long previous, Long current) {
		return previous * current;
	}

	private static Long concat(Long previous, Long current) {
		return Long.valueOf("" + previous + current);
	}

	private static boolean solve(List<Long> equation, List<BiFunction<Long, Long, Long>> operators) {

		List<Long> previousValues = List.of(equation.get(1));
		for (int i = 2; i < equation.size(); i++) {
			Long current = equation.get(i);
			
			List<Long> next = new ArrayList<>();
			for (Long previous : previousValues) {
				for (BiFunction<Long, Long, Long> operator : operators) {
					next.add(operator.apply(previous, current));
				}
			}
			previousValues = next;
		}

		Long expected = equation.get(0);
		for (Long result : previousValues) {
			if (expected.equals(result)) {
				return true;
			}
		}

		return false;
	}
}
