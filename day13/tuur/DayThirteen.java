package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DayThirteen {
	
	private static final Pattern NUMBERS = Pattern.compile("\\d+");
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DayThirteen.class.getResource("input-day13.txt").toURI()));
		List<Machine> machines = parse(lines);

		long partOne = 0;
		for (Machine machine : machines) {
			long[] solution = solvePartOne(
				machine.priceX, machine.priceY,
				machine.aX, machine.aY,
				machine.bX, machine.bY);
			
			if (solution != null) {
				partOne += solution[0] * 3 + solution[1]; 
			}
		}
		System.out.println("Part 1: " + partOne);

		long partTwo = 0;
		for (Machine machine : machines) {
			long[] solution = solvePartTwo (
				10_000_000_000_000l + machine.priceX, 10_000_000_000_000l + machine.priceY,
				machine.aX, machine.aY,
				machine.bX, machine.bY);
			
			if (solution != null) {
				partTwo += solution[0] * 3 + solution[1]; 
			}
		}	
		System.out.println("Part 2: " + partTwo);
	}

	private static long[] solvePartOne (long priceX, long priceY, long aX, long aY, long bX, long bY) {
		for (long a = 0; a < 100; a++) {
			for (long b = 0; b < 100; b++) {
				long x = b * bX + a * aX;
				long y = b * bY + a * aY;

				if (x == priceX && y == priceY) {
					return new long[] {a, b};
				} else if (x > priceX || y > priceY) {
					break;
				}
			}
		}
		return null;
	}

	/**
	 * See: https://pressbooks.bccampus.ca/algebraintermediate/chapter/solve-systems-of-equations-using-determinants/
	 */
	private static long[] solvePartTwo (long priceX, long priceY, long aX, long aY, long bX, long bY) {

		long D = (aX * bY) - (aY * bX);
		long Da = (priceX * bY) - (priceY * bX);
		long Db = (aX * priceY) - (aY * priceX);

		if (Da % D != 0) {
			return null;
		}
		long a = Da / D;

		if (Db % D != 0) {
			return null;
		}
		long b = Db / D;
	
		return new long[] {a, b};
	}

	private static List<Machine> parse(List<String> input) {
		List<Machine> result = new ArrayList<>();

		for (int i = 0 ; i < input.size(); i+=4) {
			Matcher buttonA = NUMBERS.matcher(input.get(i));
			buttonA.find();
			long aX = Integer.parseInt(buttonA.group(0));
			buttonA.find();
			long aY = Integer.parseInt(buttonA.group(0));

			Matcher buttonB = NUMBERS.matcher(input.get(i+1));
			buttonB.find();
			long bX = Integer.parseInt(buttonB.group(0));
			buttonB.find();
			long bY = Integer.parseInt(buttonB.group(0));

			Matcher price = NUMBERS.matcher(input.get(i+2));
			price.find();
			long priceX = Integer.parseInt(price.group(0));
			price.find();
			long priceY = Integer.parseInt(price.group(0));

			result.add(new Machine(priceX, priceY, aX, aY, bX, bY));
		}

		return result;
	}

	private static class Machine {
		final long aX, aY;
		final long bX, bY;
		final long priceX, priceY;

		public Machine(long priceX, long priceY, long aX, long aY, long bX, long bY) {
			this.aX = aX;
			this.aY = aY;
			this.bX = bX;
			this.bY = bY;
			this.priceX = priceX;
			this.priceY = priceY;
		}

		@Override
		public String toString() {
			return "Machine [aX=" + aX + ", aY=" + aY + ", bX=" + bX + ", bY=" + bY + ", priceX=" + priceX + ", priceY=" + priceY + "]";
		}
		
	}

}
