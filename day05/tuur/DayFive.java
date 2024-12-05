package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DayFive {
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DayFive.class.getResource("input-day05.txt").toURI()));

		Map<Integer, Set<Integer>> rules = new HashMap<>();
		List<List<Integer>> updates = new ArrayList<>();
		for (String line: lines) {
			if (line.contains("|")) {
				String[] split = line.split("\\|");
				Integer key = Integer.valueOf(split[0]);
				Integer value = Integer.valueOf(split[1]);				
				rules.computeIfAbsent(key, HashSet::new).add(value);
			} else if (line.contains(",")) {
				updates.add(
					Arrays.stream(line.split(","))
						.map(Integer::valueOf)
						.collect(Collectors.toList())
				);
			}
		}
		
		Integer partOne = 0;
		Integer partTwo = 0;
		for (List<Integer> update: updates) {
			if (isValid(rules, update)) {
				partOne += update.get(update.size() / 2);
			} else {
				List<Integer> validUpdate = order(rules, update);
				partTwo += validUpdate.get(validUpdate.size() / 2);
			}
		}

		System.out.println("Part 1: " + partOne);
		System.out.println("Part 2: " + partTwo);

	}

	private static boolean isValid(Map<Integer, Set<Integer>> rules, List<Integer> update) {
		boolean valid = true;
		for (int i = 0; i < update.size(); i++) {
			Integer current = update.get(i);
			for (int j = i + 1; j < update.size(); j++) {
				Integer other = update.get(j);

				if (rules.containsKey(other) && rules.get(other).contains(current)) {
					valid = false;
					break;
				}
			}
		}
		return valid;
	} 

	private static List<Integer> order(Map<Integer, Set<Integer>> rules, List<Integer> update) {
		ArrayList<Integer> result = new ArrayList<>(update);

		while (!isValid(rules, result)) {
			for (int i = 0; i < result.size(); i++) {
				Integer current = result.get(i);
				for (int j = i + 1; j < result.size(); j++) {
					Integer other = result.get(j);

					if (rules.containsKey(other) && rules.get(other).contains(current)) {
						result.remove(j); // remove other from it's location
						result.add(i, other); // and puts it before current
					}
				}
			}
		}

		return result;
	}
}
