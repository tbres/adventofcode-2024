package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DayEleven {
	
	private static final int MAX_DEPTH_PART1 = 25;
	private static final int MAX_DEPTH_PART2 = 75;

	public static void main(String[] args) throws Exception {
		String input = Files.readString(Paths.get(DayEleven.class.getResource("input-day11.txt").toURI()));
		List<Long> stones = Arrays.stream(input.split("\\s")).map(Long::valueOf).collect(Collectors.toList());

		List<Long> partOne = stones;
		for (int i = 0; i < MAX_DEPTH_PART1; i++) {
			partOne = blink(partOne);
		}
		System.out.println("Part 1: " + partOne.size());

		Long partTwo = 0L;
		Map<Long, List<Long>> seen = new HashMap<>();
		for (Long stone : stones) {
			partTwo += recursiveBlink(stone, 0, seen);
		}
		System.out.println("Part 2: " + partTwo);
	}

	private static List<Long> blink(List<Long> input) {
		List<Long> result = new ArrayList<>();

		for (Long stone : input) {
			if (stone == 0) {
				result.add(1L);
			} else if (stone.toString().length() % 2 == 0) {
				String numberAsString = stone.toString();
				String left = numberAsString.substring(0, numberAsString.length() / 2);
				result.add(Long.valueOf(left));
				String right = numberAsString.substring(numberAsString.length() / 2);
				result.add(Long.valueOf(right));
			} else {
				result.add(stone * 2024);
			}
		}

		return result;
	}

	private static Long recursiveBlink(Long stone, int depth, Map<Long, List<Long>> counts) {
		if (depth == MAX_DEPTH_PART2) {
			return 1L;
		}

		int remainingDepth = MAX_DEPTH_PART2 - depth;

		if (counts.containsKey(stone)
				&& counts.get(stone).size() > remainingDepth 
				&& counts.get(stone).get(remainingDepth) != null) {
			// We've already computed the answer
			return counts.get(stone).get(remainingDepth);
		}

		Long result = 0L;
		
		if (stone == 0) {
			result += recursiveBlink(1L, depth + 1, counts);

		} else if (stone.toString().length() % 2 == 0) {
			String numberAsString = stone.toString();
			
			String left = numberAsString.substring(0, numberAsString.length() / 2);
			result += recursiveBlink(Long.valueOf(left), depth + 1, counts);

			String right = numberAsString.substring(numberAsString.length() / 2);
			result += recursiveBlink(Long.valueOf(right), depth + 1, counts);

		} else {
			result += recursiveBlink(stone * 2024, depth + 1, counts);
		}

		counts.computeIfAbsent(stone, i -> initializedList(MAX_DEPTH_PART2)).set(remainingDepth, result);

		return result;
	}

	private static final List<Long> initializedList(int size) {
		List<Long> result = new ArrayList<>(size);
		for (int i = 0; i <= size; i++) {
			result.add(null);
		}
		return result;
	}
}
