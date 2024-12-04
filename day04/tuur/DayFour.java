package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DayFour {

	private static final Character[] XMAS = new Character[] { 'X', 'M', 'A', 'S' };

	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DayFour.class.getResource("input-day04.txt").toURI()));

		Map<Coord, Character> map = parseMap(lines);
		final int sizeX = lines.get(0).length();
		final int sizeY = lines.size();

		System.out.println("Part 1: " + partOne(map, sizeX, sizeY));

		System.out.println("Part 2: " + partTwo(map, sizeX, sizeY));

	}

	public static long partOne(Map<Coord, Character> map, int sizeX, int sizeY) {
		long count = 0l;
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				Coord current = new Coord(x, y);
				if (map.get(current) == 'X') {
					count += wordSearch(map, sizeX, sizeY, current);
				}
			}
		}
		return count;
	}

	public static long wordSearch(Map<Coord, Character> map, int sizeX, int sizeY, final Coord start) {
		long count = 0l;
		for (Coord direction : directions()) {
			Coord current = start;

			for (int i = 1; i <= 3; i++) {
				current = new Coord(current.getX() + direction.getX(), current.getY() + direction.getY());

				if (current.getX() >= 0 && current.getX() < sizeX && current.getY() >= 0 && current.getY() < sizeY) {
					if (map.get(current) != XMAS[i]) {
						break;
					} else if (i == 3) {
						count++;
					}
				}
			}
		}
		return count;
	}

	public static long partTwo(Map<Coord, Character> map, int sizeX, int sizeY) {
		long count = 0l;
		for (int x = 1; x < sizeX - 1; x++) {
			for (int y = 1; y < sizeY - 1; y++) {
				Coord current = new Coord(x, y);
				if (map.get(current) == 'A' && masInTheShapeOfAnX(map, current)) {
					count++;
				}
			}
		}
		return count;
	}

	public static boolean masInTheShapeOfAnX(Map<Coord, Character> map, final Coord start) {
		Character a1 = map.get(new Coord(start.getX() - 1, start.getY() - 1));
		Character a2 = map.get(new Coord(start.getX() + 1, start.getY() + 1));

		boolean diagonalA = false;
		if (a1 == 'M' && a2 == 'S') {
			diagonalA = true;
		} else if (a1 == 'S' && a2 == 'M') {
			diagonalA = true;
		}

		Character b1 = map.get(new Coord(start.getX() + 1, start.getY() - 1));
		Character b2 = map.get(new Coord(start.getX() - 1, start.getY() + 1));

		boolean diagonalB = false;
		if (b1 == 'M' && b2 == 'S') {
			diagonalB = true;
		} else if (b1 == 'S' && b2 == 'M') {
			diagonalB = true;
		}

		return diagonalA && diagonalB;
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

	private static List<Coord> directions() {
		List<Coord> result = new ArrayList<>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i != 0 || j != 0) {
					result.add(new Coord(i, j));
				}
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
