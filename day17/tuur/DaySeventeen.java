//package tuur;

import java.util.List;
import java.util.StringJoiner;

public class DaySeventeen {
	/* DECOMPILED PROGRAM:
	 * 
	 * BST 4 : B = A mod 8 (take the first 3 bits = 1 octal)
	 * BXL 1 : B = B XOR 1
	 * CDV 5 : C = A / 2^B (shiftleft by B bits)
	 * BXL 5 : B = B XOR 5
	 * BXC 0 : B = B XOR C
	 * OUT 5 : OUTPUT B
	 * ADV 3 : A = A / 2^3 (A shift left 3 bits or 1 octal)
	 * JNZ 0 : JUMP to beginning if A != 0 (repeat the program)
	 * 
	 * Remarks: 
	 * - The program always repeats these 8 steps. 
	 * - Only the value of A is carried over between each repeat: B and C are deduced from A
	 * - The value of A gets smaller each iteration (shiftleft by 3 bits)
	 */
	public static void main(String[] args) throws Exception {
		Long registerA = 64854237l;
		Long registerB = 0l;
		Long registerC = 0l;
		List<Integer> program = List.of(2,4,1,1,7,5,1,5,4,0,5,5,0,3,3,0);
		
		System.out.println("Part 1: " + runProgram(registerA, registerB, registerC, program));

		System.out.println("Part 2: " + partTwo(program));
	}

	/**
	 * Working backwards to get close to the final result: 
	 * 1. find a number that produces the last element of the program, 
	 * 2. then multiply it by 8 (or shift right by 3). <- figured this out by manually 'decompiling the program'
	 * 3. use that as a starting point to find a number that produces the last 2 elements of the program.
	 * 4. REPEAT
	 */
	private static long partTwo(List<Integer> program) {
		// long start = 78334343l; 		//"5,4,0,5,5,0,3,3,0"
		// long start = 626674746l; 	//"1,5,4,0,5,5,0,3,3,0"
		// long  start = 5013397978l; 	//"5,1,5,4,0,5,5,0,3,3,0"
		// long start = 40107183830l; 	//"7,5,1,5,4,0,5,5,0,3,3,0"
		// long start = 320857470647l; 	//"1,7,5,1,5,4,0,5,5,0,3,3,0"
		// long start = 2566859765178l; //"1,1,7,5,1,5,4,0,5,5,0,3,3,0"
		// long start = 20534878121424l; //"4,1,1,7,5,1,5,4,0,5,5,0,3,3,0"
		// long start = 164279024971453l;	//"2,4,1,1,7,5,1,5,4,0,5,5,0,3,3,0"
		
		long a = 0l;
		for (int i = 0; i < program.size(); i++) {
			StringJoiner expected = new StringJoiner(",");
			for (int j = program.size() - i - 1; j < program.size(); j++) {
				expected.add(Integer.toString(program.get(j)));
			}
			
			a = a << 3; // doing this unnecessarily the first time, but otherwise 'a' gets overwritten in the last iteration.
			a = bruteForce(a, program, expected.toString());		
		}
		return a;
	}

	private static long bruteForce(long startA, List<Integer> program, String expected) {

		for (long a = startA; a < Long.MAX_VALUE; a++) {
			String output = runProgram(a, 0l, 0l, program);
			if (expected.equals(output)) {
				return a;
			}
		}

		throw new RuntimeException("Nothing found.");
	}

	private static String runProgram(long registerA, long registerB, long registerC, List<Integer> program) {
		StringJoiner output = new StringJoiner(",");
		
		int instructionCounter = 0;
		while (instructionCounter + 1 < program.size()) {
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
				instructionCounter += 2;  // increases by 2 (after the instruction), unless a jump instruction was executed.
			}
		}
		return output.toString();
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
