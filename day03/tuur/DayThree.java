package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DayThree {

	private static final String REGEX = "mul\\((?<first>\\d{1,3}),(?<second>\\d{1,3})\\)";

	private static final Pattern PATTERN = Pattern.compile(REGEX);
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DayThree.class.getResource("input-day03.txt").toURI()));

		System.out.println("Part 1: " + lines.stream().mapToLong(DayThree::accumalate).sum());

		//don't work line by line, the enabled/disabled state carries over!
		String memory = lines.stream().collect(Collectors.joining()); 
		System.out.println("Part 2: " + accumalate(clean(memory)));
	}

	private static long accumalate(String input) {
		Matcher matcher = PATTERN.matcher(input);
		long accumalator = 0l;
		while (matcher.find()) {
			accumalator += Long.parseLong(matcher.group("first")) * Long.parseLong(matcher.group("second"));
		}
		return accumalator;
	}

	private static String clean(String input) {
		final int end = input.length();
		
		StringBuilder result = new StringBuilder();
		boolean enabled = true;
		int current = 0;
		while(current < end) {
			if (enabled) {
				int index = input.indexOf("don't()", current);
				if (index > 0) {
					result.append(input.substring(current, index));
					enabled = false;
					current = index;
				} else {
					result.append(input.substring(current, end));
					current = end;
				}
			} else {
				int index = input.indexOf("do()", current);
				if (index > 0) {
					enabled = true;
					current = index;
				} else {
					current = end;
				}
			}
		}

		return result.toString();
	}
}
