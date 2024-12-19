package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DayNineteen {
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DayNineteen.class.getResource("input-day19.txt").toURI()));
		List<String> towels = Arrays.stream(lines.get(0).split(","))
			.map(String::trim)
			.collect(Collectors.toList());
		List<String> designs = lines.stream()
			.filter(line -> !line.isBlank())
			.filter(line -> !line.contains(","))
			.collect(Collectors.toList());
		
		long partOne = designs.stream()
			.filter(design -> partOne(towels, design))
			.count();
		
		System.out.println("Part 1: "  + partOne);


		int maxTowelLength = towels.stream().mapToInt(String::length).max().getAsInt();
		Map<String, Long> SEEN = new HashMap<>();
		long partTwo = designs.stream()
			.mapToLong(design -> partTwo(new HashSet<>(towels), maxTowelLength, design, SEEN))
			.sum();

		System.out.println("Part 2: " + partTwo);
	}

	private static boolean partOne(List<String> towels, String design) {
		for (String towel : towels) {
			if (design.startsWith(towel)) {
				if (design.length() == towel.length()) {
					return true;
				} else if(partOne(towels, design.substring(towel.length()))){
					return true;
				}
			}
		}
		return false;
	}

	private static long partTwo(Set<String> towels, int maxTowelLength, String design, Map<String, Long> SEEN) {
		long count = 0l;
		for (int i = 1; i <= design.length() && i <= maxTowelLength; i++) {
			if (towels.contains(design.substring(0, i))) {
				if (design.length() == i) {
					count++;
				} else {
					String subDesign = design.substring(i);
					if (SEEN.containsKey(subDesign)) {
						count += SEEN.get(subDesign);
					} else {
						long result = partTwo(towels, maxTowelLength, subDesign, SEEN);
						SEEN.put(subDesign, result);
						count += result;
					}
				}
			}
		}
		return count;
	}
}
