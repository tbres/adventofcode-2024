package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DayTwo {
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DayTwo.class.getResource("input-day02.txt").toURI()));
		List<List<Integer>> reports = lines.stream()
			.map(line -> line.split("\\s"))
			.map(
				line -> Arrays.stream(line).map(Integer::valueOf).collect(Collectors.toList())
			)
			.collect(Collectors.toList());

		System.out.println("Part 1: " + reports.stream()
												.filter(DayTwo::validReportPartOne)
												.count());
				
		System.out.println("Part 2: " + reports.stream()
												.filter(DayTwo::validReportPartTwo)
												.count());
				
	}
	
	private static boolean validReportPartTwo(List<Integer> report) {
		
		if (validReportPartOne(report)) {
			return true;
		}

		for (int i = 0; i != report.size(); i++) {
			List<Integer> subReport = new ArrayList<>(report);
			subReport.remove(i);
			if (validReportPartOne(subReport)) {
				return true;
			}
		}

		return false;
	}

	private static boolean validReportPartOne(List<Integer> report) {
		boolean increasing = report.get(0) - report.get(1) < 0;

		for (int i = 1; i < report.size(); i++) {
			int a = report.get(i-1);
			int b = report.get(i);
			if (Math.abs(a - b) < 1 || Math.abs(a-b) > 3) {
				return false;
			}
			if (increasing && a - b > 0) {
				return false;
			} else if (!increasing && a - b < 0) {
				return false;
			}
		}

		return true;
	}

}
