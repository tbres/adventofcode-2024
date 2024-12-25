package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DayTwentyFive {
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DayTwentyFive.class.getResource("input-day25.txt").toURI()));

		List<Map<Integer,Integer>> locks = new ArrayList<>();
		List<Map<Integer,Integer>> keys = new ArrayList<>();

		for (int i = 0; i < lines.size(); i += 8) {
			if ("#####".equals(lines.get(i))) { //LOCK
				Map<Integer, Integer> lock = new HashMap<>();
				for (int j = i + 1; j < i + 6; j++) {
					String line = lines.get(j);
					for (int k = 0; k < line.length(); k++) {
						if (line.charAt(k) == '#') {
							lock.merge(k, 1, Integer::sum);
						}
					}
				}
				locks.add(lock);
			} else { // KEY
				Map<Integer, Integer> key = new HashMap<>();
				for (int j = i + 5; j > i; j--) {
					String line = lines.get(j);
					for (int k = 0; k < line.length(); k++) {
						if (line.charAt(k) == '#') {
							key.merge(k, 1, Integer::sum);
						}
					}
				}
				keys.add(key);
			}
		}

		int partOne = 0;
		for (Map<Integer, Integer> lock : locks) {
			for (Map<Integer, Integer> key : keys) {
				boolean fits = true;
				for (int i = 0; i < 5; i++) {
					if (lock.getOrDefault(i, 0) + key.getOrDefault(i, 0) > 5) {
						fits = false;
					}
				}
				if (fits) {
					partOne++;
				}
			}
		}


		System.out.println("Part 1: "+ partOne);

		System.out.println("Part 2: ");
	}
}
