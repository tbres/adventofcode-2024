package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DayEight {
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DayEight.class.getResource("input-day08.txt").toURI()));
		Map<Character, List<Coord>> antennas = parseMap(lines);
		final int sizeX = lines.get(0).length();
		final int sizeY = lines.size();
		
		System.out.println("Part 1: " + partOne(antennas, sizeX, sizeY));
	
		System.out.println("Part 2: " + partTwo(antennas, sizeX, sizeY));
	}
		
	private static long partOne(Map<Character, List<Coord>> antennas, int sizeX, int sizeY) {
		Set<Coord> antinodes = new HashSet<>();

		antennas.forEach((character, locations) -> {
			for (int i = 0; i < locations.size(); i++) {
				Coord first = locations.get(i);

				for (int j = i + 1; j < locations.size(); j++) {
					Coord second = locations.get(j);

					int dX = first.x - second.x;
					int dY = first.y - second.y;

					Coord antinodeOne = second.adding(-dX, -dY);
					if (inRange(antinodeOne, 0, sizeX, 0, sizeY)) {
						antinodes.add(antinodeOne);
					}

					Coord antinodeTwo = first.adding(dX, dY);
					if (inRange(antinodeTwo, 0, sizeX, 0, sizeY)) {
						antinodes.add(antinodeTwo);
					}
				}
			}
		});
 
		return antinodes.size();
	}

	private static long partTwo(Map<Character, List<Coord>> antennas, int sizeX, int sizeY) {
		Set<Coord> antinodes = new HashSet<>();

		antennas.forEach((character, locations) -> {
			if (locations.size() > 1) {
				antinodes.addAll(locations);
			}

			for (int i = 0; i < locations.size(); i++) {
				Coord first = locations.get(i);

				for (int j = i + 1; j < locations.size(); j++) {
					Coord second = locations.get(j);

					int dX = first.x - second.x;
					int dY = first.y - second.y;

					Coord antinodeOne = second.adding(-dX, -dY);
					while (inRange(antinodeOne, 0, sizeX, 0, sizeY)) {
						antinodes.add(antinodeOne);
						antinodeOne = antinodeOne.adding(-dX, -dY);
					}

					Coord antinodeTwo = first.adding(dX, dY);
					while (inRange(antinodeTwo, 0, sizeX, 0, sizeY)) {
						antinodes.add(antinodeTwo);
						antinodeTwo = antinodeTwo.adding(dX, dY);
					}
				}
			}
		});
 
		return antinodes.size();
	}

	private static boolean inRange(Coord coord, int minX, int sizeX, int minY, int sizeY) {
		return minX <= coord.x && coord.x < sizeX 
			&& minY <= coord.y && coord.y < sizeY;
	}
		
	private static Map<Character, List<Coord>> parseMap(List<String> lines) {
		Map<Character, List<Coord>> antennas = new HashMap<>();
		for (int y = 0; y < lines.size(); y++) {
			String line = lines.get(y);
			for (int x = 0; x < line.length(); x++) {
				Character shape = line.charAt(x);
				Coord coord = new Coord(x, y);
				if ('.' != shape) {
					antennas
						.computeIfAbsent(shape, ArrayList::new)
						.add(coord);
				}
			}
		}
		return antennas;
	}

	private static final class Coord {
		final int x, y;

		public Coord(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public Coord adding (int dX, int dY) {
			return new Coord(x + dX, y + dY);
		}

		@Override
		public String toString() {
			return "(" + x + ", " + y + ")";
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Coord) {
				Coord other = (Coord) obj;
				return x == other.x && y == other.y;
			}
			return false;
		}
	}
}
