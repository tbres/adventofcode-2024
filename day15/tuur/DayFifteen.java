package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;

public class DayFifteen {

	private static final Character EMPTY = '.';
	private static final Character WALL = '#';
	private static final Character BOX = 'O';
	private static final Character ROBOT = '@';
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DayFifteen.class.getResource("input-day15.txt").toURI()));
		System.out.println("Part 1: " + partOne(lines));
		System.out.println("Part 2: " + partTwo(lines));
	}

	private static long partOne(List<String> lines) {
		Map<Coord, Character> map = new HashMap<>();
		List<Character> instructions = new ArrayList<>();
		Coord robot = null;
		int sizeX = lines.get(0).length();
		int sizeY = 0;
		for (int y = 0; y < lines.size(); y++) {
			String line = lines.get(y);
			if (line.startsWith(WALL.toString())) {
				for (int x = 0; x < line.length(); x++) {
					if (line.charAt(x) == ROBOT) {
						robot = new Coord(x, y);
						map.put(robot, ROBOT);
					} else {
						map.put(new Coord(x, y), line.charAt(x));
					}
				}
				sizeY++;
			} else if (line.trim().isEmpty()) {
				// nothing
			} else {
				for (int x = 0; x < line.length(); x++) {
					instructions.add(line.charAt(x));
				}
			}
		}

		followInstructions(robot, map, sizeX, sizeY, instructions);

		long partOne = map.entrySet().stream()
			.filter(entry -> BOX.equals(entry.getValue()))
			.map(Entry::getKey)
			.mapToLong(coord -> 100 * coord.y + coord.x)
			.sum();

		return partOne;
	}

	private static long partTwo(List<String> lines) {
		Map<Coord, Character> map = new HashMap<>();
		List<Character> instructions = new ArrayList<>();
		Coord robot = null;
		int sizeX = lines.get(0).length() * 2;
		int sizeY = 0;
		for (int y = 0; y < lines.size(); y++) {
			String line = lines.get(y);
			if (line.startsWith(WALL.toString())) {
				for (int x = 0; x < line.length(); x++) {
					if (line.charAt(x) == ROBOT) {
						robot = new Coord(x * 2, y);
						map.put(robot, ROBOT);
						map.put(robot.adding(1, 0), EMPTY);
					} else if (line.charAt(x) == BOX) {
						map.put(new Coord(x * 2, y), '[');
						map.put(new Coord(x * 2 + 1, y), ']');
					} else {
						map.put(new Coord(x * 2, y), line.charAt(x));
						map.put(new Coord(x * 2 + 1, y), line.charAt(x));
					}
				}
				sizeY++;
			} else if (line.trim().isEmpty()) {
				// nothing
			} else {
				for (int x = 0; x < line.length(); x++) {
					instructions.add(line.charAt(x));
				}
			}
		}

		followInstructions(robot, map, sizeX, sizeY, instructions);

		long partTwo = map.entrySet().stream()
		 	.filter(entry -> entry.getValue() == '[')
		 	.map(Entry::getKey)
		 	.mapToLong(coord -> 100 * coord.y + coord.x)
		 	.sum();

		return partTwo;
	}

	private static void followInstructions(Coord startPosition, Map<Coord, Character> map, int sizeX, int sizeY, List<Character> instructions ) {
		Coord robot = startPosition;
		for (Character direction : instructions) {
			Coord next = move(robot, direction);
			if (push(next, direction, map)) {
				map.put(robot, EMPTY);
				map.put(next, ROBOT);
				robot = next;

				// System.out.println();
				// System.out.println("Move " + direction);
				// print(map, sizeX, sizeY);
			}
		}
	}

	/**
	 * @param position
	 * @param direction
	 * @param map
	 * @return true if the next spot is empty, false otherwise
	 */
	private static boolean push(Coord position, Character direction, Map<Coord, Character> map) {
		Character value = map.get(position);

		if (EMPTY.equals(value)) {
			return true;
		} else if (WALL.equals(value)) {
			return false;
		} else if (BOX.equals(value)) {
			Coord next = move(position, direction);
			if (push(next, direction, map)) {
				map.put(position, EMPTY);
				map.put(next, value);
				return true;
			}
		} else if ('[' == value || ']' == value) { // Handling Boxes for part 2
			if ('<' == direction || '>'  == direction) { // Move horizontally
				Coord next = move(position, direction);
				Coord nexter = move(next, direction);
				if (push(nexter, direction, map)) {
					map.put(nexter, map.get(next));
					map.put(next, map.get(position));
					map.put(position, EMPTY);
					
					return true;
				}
			} else { // Move vertically
				Coord left, right; // figure out the left and right edge of the box
				if (value == '[') {
					left = position;
					right = position.right();
				} else {
					left = position.left();
					right = position;
				}
				
				if (canBoxMoveUpOrDown(left, right, direction, map)) {
					moveBoxUpOrDown(left, right, direction, map);

					return true;
				}
			}
		} else {
			throw new IllegalStateException("Unknown item at location " + position);
		}
		return false;
	}

	private static boolean canBoxMoveUpOrDown(Coord left, Coord right, Character direction, Map<Coord, Character> map) {
		Coord nextLeft = move(left, direction);
		Coord nextRight = move(right, direction);

		if (map.get(nextLeft) == '#' || map.get(nextRight) == '#') {
			return false;
		} else if (map.get(nextLeft) == '.' && map.get(nextRight) == '.') {
			return true;
		} else if (map.get(nextLeft) == '[' && map.get(nextRight) == ']') {
			return canBoxMoveUpOrDown(nextLeft, nextRight, direction, map);
		} else {
			boolean result = true;
			if (map.get(nextLeft) == ']') {
				result &= canBoxMoveUpOrDown(nextLeft.left(), nextLeft, direction, map);
			}
			if (map.get(nextRight) == '[') {
				result &= canBoxMoveUpOrDown(nextRight, nextRight.right(), direction, map);
			}
			return result;
		}
	}

	private static void moveBoxUpOrDown(Coord left, Coord right, Character direction, Map<Coord, Character> map) {
		Coord nextLeft = move(left, direction);
		Coord nextRight = move(right, direction);

		if (map.get(nextLeft) == '[' && map.get(nextRight) == ']') {
			moveBoxUpOrDown(nextLeft, nextRight, direction, map);
		} else {
			if (map.get(nextLeft) == ']') {
				moveBoxUpOrDown(nextLeft.left(), nextLeft, direction, map);
			}
			if (map.get(nextRight) == '[') {
				moveBoxUpOrDown(nextRight, nextRight.right(), direction, map);
			}
		}

		map.put(nextLeft, '[');
		map.put(nextRight, ']');
		map.put(left, EMPTY);
		map.put(right, EMPTY);
	}

	private static Coord move(Coord position, Character direction) {
		switch(direction) {
			case '>': return position.adding(1, 0);
			case '<': return position.adding(-1, 0); 
			case '^': return position.adding(0, -1); 
			case 'v': return position.adding(0, 1);
			default: throw new IllegalArgumentException("Unknown direction: " + direction);
		}
	}
	
	private static void print(Map<Coord, Character> map, int sizeX, int sizeY) {
		for (int y = 0; y < sizeY; y++) {
			StringBuilder sb = new StringBuilder();
			for (int x = 0; x < sizeX; x++) {
				sb.append(map.get(new Coord(x, y)));
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

		public Coord left() {
			return new Coord(x - 1, y);
		}

		public Coord right() {
			return new Coord(x + 1, y);
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
