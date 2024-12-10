package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DayTen {
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DayTen.class.getResource("input-day10.txt").toURI()));
		Map<Coord, Integer> map = parseMap(lines);
		final int sizeX = lines.get(0).length();
		final int sizeY = lines.size();

		System.out.println("Part 1: " + partOne(map, sizeX, sizeY));

		System.out.println("Part 2: " + partTwo(map, sizeX, sizeY));

	}

	private static int partOne(Map<Coord, Integer> map, int sizeX, int sizeY) {
		return map.entrySet().stream()
			.filter(entry -> entry.getValue() == 0)
			.map(entry -> walk(entry.getKey(), map, sizeX, sizeY))
			.map(list -> new HashSet<>(list)) // only unique end-points
			.mapToInt(Collection::size)
			.sum();
	}
	private static int partTwo(Map<Coord, Integer> map, int sizeX, int sizeY) {
		return map.entrySet().stream()
			.filter(entry -> entry.getValue() == 0)
			.map(entry -> walk(entry.getKey(), map, sizeX, sizeY))
			.mapToInt(Collection::size) // all possible paths
			.sum();
	}

	private static List<Coord> walk(Coord trailHead, Map<Coord, Integer> map, int sizeX, int sizeY) {
		List<Coord> ends = new ArrayList<>();

		int height = map.get(trailHead);
		for (Coord neighbour : trailHead.neighbours()) {
			int nextHeight = map.getOrDefault(neighbour, -1);
			if (nextHeight ==  height + 1) {
				if (nextHeight == 9) {
					ends.add(neighbour);
				} else {
					ends.addAll(walk(neighbour, map, sizeX, sizeY));
				}
			} 
		}

		return ends;
	}

	private static Map<Coord, Integer> parseMap(List<String> lines) {
		Map<Coord, Integer> result = new HashMap<>();
		for (int y = 0; y < lines.size(); y++) {
			String line = lines.get(y);
			for (int x = 0; x < line.length(); x++) {
				Character shape = line.charAt(x);
				Coord coord = new Coord(x, y);
				result.put(coord, Integer.valueOf(""  + shape));
			}
		}
		return result;
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

		public Set<Coord> neighbours() {
			return Set.of(
				new Coord(x + 1, y),
				new Coord(x - 1, y),
				new Coord(x, y + 1),
				new Coord(x, y - 1)
			);
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

