#!/bin/bash

DAYZ=(
	Zero
	One
	Two
	Three
	Four
	Five
	Six
	Seven
	Eight
	Nine
	Ten
	Eleven
	Twelve
	Thirteen
	Fourteen
	Fifteen
	Sixteen
	Seventeen
	Eighteen
	Nineteen
	Twenty
	TwentyOne
	TwentyTwo
	TwentyThree
	TwentyFour
	TwentyFive
)

if [[ -z "$1" ]]; then
	day=$(date +%d)
else
	day=$1
fi

if [[ -z "$2" ]]; then
	year=$(date +%Y)
else
	year=$1
fi


formattedNumber=$(printf %02d $day)
humanReadableNumber=${DAYZ[$day]}

mkdir -p "day${formattedNumber}/tuur"

touch "day${formattedNumber}/tuur/test-input-day${formattedNumber}.txt"

cookie=$(cat cookie.txt)
curl -s "https://adventofcode.com/$year/day/$day/input" \
	-o "day${formattedNumber}/tuur/input-day${formattedNumber}.txt" \
	-H "Cookie: ${cookie}"

cat > "day${formattedNumber}/tuur/Day${humanReadableNumber}.java" <<EOF
package tuur;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.*;

public class Day${humanReadableNumber} {
	
	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(Day${humanReadableNumber}.class.getResource("input-day${formattedNumber}.txt").toURI()));

		System.out.println("Part 1: ");

		System.out.println("Part 2: ");
	}
}
EOF
