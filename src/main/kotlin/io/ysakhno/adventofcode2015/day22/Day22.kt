/*
 * --- Day 22: Wizard Simulator 20XX ---
 *
 * Little Henry Case decides that defeating bosses with swords and stuff is boring.  Now he's playing the game with a
 * wizard.  Of course, he gets stuck on another boss and needs your help again.
 *
 * In this version, combat still proceeds with the player and the boss taking alternating turns.  The player still goes
 * first.  Now, however, you don't get any equipment; instead, you must choose one of your spells to cast.  The first
 * character at or below 0 hit points loses.
 *
 * Since you're a wizard, you don't get to wear armor, and you can't attack normally.  However, since you do magic
 * damage, your opponent's armor is ignored, and so the boss effectively has zero armor as well.  As before, if armor
 * (from a spell, in this case) would reduce damage below 1, it becomes 1 instead - that is, the boss' attacks always
 * deal at least 1 damage.
 *
 * On each of your turns, you must select one of your spells to cast.  If you cannot afford to cast any spell, you lose.
 * Spells cost mana; you start with 500 mana, but have no maximum limit.  You must have enough mana to cast a spell, and
 * its cost is immediately deducted when you cast it.  Your spells are Magic Missile, Drain, Shield, Poison, and
 * Recharge.
 *
 *   - Magic Missile costs 53 mana.  It instantly does 4 damage.
 *   - Drain costs 73 mana.  It instantly does 2 damage and heals you for 2 hit points.
 *   - Shield costs 113 mana.  It starts an effect that lasts for 6 turns.  While it is active, your armor is increased
 *     by 7.
 *   - Poison costs 173 mana.  It starts an effect that lasts for 6 turns.  At the start of each turn while it is
 *     active, it deals the boss 3 damage.
 *   - Recharge costs 229 mana.  It starts an effect that lasts for 5 turns.  At the start of each turn while it is
 *     active, it gives you 101 new mana.
 *
 * Effects all work the same way.  Effects apply at the start of both the player's turns and the boss' turns.  Effects
 * are created with a timer (the number of turns they last); at the start of each turn, after they apply any effect they
 * have, their timer is decreased by one.  If this decreases the timer to zero, the effect ends.  You cannot cast a
 * spell that would start an effect that is already active.  However, effects can be started on the same turn they end.
 *
 * For example, suppose the player has 10 hit points and 250 mana, and that the boss has 13 hit points and 8 damage:
 *
 *   -- Player turn --
 *   - Player has 10 hit points, 0 armor, 250 mana
 *   - Boss has 13 hit points
 *   Player casts Poison.
 *
 *   -- Boss turn --
 *   - Player has 10 hit points, 0 armor, 77 mana
 *   - Boss has 13 hit points
 *   Poison deals 3 damage; its timer is now 5.
 *   Boss attacks for 8 damage.
 *
 *   -- Player turn --
 *   - Player has 2 hit points, 0 armor, 77 mana
 *   - Boss has 10 hit points
 *   Poison deals 3 damage; its timer is now 4.
 *   Player casts Magic Missile, dealing 4 damage.
 *
 *   -- Boss turn --
 *   - Player has 2 hit points, 0 armor, 24 mana
 *   - Boss has 3 hit points
 *   Poison deals 3 damage.  This kills the boss, and the player wins.
 *
 * Now, suppose the same initial conditions, except that the boss has 14 hit points instead:
 *
 *   -- Player turn --
 *   - Player has 10 hit points, 0 armor, 250 mana
 *   - Boss has 14 hit points
 *   Player casts Recharge.
 *
 *   -- Boss turn --
 *   - Player has 10 hit points, 0 armor, 21 mana
 *   - Boss has 14 hit points
 *   Recharge provides 101 mana; its timer is now 4.
 *   Boss attacks for 8 damage!
 *
 *   -- Player turn --
 *   - Player has 2 hit points, 0 armor, 122 mana
 *   - Boss has 14 hit points
 *   Recharge provides 101 mana; its timer is now 3.
 *   Player casts Shield, increasing armor by 7.
 *
 *   -- Boss turn --
 *   - Player has 2 hit points, 7 armor, 110 mana
 *   - Boss has 14 hit points
 *   Shield's timer is now 5.
 *   Recharge provides 101 mana; its timer is now 2.
 *   Boss attacks for 8 - 7 = 1 damage!
 *
 *   -- Player turn --
 *   - Player has 1 hit point, 7 armor, 211 mana
 *   - Boss has 14 hit points
 *   Shield's timer is now 4.
 *   Recharge provides 101 mana; its timer is now 1.
 *   Player casts Drain, dealing 2 damage, and healing 2 hit points.
 *
 *   -- Boss turn --
 *   - Player has 3 hit points, 7 armor, 239 mana
 *   - Boss has 12 hit points
 *   Shield's timer is now 3.
 *   Recharge provides 101 mana; its timer is now 0.
 *   Recharge wears off.
 *   Boss attacks for 8 - 7 = 1 damage!
 *
 *   -- Player turn --
 *   - Player has 2 hit points, 7 armor, 340 mana
 *   - Boss has 12 hit points
 *   Shield's timer is now 2.
 *   Player casts Poison.
 *
 *   -- Boss turn --
 *   - Player has 2 hit points, 7 armor, 167 mana
 *   - Boss has 12 hit points
 *   Shield's timer is now 1.
 *   Poison deals 3 damage; its timer is now 5.
 *   Boss attacks for 8 - 7 = 1 damage!
 *
 *   -- Player turn --
 *   - Player has 1 hit point, 7 armor, 167 mana
 *   - Boss has 9 hit points
 *   Shield's timer is now 0.
 *   Shield wears off, decreasing armor by 7.
 *   Poison deals 3 damage; its timer is now 4.
 *   Player casts Magic Missile, dealing 4 damage.
 *
 *   -- Boss turn --
 *   - Player has 1 hit point, 0 armor, 114 mana
 *   - Boss has 2 hit points
 *   Poison deals 3 damage.  This kills the boss, and the player wins.
 *
 * You start with 50 hit points and 500 mana points.  The boss's actual stats are in your puzzle input.  What is the
 * least amount of mana you can spend and still win the fight?  (Do not include mana recharge effects as "spending"
 * negative mana.)
 *
 * --- Part Two ---
 *
 * On the next run through the game, you increase the difficulty to hard.
 *
 * At the start of each player turn (before any other effects apply), you lose 1 hit point.  If this brings you to or
 * below 0 hit points, you lose.
 *
 * With the same starting stats for you and the boss, what is the least amount of mana you can spend and still win the
 * fight?
 */
package io.ysakhno.adventofcode2015.day22

import io.ysakhno.adventofcode2015.day22.FightState.Outcome.BOSS_WON
import io.ysakhno.adventofcode2015.day22.FightState.Outcome.CONTINUE
import io.ysakhno.adventofcode2015.day22.FightState.Outcome.HENRY_WON
import io.ysakhno.adventofcode2015.util.ProblemInput
import io.ysakhno.adventofcode2015.util.println
import java.util.Comparator.comparingInt
import java.util.PriorityQueue
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private data class Player(
    val name: String,
    val hitpoints: Int,
    val mana: Int = 0,
    val damage: Int = 0,
    val armor: Int = 0,
) {
    val isAlive get() = hitpoints > 0
    val isDead get() = !isAlive
    fun takeDamage(damage: Int) = copy(hitpoints = hitpoints - (damage - armor).coerceAtLeast(1))
    fun heal(points: Int): Player {
        check(isAlive) { "Cannot heal dead things" }
        return copy(hitpoints = hitpoints + points)
    }
    fun burnMana(cost: Int): Player {
        require(mana >= cost) { "Cost of spell ($cost) is greater than available mana ($mana)" }
        check(isAlive) { "Dead players cannot cast spells" }
        return copy(mana = mana - cost)
    }
}

private sealed interface Effect {
    val manaCost: Int
    fun start(henry: Player, boss: Player): Pair<Player, Player>
}

private data object MagicMissile : Effect {
    override val manaCost get() = 53
    override fun start(henry: Player, boss: Player) = henry.burnMana(manaCost) to boss.takeDamage(4)
}

private data object Drain : Effect {
    override val manaCost get() = 73
    override fun start(henry: Player, boss: Player) = henry.burnMana(manaCost).heal(2) to boss.takeDamage(2)
}

private sealed interface LastingEffect : Effect {
    var timer: Int
    val hasWornOff get() = timer <= 0
    fun apply(henry: Player, boss: Player): Pair<Player, Player>
    fun copy(): LastingEffect
}

private abstract class AbstractLastingEffect(override var timer: Int) : LastingEffect {
    override fun toString() = "${this::class.simpleName}($timer)"
}

private class Shield private constructor(timer: Int) : AbstractLastingEffect(timer) {
    constructor() : this(6)
    override val manaCost get() = 113
    override fun start(henry: Player, boss: Player) = henry.burnMana(manaCost).copy(armor = henry.armor + 7) to boss
    override fun apply(henry: Player, boss: Player) = with(henry) {
        if (--timer == 0) copy(armor = (armor - 7).coerceAtLeast(0)) else this
    } to boss
    override fun copy() = Shield(timer)
}

private class Poison private constructor(timer: Int) : AbstractLastingEffect(timer) {
    constructor() : this(6)
    override val manaCost get() = 173
    override fun start(henry: Player, boss: Player) = henry.burnMana(manaCost) to boss
    override fun apply(henry: Player, boss: Player) = henry to boss.takeDamage(3).also { --timer }
    override fun copy() = Poison(timer)
}

private class Recharge private constructor(timer: Int) : AbstractLastingEffect(timer) {
    constructor() : this(5)
    override val manaCost get() = 229
    override fun start(henry: Player, boss: Player) = henry.burnMana(manaCost) to boss
    override fun apply(henry: Player, boss: Player) = with(henry) { copy(mana = mana + 101) to boss }.also { --timer }
    override fun copy() = Recharge(timer)
}

private data class FightState(
    val henry: Player,
    val boss: Player,
    val manaSpent: Int = 0,
    val activeEffects: List<LastingEffect> = emptyList(),
    val isHardDifficulty: Boolean = false,
) {
    enum class Outcome { CONTINUE, HENRY_WON, BOSS_WON }

    val outcome get() = when {
        henry.isDead -> BOSS_WON
        boss.isDead -> HENRY_WON
        else -> CONTINUE
    }

    fun startTurn(): FightState {
        val players = with(henry) {
            if (isHardDifficulty) takeDamage(1).also { if (it.isDead) return copy(henry = it) } else this
        } to boss
        val newEffects = activeEffects.map(LastingEffect::copy)
        val (newHenry, newBoss) = newEffects.fold(players) { (henry, boss), effect -> effect.apply(henry, boss) }

        return copy(henry = newHenry, boss = newBoss, activeEffects = newEffects.filterNot(LastingEffect::hasWornOff))
    }

    fun isPossibleToCast(effect: Effect) =
        (effect !is LastingEffect || activeEffects.none(effect::class::isInstance)) && effect.manaCost <= henry.mana

    fun cast(effect: Effect): FightState {
        check(outcome == CONTINUE) { "Cannot cast, because the fight is in the wrong state ($outcome)" }
        require(isPossibleToCast(effect)) {
            "Cannot cast ${
                effect.javaClass.simpleName
            }, because it is already one of active effects or not enough Mana"
        }

        val (newHenry, newBoss) = effect.start(henry, boss)
        val moreEffects = if (effect is LastingEffect) activeEffects + effect else activeEffects

        var newState = copy(
            henry = newHenry,
            boss = newBoss,
            activeEffects = moreEffects,
            manaSpent = manaSpent + effect.manaCost,
        )
        if (newBoss.isDead) return newState

        newState = newState.startTurn()
        return if (newState.outcome != CONTINUE) newState
        else newState.copy(henry = newState.henry.takeDamage(newState.boss.damage))
    }
}

private fun parseBoss(input: List<String>) = input.map { it.split(':').map(String::trim) }
    .associate { (name, stat) -> name to stat.toInt() }
    .let { stats -> Player(name = "Boss", hitpoints = stats.getValue("Hit Points"), damage = stats.getValue("Damage")) }

private val allSpells = listOf(MagicMissile, Drain, Shield(), Poison(), Recharge())

private fun getAllPossibleSpells(state: FightState) =
    allSpells.filter(state::isPossibleToCast).map { if (it is LastingEffect) it.copy() else it }

private fun generateStates(startState: FightState, predicate: (FightState) -> Boolean) = sequence {
    val queue = PriorityQueue(comparingInt(FightState::manaSpent)).apply { add(startState) }
    val seen = mutableSetOf(startState)

    while (queue.isNotEmpty()) {
        val current = queue.poll().let { state ->
            if (predicate(state)) yield(state)
            if (state.outcome == CONTINUE) state.startTurn() else state
        }

        if (current.outcome == CONTINUE) {
            getAllPossibleSpells(current)
                .asSequence()
                .map(current::cast)
                .filterNot(seen::contains)
                .onEach(seen::add)
                .forEach(queue::add)
        }
    }
}

private fun part1(input: List<String>, henry: Player = Player(name = "Henry", hitpoints = 50, mana = 500)) =
    generateStates(FightState(henry, parseBoss(input))) { it.outcome == HENRY_WON }.first().manaSpent

private fun part2(input: List<String>, henry: Player = Player(name = "Henry", hitpoints = 50, mana = 500)) =
    generateStates(FightState(henry, parseBoss(input), isHardDifficulty = true)) { it.outcome == HENRY_WON }
        .first()
        .manaSpent

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    val testPlayer = Player(name = "Henry", hitpoints = 10, mana = 250)
    assertEquals(173 + 53, part1(testInput, testPlayer), "Part one (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
