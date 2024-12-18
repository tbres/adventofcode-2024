package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DayEighteen {
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DayEighteen.class.getResource("input-day18.txt").toURI()));
		List<Coord> bytes = lines.stream().map(line -> {
			String[] split = line.split(",");
			return new Coord(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
		}).collect(Collectors.toList());

		Coord start = new Coord (0,0);
		Coord end = new Coord (70, 70);

		System.out.println("Part 1: " + partOne(bytes, start, end));

		System.out.println("Part 2: " + partTwo(bytes, start, end));

	}

	public static int partOne(List<Coord> bytes, Coord start, Coord end) {
		Map<Coord, Character> memory = new HashMap<>();

		for (int i = 0; i < 1024; i++) {
			Coord fallenByte = bytes.get(i);
			memory.put(fallenByte, '#');
		}

		return pathFinding(start, end, memory).get();
	}

	public static Coord partTwo(List<Coord> bytes, Coord start, Coord end) {
		Map<Coord, Character> memory = new HashMap<>();

		for (int i = 0; i < 1024; i++) {
			Coord fallenByte = bytes.get(i);
			memory.put(fallenByte, '#');
		}

		for (int i = 1024; i < bytes.size(); i++) {
			Coord fallenByte = bytes.get(i);
			memory.put(fallenByte, '#');

			if (pathFinding(start, end, memory).isEmpty()) {
				return fallenByte;
			}
		}

		return null;
	}

	private static Optional<Integer> pathFinding(Coord start, Coord end, Map<Coord, Character> memory) {
		Map<Coord, Integer> shortestPaths = new HashMap<>();
		List<Coord> toInvestigate = new ArrayList<>();
		toInvestigate.add(start);
		shortestPaths.put(start, 0);

		while (!toInvestigate.isEmpty()) {
			List<Coord> next = new ArrayList<>();

			for (Coord coord : toInvestigate) {
				int distance = shortestPaths.get(coord) + 1; // we have already visited 'coord'
				
				for (Coord neighbour: coord.neighbours()) {
					if (0 <= neighbour.x && neighbour.x <= end.x && 0 <= neighbour.y && neighbour.y <= end.y
							&& memory.getOrDefault(neighbour, '.') != '#'
							&& shortestPaths.getOrDefault(neighbour, Integer.MAX_VALUE) > distance) {
						
						shortestPaths.put(neighbour, distance);
						next.add(neighbour);
					}
				}
			}

			toInvestigate = next;
		}

		return Optional.ofNullable(shortestPaths.get(end));
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

		public Coord left() {
			return new Coord(x - 1, y);
		}

		public Coord right() {
			return new Coord(x + 1, y);
		}

		public Coord up() {
			return new Coord(x, y - 1);
		}

		// A bit strange, the y axis runs downwards, so down is actually more...
		public Coord down() {
			return new Coord(x, y + 1);
		}

		public Coord adding (Coord other) {
			return new Coord(this.x + other.x, this.y + other.y);
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
