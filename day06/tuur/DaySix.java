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

public class DaySix {
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DaySix.class.getResource("input-day06.txt").toURI()));
		Map<Coord, Character> map = new HashMap<>();
		Coord startingPosition = null;
		for (int y = 0; y < lines.size(); y++) {
			String line = lines.get(y);
			for (int x = 0; x < line.length(); x++) {
				char shape = line.charAt(x);
				if ('#' ==  shape) {
					map.put(new Coord(x, y), '#');
				} else if ('^' == shape) {
					startingPosition = new Coord(x, y);
				}
			}
		}
		final int sizeX = lines.get(0).length();
		final int sizeY = lines.size();

		List<Step> path = new ArrayList<>();
		walk(path, startingPosition, map, sizeX, sizeY);
		long visitedCoordinates = path.stream().map(Step::coord).distinct().count();

		System.out.println("Part 1: " + visitedCoordinates);

		Set<Coord> obstructions = getObstructions(map, startingPosition, sizeX, sizeY, path);
	
		System.out.println("Part 2: " + obstructions.size());
		
	}
		
	private static boolean walk(List<Step> path, Coord start, Map<Coord, Character> map, int sizeX, int sizeY) {
		Coord current = start;
		Facing facing = Facing.up;
		
		while (0 <= current.getX() && current.getX() < sizeX
			&& 0 <= current.getY() && current.getY() < sizeY) {
			
			Step step = new Step(current, facing);
			if (path.contains(step)) {
				return true; // LOOP detected
			}
			
			path.add(step);

			Coord next;
			if (Facing.up.equals(facing)) {
				next = new Coord(current.getX(), current.getY() - 1);
			} else if (Facing.down.equals(facing)) {
				next = new Coord(current.getX(), current.getY() + 1);
			} else if (Facing.right.equals(facing)) {
				next = new Coord(current.getX() + 1, current.getY());
			} else {
				next = new Coord(current.getX() - 1, current.getY());
			}

			if (map.getOrDefault(next, '.') == '#') {
				if (Facing.up.equals(facing)) {
					facing = Facing.right;
				} else if (Facing.down.equals(facing)) {
					facing = Facing.left;
				} else if (Facing.right.equals(facing)) {
					facing = Facing.down;
				} else {
					facing = Facing.up;
				}
			} else {
				current = next;
			}
		}

		return false;
	}	
	
	private static Set<Coord> getObstructions(Map<Coord, Character> map, Coord startingPosition, int sizeX, int sizeY, List<Step> path) {
		Set<Coord> obstructions = new HashSet<>();
		for (int i = 1; i < path.size(); i++) {
			Coord obstruction = path.get(i).coord();

			if (!obstruction.equals(startingPosition) 
				&& !obstructions.contains(obstruction)) {

				map.put(obstruction, '#');

				List<Step> newPath = new ArrayList<>();
				if (walk(newPath, startingPosition, map, sizeX, sizeY)) {
					obstructions.add(obstruction);
				}

				map.remove(obstruction);
			}	
		}
		return obstructions;
	}

	private static enum Facing {
		up, down, right, left;
	}

	private static final class Step {
		final Coord start;
		final Facing direction;

		public Step(Coord start, Facing direction) {
			this.start = start;
			this.direction = direction;
		}

		public Coord coord() {
			return start;
		}

		@Override
		public int hashCode() {
			return Objects.hash(start.x, start.y, direction);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Step) {
				Step other = (Step) obj;
				return start.x == other.start.x && start.y == other.start.y && direction == other.direction;
			}
			return false;
		}

		@Override
		public String toString() {
			return start + " " + direction;
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
