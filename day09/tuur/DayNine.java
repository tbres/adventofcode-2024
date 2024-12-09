package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DayNine {
	
	public static void main(String[] args) throws Exception {
		String input = Files.readString(Paths.get(DayNine.class.getResource("input-day09.txt").toURI())).trim();

		System.out.println("Part 1: "+ checkSum(partOne(parse(input))));

		System.out.println("Part 2: " + checkSum(partTwo(parse(input))));
	}

	private static List<Integer> partTwo(List<Integer> memory) {

		for (int readPointer = memory.size() - 1; readPointer > 0; readPointer--) {
			Integer value = memory.get(readPointer);

			if (value != null) {
				final int end = readPointer;

				int start = end;
				for (int j = readPointer; j>=0; j --) {
					if (memory.get(j) == value) {
						start = j;
					} else {
						break;
					}
				}

				int length = end - start + 1;

				Optional<Integer> writePointer = findFreeSpace(memory, length);
				if (writePointer.isPresent() && writePointer.get() < readPointer) {
					for (int i = 0; i < length; i++) {
						memory.set(writePointer.get() + i, value);
						memory.set(readPointer - i, null); 
					}
				}

				readPointer = start;
			}
		}

		return memory;
	}

	
	private static List<Integer> partOne(List<Integer> memory) {
		
		int writePointer = 0;
		for (int readPointer = memory.size() - 1; readPointer > writePointer; readPointer--) {
			Integer value = memory.get(readPointer);
			
			if (value != null) {
				while (memory.get(writePointer) != null && writePointer < readPointer) {
					writePointer++;
				}
				if (memory.get(writePointer) == null) {
					memory.set(writePointer, value);
					memory.set(readPointer, null);
				} else {
					break; // no more empty spaces
				}
			}
		}
		
		return memory;
	}
	
	private static Optional<Integer> findFreeSpace(List<Integer> memory, int length) {
	
		for (int i = 0; i < memory.size(); i++) {
			if (memory.get(i) == null) {
				boolean fits = true;
				for (int j = i + 1; j < i + length; j++) {
					if (j >= memory.size() || memory.get(j) != null) {
						fits = false;
						i = j;
						break;
					}
				}
				if (fits) {
					return Optional.of(i);
				}
			}
		}
	
		return Optional.empty();
	}

	private static long checkSum(List<Integer> memory) {
		long result = 0l;
		for (int id = 0; id < memory.size(); id ++) {
			Integer value = memory.get(id);
			if (value == null) {
				value = 0;
			}
			long theValue = value.longValue();
			long theId = Integer.valueOf(id).longValue();
			result += theId * theValue;

		}
		return result;
	}

	private static List<Integer> parse(String input) {
		List<Integer> result = new ArrayList<>();
		Integer id = 0; 
		for (int i = 0; i < input.length(); i++) {
			int length = Integer.parseInt("" + input.charAt(i));
			Integer value = null;
			if (i%2 == 0) {;
				value = id;
			}
			for (int j = 0; j < length; j++) {
				result.add(value);
			}
			if (i%2 == 0) {;
				id++;
			}			
		}
		return result;
	}
}
