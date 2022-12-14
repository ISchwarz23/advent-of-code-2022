package aoc2022

import aoc2022.day12.Coords
import aoc2022.day15.Day15
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder
import utils.aocClient
import utils.readInput
import kotlin.test.Test
import kotlin.test.assertEquals

@TestMethodOrder(
    MethodOrderer.Alphanumeric::class
)
internal class Day15Test {

    private val input = readInputAsCoordinatePairs("aoc2022/day15.txt")
    private val inputExample = readInputAsCoordinatePairs("aoc2022/day15_example.txt")

    @Test
    internal fun testPart1() {
        // when
        val result = Day15.part1(inputExample, 10)

        // then
        assertEquals(26, result)

        // submit answer
        val answer = Day15.part1(input, 2_000_000)
        val submissionResult = aocClient.submit(2022, 15, 1, answer)
        println("Result of Day 15 - Part 1: $answer (Submission: $submissionResult)")
    }

    @Test
    internal fun testPart2() {
        // when
        val result = Day15.part2(inputExample, 20)

        // then
        assertEquals(56000011, result)

        // submit answer
        val answer = Day15.part2(input, 4_000_000)
        val submissionResult = aocClient.submit(2022, 15, 2, answer)
        println("Result of Day 15 - Part 2: $answer (Submission: $submissionResult)")
    }

}

private fun readInputAsCoordinatePairs(name: String): List<Pair<Coords, Coords>> {
    return readInput(name).map { toCoordsPair(it) }
}

private fun toCoordsPair(inputLine: String): Pair<Coords, Coords> {
    val sensorCoordsString = inputLine.substringAfter("Sensor at ").substringBefore(":")
    val beaconCoordsString = inputLine.substringAfter("closest beacon is at ")
    return Pair(toCoords(sensorCoordsString), toCoords(beaconCoordsString))
}

private fun toCoords(coordsString: String): Coords {
    val xString = coordsString.substringAfter("x=").substringBefore(",")
    val yString = coordsString.substringAfter("y=")
    return Coords(xString.toInt(), yString.toInt())
}