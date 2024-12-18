package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayOne {
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DayOne.class.getResource("input-day01.txt").toURI()));

		List<Integer> left = new ArrayList<>();
		List<Integer> right = new ArrayList<>();
		for(String line: lines) {
			String[] split = line.split("\\s+");
			left.add(Integer.parseInt(split[0]));
			right.add(Integer.parseInt(split[1]));
		}
		Collections.sort(left);
		Collections.sort(right);

		System.out.println("Part 1: " + partOne(left, right));

		System.out.println("Part 2: " + partTwo(left, right));
	}

	private static long partOne(List<Integer> first, List<Integer> second) {
		long result = 0l;
		for (int i = 0; i < first.size(); i++) {
			result += Math.abs(first.get(i) - second.get(i));
		}
		return result;
	}

	private static long partTwo(List<Integer> first, List<Integer> second) {
		Map<Integer, Long> occurences = new HashMap<>();
		for (Integer locationId : second) {
			occurences.merge(locationId, 1L, (existing, value) -> existing + 1L);
		}
		
		long result = 0l;
		for (Integer locationId : first) {
			result += locationId * occurences.getOrDefault(locationId, 0L);
		}
		return result;
	}
}
