/**
 * Generated JSON pojos from https://raw.githubusercontent.com/lsv/fifa-worldcup-2018/master/data.json
 */
package betman.gameprovider.fifa2018


import com.fasterxml.jackson.annotation.JsonProperty

data class A(@JsonProperty("winner")
             val winner: String? = null,
             @JsonProperty("runnerup")
             val runnerup: String? = null,
             @JsonProperty("matches")
             val matches: List<MatchesItem>?)


data class B(@JsonProperty("winner")
             val winner: String? = null,
             @JsonProperty("runnerup")
             val runnerup: String? = null,
             @JsonProperty("matches")
             val matches: List<MatchesItem>?)


data class C(@JsonProperty("winner")
             val winner: String? = null,
             @JsonProperty("runnerup")
             val runnerup: String? = null,
             @JsonProperty("matches")
             val matches: List<MatchesItem>?)


data class D(@JsonProperty("winner")
             val winner: String? = null,
             @JsonProperty("runnerup")
             val runnerup: String? = null,
             @JsonProperty("matches")
             val matches: List<MatchesItem>?)


data class E(@JsonProperty("winner")
             val winner: String? = null,
             @JsonProperty("runnerup")
             val runnerup: String? = null,
             @JsonProperty("matches")
             val matches: List<MatchesItem>?)


data class F(@JsonProperty("winner")
             val winner: String? = null,
             @JsonProperty("runnerup")
             val runnerup: String? = null,
             @JsonProperty("matches")
             val matches: List<MatchesItem>?)


data class G(@JsonProperty("winner")
             val winner: String? = null,
             @JsonProperty("runnerup")
             val runnerup: String? = null,
             @JsonProperty("matches")
             val matches: List<MatchesItem>?)


data class H(@JsonProperty("winner")
             val winner: String? = null,
             @JsonProperty("runnerup")
             val runnerup: String? = null,
             @JsonProperty("matches")
             val matches: List<MatchesItem>?)


data class MatchesItem(@JsonProperty("date")
                       val date: String = "",
                       @JsonProperty("home_result")
                       val homeResult: String? = null,
                       @JsonProperty("name")
                       val name: Int = 0,
                       @JsonProperty("stadium")
                       val stadium: Int = 0,
                       @JsonProperty("finished")
                       val finished: Boolean = false,
                       @JsonProperty("away_result")
                       val awayResult: String? = null,
                       @JsonProperty("type")
                       val type: String = "",
                       @JsonProperty("home_team")
                       val homeTeam: String? = null,
                       @JsonProperty("away_team")
                       val awayTeam: String? = null,
                       @JsonProperty("home_penalty")
                       val homePenalty: Int? = null,
                       @JsonProperty("winner")
                       val winner: String? = null,
                       @JsonProperty("away_penalty")
                       val awayPenalty: Int? = null,
                       @JsonProperty("channels")
                       val channels: List<String>? = null
)


data class Knockout(@JsonProperty("round_8")
                    val round8: Round,
                    @JsonProperty("round_2")
                    val round2: Round,
                    @JsonProperty("round_4")
                    val round4: Round,
                    @JsonProperty("round_2_loser")
                    val round2Loser: RoundLoser,
                    @JsonProperty("round_16")
                    val round16: Round)


data class Lsv(@JsonProperty("knockout")
               val knockout: Knockout,
               @JsonProperty("stadiums")
               val stadiums: List<StadiumsItem>?,
               @JsonProperty("tvchannels")
               val tvchannels: List<TvchannelsItem>?,
               @JsonProperty("teams")
               val teams: List<TeamsItem>?,
               @JsonProperty("groups")
               val groups: Groups)


data class TeamsItem(@JsonProperty("name")
                     val name: String = "",
                     @JsonProperty("id")
                     val id: Int = 0,
                     @JsonProperty("iso2")
                     val iso: String = "")


data class Groups(@JsonProperty("a")
                  val a: A,
                  @JsonProperty("b")
                  val b: B,
                  @JsonProperty("c")
                  val c: C,
                  @JsonProperty("d")
                  val d: D,
                  @JsonProperty("e")
                  val e: E,
                  @JsonProperty("f")
                  val f: F,
                  @JsonProperty("g")
                  val g: G,
                  @JsonProperty("h")
                  val h: H)


data class Round(@JsonProperty("name")
                 val name: String = "",
                 @JsonProperty("matches")
                 val matches: List<MatchesItem>?)


data class StadiumsItem(@JsonProperty("lng")
                        val lng: Double = 0.0,
                        @JsonProperty("city")
                        val city: String = "",
                        @JsonProperty("name")
                        val name: String = "",
                        @JsonProperty("id")
                        val id: Int = 0,
                        @JsonProperty("lat")
                        val lat: Double = 0.0)


data class RoundLoser(@JsonProperty("name")
                      val name: String = "",
                      @JsonProperty("matches")
                      val matches: List<MatchesItem>?)


data class TvchannelsItem(@JsonProperty("name")
                          val name: String = "",
                          @JsonProperty("icon")
                          val icon: String = "",
                          @JsonProperty("id")
                          val id: Int = 0,
                          @JsonProperty("country")
                          val country: String = "",
                          @JsonProperty("iso2")
                          val iso2: String = "",
                          @JsonProperty("lang")
                          val Lang: List<String>? = null

)


