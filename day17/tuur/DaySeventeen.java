//package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DaySeventeen {
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DaySeventeen.class.getResource("input-day17.txt").toURI()));
		
		// Sample
		// Long registerA = 729;
		// Long registerB = 0;
		// Long registerC = 0;
		// List<Long> program = List.of(0,1,5,4,3,0);

		// Sample part 2
		// Long registerA = 2024;
		// Long registerB =  0;
		// Long registerC =  0;
		// List<Long> program = List.of(0,3,5,4,3,0);

		// Input
		Long registerA = 64854237l; //35184372088832
		Long registerB = 0l;
		Long registerC = 0l;
		List<Integer> program = List.of(2,4,1,1,7,5,1,5,4,0,5,5,0,3,3,0);
		
		System.out.println("Part 1: " + runProgram(registerA, registerB, registerC, program));
		//final String expected = program.stream().map(i -> "" + i).collect(Collectors.joining(","));
		//partTwo(registerB, registerC, program);

		partTwo(0l, 0l, program, "2,4,1,1,7,5,1,5,4,0,5,5,0,3,3,0");
	}

	private static void partTwo(Long registerB, Long registerC, List<Integer> program, String expected) {
		// long start = 78334343l; 		//"5,4,0,5,5,0,3,3,0"
		// long start = 626674746l; 	//"1,5,4,0,5,5,0,3,3,0"
		// long  start = 5013397978l; 	//"5,1,5,4,0,5,5,0,3,3,0"
		// long start = 40107183830l; 	//"7,5,1,5,4,0,5,5,0,3,3,0"
		// long start = 320857470647l; 	//"1,7,5,1,5,4,0,5,5,0,3,3,0"
		// long start = 2566859765178l; //"1,1,7,5,1,5,4,0,5,5,0,3,3,0"
		long start = 20534878121424l; //"4,1,1,7,5,1,5,4,0,5,5,0,3,3,0"
		// long start = 164279024971453l;	//"2,4,1,1,7,5,1,5,4,0,5,5,0,3,3,0"
		long next = start << 3;

		for (long a = next; a < Long.MAX_VALUE; a++) {
			String output = runProgram(a, 0l, 0l, program);
			if (expected.equals(output)) {
				System.out.println(expected);
				System.out.println("Part 2: " + a+ " -> " + Long.toOctalString(a));
				return;
			}

		}
		System.out.println("Part 2: UNKNOWN");
	}

	private static String runProgram(long registerA, long registerB, long registerC, List<Integer> program) {
		StringJoiner output = new StringJoiner(",");
		
		int instructionCounter = 0; // increases by 2 (after the instruction)
		final int size = program.size();

		// int idx = 1;
		while (instructionCounter + 1 < size) {
			int opcode = program.get(instructionCounter);
			long operand = program.get(instructionCounter + 1);

			long comboOperand = comboOperand(operand, registerA, registerB, registerC);

			boolean jump = false;
			switch (opcode) {
				case 0 -> { // ADV -> A Division					
					int denominator = (int) Math.pow(2, comboOperand);
					registerA = registerA / denominator;
				}
				case 1 -> { // BXL -> B XOR Literal operand
					registerB = registerB ^ operand;
				}
				case 2 -> { // BST -> Binary Search Tree?
					registerB = comboOperand % 8;
				}
				case 3 -> { // JNZ -> Jump if Not Zero
					if (registerA != 0) {
						instructionCounter = Long.valueOf(operand).intValue();
						jump = true;
					}
				}
				case 4 -> { // BXC -> B XOR C
					registerB = registerB ^ registerC;
				}
				case 5 -> { // OUT
					long out = comboOperand % 8;
					output.add("" + out);
				}
				case 6 -> { // BDV -> B DiVision
					int denominator = (int) Math.pow(2, comboOperand);
					registerB = registerA / denominator;
				}
				case 7 -> { // CDV -> C DiVision
					int denominator = (int) Math.pow(2, comboOperand);
					registerC = registerA / denominator;
				}
				default -> {
					throw new IllegalStateException("Shouldn't happen, illegal opcode: " + opcode);
				}
			}

			if (!jump) {
				instructionCounter += 2;
			}
		}
		return output.toString();
	}

	private static final String operationName(int opcode) {
		return switch (opcode) {
			case 0 -> "adv";
			case 1 -> "bxl";
			case 2 -> "bst";
			case 3 -> "jnz";
			case 4 -> "bxc";
			case 5 -> "out";
			case 6 -> "bdv";
			case 7 -> "cdv";
			default -> "UNKNOWN";
		};
	}

	private static long comboOperand (long operand, long registerA, long registerB, long registerC) {
		if (0 <= operand && operand <= 3) {
			return operand;
		} else if (operand == 4) {
			return registerA;
		} else if (operand == 5) {
			return registerB;
		} else if (operand == 6) {
			return registerC;
		} else {
			throw new IllegalArgumentException("Invalid combo operand: " + operand);
		}
	}
}
