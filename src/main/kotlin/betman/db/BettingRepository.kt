package betman.db

import betman.pojos.Bet
import io.reactivex.Maybe
import io.reactivex.Single

interface BettingRepository {

    fun get(groupId: String, username: String): Maybe<Bet>

    fun bet(groupId: String, bet: Bet, username: String): Single<Bet>
}