/*
 * --- Day 21: RPG Simulator 20XX ---
 *
 * Little Henry Case got a new video game for Christmas.  It's an RPG, and he's stuck on a boss.  He needs to know what
 * equipment to buy at the shop.  He hands you the controller.
 *
 * In this game, the player (you) and the enemy (the boss) take turns attacking.  The player always goes first.  Each
 * attack reduces the opponent's hit points by at least 1.  The first character at or below 0 hit points loses.
 *
 * Damage dealt by an attacker each turn is equal to the attacker's damage score minus the defender's armor score.  An
 * attacker always does at least 1 damage.  So, if the attacker has a damage score of 8, and the defender has an armor
 * score of 3, the defender loses 5 hit points.  If the defender had an armor score of 300, the defender would still
 * lose 1 hit point.
 *
 * Your damage score and armor score both start at zero.  They can be increased by buying items in exchange for gold.
 * You start with no items and have as much gold as you need.  Your total damage or armor is equal to the sum of those
 * stats from all of your items.  You have 100 hit points.
 *
 * Here is what the item shop is selling:
 *
 *   Weapons:    Cost  Damage  Armor
 *   Dagger        8     4       0
 *   Shortsword   10     5       0
 *   Warhammer    25     6       0
 *   Longsword    40     7       0
 *   Greataxe     74     8       0
 *
 *   Armor:      Cost  Damage  Armor
 *   Leather      13     0       1
 *   Chainmail    31     0       2
 *   Splintmail   53     0       3
 *   Bandedmail   75     0       4
 *   Platemail   102     0       5
 *
 *   Rings:      Cost  Damage  Armor
 *   Damage +1    25     1       0
 *   Damage +2    50     2       0
 *   Damage +3   100     3       0
 *   Defense +1   20     0       1
 *   Defense +2   40     0       2
 *   Defense +3   80     0       3
 *
 * You must buy exactly one weapon; no dual-wielding.  Armor is optional, but you can't use more than one.  You can buy
 * 0-2 rings (at most one for each hand).  You must use any items you buy.  The shop only has one of each item, so you
 * can't buy, for example, two rings of Damage +3.
 *
 * For example, suppose you have 8 hit points, 5 damage, and 5 armor, and that the boss has 12 hit points, 7 damage,
 * and 2 armor:
 *
 *   - The player deals 5-2 = 3 damage; the boss goes down to 9 hit points.
 *   - The boss deals 7-5 = 2 damage; the player goes down to 6 hit points.
 *   - The player deals 5-2 = 3 damage; the boss goes down to 6 hit points.
 *   - The boss deals 7-5 = 2 damage; the player goes down to 4 hit points.
 *   - The player deals 5-2 = 3 damage; the boss goes down to 3 hit points.
 *   - The boss deals 7-5 = 2 damage; the player goes down to 2 hit points.
 *   - The player deals 5-2 = 3 damage; the boss goes down to 0 hit points.
 *
 * In this scenario, the player wins! (Barely.)
 *
 * You have 100 hit points.  The boss's actual stats are in your puzzle input.  What is the least amount of gold you can
 * spend and still win the fight?
 *
 * --- Part Two ---
 *
 * Turns out the shopkeeper is working with the boss, and can persuade you to buy whatever items he wants.  The other
 * rules still apply, and he still only has one of each item.
 *
 * What is the most amount of gold you can spend and still lose the fight?
 */
package io.ysakhno.adventofcode2015.day21

import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private data class Item(val name: String, val cost: Int, val damage: Int, val armor: Int)

private fun armor(name: String, cost: Int, armor: Int) = Item(name, cost, 0, armor)

private fun weapon(name: String, cost: Int, damage: Int) = Item(name, cost, damage, 0)

private fun ring(name: String, cost: Int, damage: Int = 0, armor: Int = 0) = Item(name, cost, damage, armor)

private val weapons = listOf(
    weapon("Dagger", 8, 4),
    weapon("Shortsword", 10, 5),
    weapon("Warhammer", 25, 6),
    weapon("Longsword", 40, 7),
    weapon("Greataxe", 74, 8),
)

private val armors = listOf(
    armor("Bare", 0, 0),
    armor("Leather", 13, 1),
    armor("Chainmail", 31, 2),
    armor("Splintmail", 53, 3),
    armor("Bandedmail", 75, 4),
    armor("Platemail", 102, 5),
)

private val ring = listOf(
    ring("Damage +1", 25, damage = 1),
    ring("Damage +2", 50, damage = 2),
    ring("Damage +3", 100, damage = 3),
    ring("Defense +1", 20, armor = 1),
    ring("Defense +2", 40, armor = 2),
    ring("Defense +3", 80, armor = 3),
)

private data class Player(val name: String, val damage: Int, val armor: Int, var hitpoints: Int) {
    val isAlive get() = hitpoints > 0
    fun takeDamage(damage: Int) {
        hitpoints -= (damage - armor).coerceAtLeast(1)
    }
}

private fun parseBoss(input: List<String>) =
    input.map { it.split(':').map(String::trim) }
        .associate { (name, stat) -> name to stat.toInt() }
        .let { stats ->
            Player(
                name = "boss",
                damage = stats.getValue("Damage"),
                armor = stats.getValue("Armor"),
                hitpoints = stats.getValue("Hit Points"),
            )
        }

private fun chooseGear() = sequence {
    for (weapon in weapons) {
        for (armor in armors) {
            yield(listOf(weapon, armor))
            for (idxRing1 in 0..ring.lastIndex) {
                for (idxRing2 in idxRing1..ring.lastIndex) {
                    val ring1 = ring[idxRing1]
                    val ring2 = ring[idxRing2]

                    if (idxRing1 == idxRing2) yield(listOf(weapon, armor, ring1))
                    else yield(listOf(weapon, armor, ring1, ring2))
                }
            }
        }
    }
}

private fun generateHumanPlayerWithCost(prototype: Player) = chooseGear().map {
    prototype.copy(damage = it.sumOf(Item::damage), armor = it.sumOf(Item::armor)) to it.sumOf(Item::cost)
}

private fun Player.defeats(other: Player): Boolean {
    var active = this
    var opponent = other

    while (active.isAlive && opponent.isAlive) {
        opponent.takeDamage(active.damage)
        active = opponent.also { opponent = active }
    }

    return isAlive
}

private fun part1(input: List<String>, yourHitpoints: Int) = parseBoss(input).let { bossPrototype ->
    generateHumanPlayerWithCost(Player(name = "you", damage = 0, armor = 0, hitpoints = yourHitpoints))
        .filter { (you) -> you.defeats(bossPrototype.copy()) }
        .minOf { it.second }
}

private fun part2(input: List<String>, yourHitpoints: Int) = parseBoss(input).let { bossPrototype ->
    generateHumanPlayerWithCost(Player(name = "you", damage = 0, armor = 0, hitpoints = yourHitpoints))
        .filterNot { (you) -> you.defeats(bossPrototype.copy()) }
        .maxOf { it.second }
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(65, part1(testInput, 8), "Part one (sample input)")
    assertEquals(188, part2(testInput, 8), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input, 100).println()
    part2(input, 100).println()
}
