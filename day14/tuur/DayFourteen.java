package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DayFourteen {
	
	private static final String REGEX = "^p=(?<pX>\\d+),(?<pY>\\d+)\\sv=(?<vX>-?\\d+),(?<vY>-?\\d+)$";

	private static final Pattern PATTERN = Pattern.compile(REGEX);

	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DayFourteen.class.getResource("input-day14.txt").toURI()));
		
		final int sizeX = 101;
		final int sizeY = 103;
		
		final List<Coord> velocities = new ArrayList<>();
		final List<Coord> initialPositions = new ArrayList<>();
		for (String line : lines) {
			Matcher matcher = PATTERN.matcher(line);
			if (matcher.matches()) {
				int posX = Integer.parseInt(matcher.group("pX"));
				int posY = Integer.parseInt(matcher.group("pY"));
				int vX = Integer.parseInt(matcher.group("vX"));
				int vY = Integer.parseInt(matcher.group("vY"));

				initialPositions.add(new Coord(posX, posY));
				velocities.add(new Coord(vX, vY));
			}
		}

		List<Coord> positions = initialPositions;
		for (int time = 1; time <= 100; time++) {
			positions = move(positions, velocities, sizeX, sizeY);
		}
		System.out.println("Part 1: " + safetyFactor(positions, sizeX, sizeY));

		positions = initialPositions;
		int time = 0;
		for (time = 1; time <= 1_000_000; time++) {
			positions = move(positions, velocities, sizeX, sizeY);

			Set<Coord> uniquePositions = new HashSet<>(positions);
			if (positions.size() == uniquePositions.size()) { // How are you supposed to guess this?
				print(positions, sizeX, sizeY);				
				break;
			}
		}
		System.out.println("Part 2: After " + time +  " seconds");

	}


	private static List<Coord> move(List<Coord> initialPositions, List<Coord> velocities, int sizeX, int sizeY) {
		List<Coord> positions = new ArrayList<>();

		for (int i = 0; i < initialPositions.size(); i++) {
			Coord pos = initialPositions.get(i);
			Coord vel = velocities.get(i);

			int newX = Math.floorMod(pos.x + vel.x, sizeX);
			int newY = Math.floorMod(pos.y + vel.y, sizeY);

			positions.add(new Coord(newX, newY));
		}
		
		return positions;
	}

	private static long safetyFactor(List<Coord> positions, int sizeX, int sizeY) {
		long q1 = 0;
		long q2 = 0;
		long q3 = 0;
		long q4 = 0;

		for (Coord pos : positions) {
			if (pos.x < sizeX / 2) {
				if (pos.y  < sizeY / 2) {
					q1++;
				} else if (pos.y > sizeY / 2) {
					q3 ++;
				}
			} else if (pos.x > sizeX / 2) {
				if (pos.y  < sizeY / 2) {
					q2++;
				} else if (pos.y > sizeY / 2) {
					q4++;
				}
			}
		}

		return q1 * q2 * q3 * q4;
	}

	private static void print(List<Coord> positions, int sizeX, int sizeY) {

		System.out.println();

		Map<Coord, Integer> locations = positions.stream().collect(Collectors.toMap(coord -> coord, coord -> 1, Integer::sum));

		for (int y = 0; y < sizeY; y++) {
			StringBuilder sb = new StringBuilder();

			for (int x = 0; x < sizeX; x++) {
				Coord c = new Coord(x, y);
				if (locations.containsKey(c)) {
					sb.append(locations.get(c));
				} else {
					sb.append(".");
				}
			}

			System.out.println(sb.toString());
		}		
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
