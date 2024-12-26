package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class DayTwentyThree {
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(DayTwentyThree.class.getResource("input-day23.txt").toURI()));
		Map<String, Set<String>> network = new HashMap<>();
		lines.forEach(line -> {
			String[] split = line.split("-");
			network.computeIfAbsent(split[0], computer -> new HashSet<>()).add(split[1]);
			network.computeIfAbsent(split[1], computer -> new HashSet<>()).add(split[0]);
		});

		System.out.println(network.size());

		System.out.println("Part 1: " + partOne(network));

		System.out.println("Part 2: " + partTwo(network));

	}

	private static String partTwo(Map<String, Set<String>> network) {
		Set<String> best = Collections.emptySet();
		Set<String> seen = new HashSet<>();

		for (Entry<String, Set<String>> entry : network.entrySet()) {
			final String computer = entry.getKey();
			final Set<String> subnet = entry.getValue();

			SortedSet<String> lanparty = new TreeSet<String>();
			lanparty.add(computer);

			SortedSet<String> result = recurse(new ArrayList<>(subnet), lanparty, network, seen);
			if (result.size() > best.size()) {
				best = result;
			}
		}

		return best.stream().collect(Collectors.joining(","));
	}

	private static SortedSet<String> recurse(List<String> subnet, SortedSet<String> lanParty, Map<String, Set<String>> network, Set<String> seen) {
		SortedSet<String> best = lanParty;

		for (String computer : subnet) {
			if (!lanParty.contains(computer)) {
				SortedSet<String> next = new TreeSet<>(lanParty);
				next.add(computer);

				if (seen.contains(next.toString())) {
					continue;
				}
				seen.add(next.toString());

				Set<String> otherSubnet = network.get(computer);
				boolean allConnected = true;
				for (String computerB: lanParty) {
					if (!otherSubnet.contains(computerB)) {
						allConnected = false;
						break;
					}
				}
				if (allConnected) {
					SortedSet<String> result = recurse(subnet, next, network, seen);
					if (result.size() > best.size()) {
						best = result;
					}
				}
			}
		}		

		return best;
	}

	private static long partOne(Map<String, Set<String>> network) {
		Set<String> threeComputers = new HashSet<>();

		for (String computer : network.keySet()) {
			List<String> connectedTo = new ArrayList<>(network.get(computer));

			for (int i = 0; i < connectedTo.size(); i++) {
				String computerB = connectedTo.get(i);
				for (int j = 0; j < connectedTo.size(); j++) {
					String computerC = connectedTo.get(j);

					if (network.get(computerB).contains(computerC)) {
						if (computer.startsWith("t") || computerB.startsWith("t") || computerC.startsWith("t")) {
							threeComputers.add(new TreeSet<String>(Set.of(computer, computerB, computerC)).toString());
						}
					}
				}
			}
		}

		return threeComputers.size();
	}

}
