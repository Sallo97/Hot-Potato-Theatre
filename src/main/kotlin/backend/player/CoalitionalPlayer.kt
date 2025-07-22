package backend.player

import backend.game.Game
import backend.game.GameType
import backend.potato.Potato

/**
 * A Coalitional player in the SHPG, i.e. an irrational player concerned in forming a coalition with other players of the
 * same type in order to get a better payoff.
 *
 * @property [payoff] the payoff of the player.
 * @constructor creates a player without the hot potato, with a [payoff] of 0.
 */
class CoalitionalPlayer(id:Int) : Player(id, PlayerType.COALITIONAL) {

    /**
     * Handles the decision logic of the player behind either the acceptance or denying of the hot potato.
     * The decision of a Coalition player depends on the coalition. If the coalition has room left for him/her
     * in order to reach the optimalPayoff and said value is positive, then the player will accept, otherwise it
     * will deny the good.
     *
     * @param [game] represents the current game state, which impacts the choice.
     * @return true if the player *chosen* to accept the good, false otherwise.
     */
    override fun decideAcceptance(game: Game): Boolean {
        val decision = when(game.type) {
            GameType.HOMOGENEOUS -> decideForHomogenousGame(game)
            GameType.MIXED -> decideForMixedGame(game)
        }
        return decision
    }

    /**
     * Handles the decision logic in the case the game is composed of only Coalitional players. In this case is
     * considered statically to reach the optimal coalition.
     *
     * @param [game] represents the current game state, which impacts the choice.
     * @return true if the player *chosen* to accept the good, false otherwise.
     */
    private fun decideForHomogenousGame(game: Game): Boolean {
        val decision = game.coalition?.isWillingToTakeANewMember() ?:false
        return decision
    }

    /**
     * Handles the decision logic in the case the game is composed of only Coalitional players.
     * In this case is done dynamically by considering the gain.
     *
     * @param [game] represents the current game state, which impacts the choice.
     * @return true if the player *chosen* to accept the good, false otherwise.
     */
    private fun decideForMixedGame(game: Game): Boolean {
        val turn = game.getCurrentTurn()
        val potato = game.potato
        val coalition = game.coalition!!

        val decision = if(coalition.size() > 0) {
            val loss = potato.getCurrentLoss(turn)
            coalition.acceptProposition(loss)

        } else {
            decideMixedStartingPlayer(game)
        }
        return decision
    }

    /**
     * @return true if the starting player should accept to take the potato, false otherwise.
     */
    private fun decideMixedStartingPlayer(game: Game) : Boolean {
        val potato = game.potato
        val coalitionPlayersLeft = game.numOfCoalitionalPlayer - 1
        // Assumes potato gain and losses are fixed (at least initially)
        if (coalitionPlayersLeft <= 0) {
            return false
        }
        val remainingTurns = game.getRemainingTurnsExceptCurrent()
        val remainingPlayers = game.getNumOfAvailablePlayers() - 1

        val minEstimated = computeMinimumEstimatedPayoff(potato, minOf(remainingPlayers, remainingTurns))
        if (minEstimated == null) {
            return false
        }
        val (minCoalPlayers, minPayoff) = minEstimated

        val turnsLeft = game.getRemainingTurnsExceptCurrent()
        val playersLeft = game.getNumOfAvailablePlayers() - 1
        val probAcceptance = computeProbCoalitionalPlayersWillBeChosen(turnsLeft, playersLeft, minCoalPlayers)
        val probDeny = computeProbNoCoalPlayerWillBeChosen(turnsLeft, playersLeft, minCoalPlayers)

        val acceptanceWeight = minPayoff * probAcceptance
        val denyWeight = potato.baseLoss * probDeny

        return potatoAcceptance(acceptanceWeight, denyWeight, potato)
    }

    /**
     * @return the probability that within [turns] no [coalPlayers] will be chosen among the total [players]
     */
    private fun computeProbNoCoalPlayerWillBeChosen(turns: Int, players: Int, coalPlayers: Int) : Double {
        val numerator = players - coalPlayers
        val denominator = turns

        val prob: Double = numerator.toDouble() / denominator.toDouble()
        return prob
    }

    /**
     * @return the probability that [coalPlayers] will be chosen among the available [players] within [turns]
     */
    private fun computeProbCoalitionalPlayersWillBeChosen(turns: Int, players: Int, coalPlayers:Int) : Double {
        val notMinCoalitionalPlayersLeft = players - coalPlayers
        val notMinCoalitionTurnsLeft = (turns - coalPlayers)
        val numerator = binomial(notMinCoalitionalPlayersLeft, notMinCoalitionTurnsLeft)
        val denominator = binomial(players, turns)

        val prob: Double = numerator.toDouble() / denominator.toDouble()
        return prob
    }

    /**
     * @return the pair (size, payoff) of the minimal coalition that guarantees positive payoff to all players.
     */
    private fun computeMinimumEstimatedPayoff(potato: Potato, turns: Int) : Pair<Int, Double>? {
        var gain = 0.0
        var minSize: Int
        var minPayoff: Double

        for (i in 1..turns) {
            gain += potato.baseGain
            minPayoff = (gain - potato.baseLoss) / i + 1
            if (minPayoff > 0) {
                minSize = i
                return Pair(minSize, minPayoff)
            }
        }

        return null
    }

    /**
     * @return the binomial coefficient between n and k
     */
    private fun binomial(n: Int, k: Int): Long {
        require(n >= 0 && k >= 0 && k <= n) { "Invalid values: require 0 ≤ k ≤ n" }

        var result = 1L
        for (i in 1..k) {
            result = result * (n - i + 1) / i
        }
        return result
    }

    override fun toString(): String {
        val str = super.toString()
        return str
    }
}
