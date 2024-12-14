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

public class DayTwelve {
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DayTwelve.class.getResource("input-day12.txt").toURI()));
		Map<Coord, Character> map = parseMap(lines);
		int sizeX = lines.get(0).length();
		int sizeY = lines.size();
		

		// find all regions, nothing fancy just a collect neighbours of the same type
		// calculate perimeter: iterate over all cells and check it's neighbours
		System.out.println("Part 1: " + partOne(map, sizeX, sizeY));

		System.out.println("Part 2: ");

	}

	public static long partOne(Map<Coord, Character> map, int sizeX, int sizeY) {
		List<Set<Coord>> regions = new ArrayList<>();

		Set<Coord> mapped = new HashSet<>();
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				final Coord current = new Coord(x, y);
				final Character name = map.get(current);

				if (!mapped.contains(current)) {
					Set<Coord> region = grow(map, sizeX, sizeY, current);
					regions.add(region);
					mapped.addAll(region);
				}
			}
		}

		return calculatePrize(map, regions);
	}

	public static Set<Coord> grow(Map<Coord, Character> map, int sizeX, int sizeY, Coord start) {
		final Set<Coord> region = new HashSet<>();
		region.add(start);
		final Character name = map.get(start);

		Set<Coord> todo = start.neighbours();
		while (!todo.isEmpty()) {
			
			Set<Coord> next = new HashSet<>();

			for (Coord coord : todo) {
				if (inRange(coord, 0, sizeX, 0, sizeY)
					&& name.equals(map.get(coord))
					&& !region.contains(coord)) {
					
					region.add(coord);
					next.addAll(coord.neighbours());
				}
			}
			todo = next;
		}

		return region;
	}




	private static long calculatePrize(Map<Coord, Character> map, List<Set<Coord>> regions) {
		long total = 0l;
		System.out.println();
		for (Set<Coord> region: regions) {
			long size = region.size();
			long perimeter = 0l;

			for (Coord coord: region) {
				 perimeter += coord.neighbours().stream()
				 	.filter(c -> !region.contains(c))
					.count();
			}
			long prize = size * perimeter;
			// System.out.println("Region: " + region);
			// System.out.println(" " + map.get(region.iterator().next()) + " -> " + size + " * " + perimeter + " = " + prize );
			
			total+=prize;
		}

		return total;
	}

	private static long calculateRegionPrize(Set<Coord> region) {
		long total = 0l;
		
		for (Coord side : List.of(new Coord(1,0), new Coord (0,1), new Coord(-1, 0), new Coord(0, -1))) {

			Set<Coord> edges = new HashSet<>();
			for (Coord plot: region) {
				Coord neighbour = plot.adding(side.x, side.y);
				if (!region.contains(neighbour)) {
					edges.add(plot);
				}
			}

			
		}

		return total;
	}

	private static boolean inRange(Coord coord, int minX, int sizeX, int minY, int sizeY) {
		return minX <= coord.x && coord.x < sizeX 
			&& minY <= coord.y && coord.y < sizeY;
	}

	private static Map<Coord, Character> parseMap(List<String> lines) {
		Map<Coord, Character> result = new HashMap<>();
		for (int y = 0; y < lines.size(); y++) {
			String line = lines.get(y);
			for (int x = 0; x < line.length(); x++) {
				Character shape = line.charAt(x);
				result.put(new Coord(x, y), shape);

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
