package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DayTwenty {
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DayTwenty.class.getResource("input-day20.txt").toURI()));

		Coord start = null;
		Coord end = null;
		Map<Coord, Character> result = new HashMap<>();
		for (int y = 0; y < lines.size(); y++) {
			String line = lines.get(y);
			for (int x = 0; x < line.length(); x++) {
				char shape = line.charAt(x);
				if ('S' == shape) {
					start = new Coord(x, y);
				} else if ('E' == shape) {
					end = new Coord(x, y);
				} else if ('#' == shape) {
					result.put(new Coord(x, y), '#');
				}
			}
		}



		System.out.println("Part 1: " + partOne(result, start, end, 100));

		System.out.println("Part 2: ");

	}

	private static final Set<Coord> directions = Set.of(new Coord(1, 0), new Coord(-1, 0), new Coord(0, 1), new Coord (0, -1));

	private static int partOne(Map<Coord, Character> track, final Coord start, final Coord end, final int savingsThreshold) {
		Map<Coord, Integer> distances = pathFinding(track, start, end);
		Map<Coord, Integer> reverse = pathFinding(track, end, start);

		final int best = distances.get(end);

		int shortcuts = 0;

		for (Coord location: distances.keySet()) {
			final int distance = distances.get(location);

			for (Coord direction: directions) {
				Coord neighbour = location.adding(direction);

				if (track.getOrDefault(neighbour, '.') == '#') {
					Coord neighneighbour = neighbour.adding(direction);

					if (reverse.containsKey(neighneighbour)) {
						int remainingDistance = reverse.get(neighneighbour);

						int total = distance + remainingDistance + 2;

						if (total + savingsThreshold <= best) {
							// System.out.println("Found a shortcut from " + location + " to " + neighneighbour + " -> " + total);
							shortcuts++;
						}
					}
				}
			}
		}

		return shortcuts;
	}

	private static Map<Coord, Integer> pathFinding(Map<Coord, Character> track, final Coord start, final Coord end) {
		Map<Coord, Integer> shortestPaths = new HashMap<>();
		List<Coord> toInvestigate = new ArrayList<>();
		toInvestigate.add(start);
		shortestPaths.put(start, 0);

		while (!toInvestigate.isEmpty()) {
			List<Coord> next = new ArrayList<>();

			for (Coord coord : toInvestigate) {
				int distance = shortestPaths.get(coord) + 1; // we have already visited 'coord'
				
				for (Coord neighbour: coord.neighbours()) {
					if (track.getOrDefault(neighbour, '.') != '#'
							&& shortestPaths.getOrDefault(neighbour, Integer.MAX_VALUE) > distance) {
						
						shortestPaths.put(neighbour, distance);
						next.add(neighbour);
					}
				}
			}

			toInvestigate = next;
		}

		return shortestPaths;
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
