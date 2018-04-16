package fi.iki.lahtela.betman

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GamesController {
    companion object {
        const val REGULAR_GAMES = 48
    }

    @GetMapping("/all")
    fun all(): List<Game> {
        val team = Team("Germany")
        return List(REGULAR_GAMES, {Game(it,team,team)})
    }

    private fun games() {
        // Game(1, "Russia", "Saudi Arabia", 	"14-Jun"	"Moscow", "Luzhniki Stadium")
        /* 1	Russia v Saudi Arabia	14-Jun	Moscow	Luzhniki Stadium
2	Egypt v Uruguay	15-Jun	Ekaterinburg	Ekaterinburg Arena
3	Portugal v Spain	15-Jun	Sochi	Fisht Stadium
4	Morocco v Iran	15-Jun	Saint Petersburg	Saint Petersburg Stadium
5	France v Australia	16-Jun	Kazan	Kazan Arena
6	Peru v Denmark	16-Jun	Saransk	Mordovia Arena
7	Argentina v Iceland	16-Jun	Moscow	Spartak Stadium
8	Croatia v Nigeria	16-Jun	Kaliningrad	Kaliningrad Stadium
9	Brazil v Switzerland	17-Jun	Rostov-on-Don	Rostov Arena
10	Costa Rica v Serbia	17-Jun	Samara	Samara Arena
11	Germany v Mexico	17-Jun	Moscow	Luzhniki Stadium
12	Sweden v Korea Republic	18-Jun	Nizhny Novgorod	Nizhny Novgorod Stadium
13	Belgium v Panama	18-Jun	Sochi	Fisht Stadium
14	Tunisia v England	18-Jun	Volgograd	Volgograd Arena
15	Poland v Senegal	19-Jun	Moscow	Spartak Stadium
16	Colombia v Japan	19-Jun	Saransk	Mordovia Arena
17	Russia v Egypt	19-Jun	Saint Petersburg	Saint Petersburg Stadium
18	Uruguay v Saudi Arabia	20-Jun	Rostov-on-Don	Rostov Arena
19	Portugal v Morocco	20-Jun	Moscow	Luzhniki Stadium
20	Iran v Spain	20-Jun	Kazan	Kazan Arena
21	France v Peru	21-Jun	Ekaterinburg	Ekaterinburg Arena
22	Denmark v Australia	21-Jun	Samara	Samara Arena
23	Argentina v Croatia	21-Jun	Nizhny Novgorod	Nizhny Novgorod Stadium
24	Nigeria v Iceland	22-Jun	Volgograd	Volgograd Arena
25	Brazil v Costa Rica	22-Jun	Saint Petersburg	Saint Petersburg Stadium
26	Serbia v Switzerland	22-Jun	Kaliningrad	Kaliningrad Stadium
27	Germany v Sweden	23-Jun	Sochi	Fisht Stadium
28	Korea Republic v Mexico	23-Jun	Rostov-on-Don	Rostov Arena
29	Belgium v Tunisia	23-Jun	Moscow	Spartak Stadium
30	England v Panama	24-Jun	Nizhny Novgorod	Nizhny Novgorod Stadium
31	Poland v Colombia	24-Jun	Kazan	Kazan Arena
32	Japan v Senegal	24-Jun	Ekaterinburg	Ekaterinburg Arena
33	Uruguay v Russia	25-Jun	Samara	Samara Arena
34	Saudi Arabia v Egypt	25-Jun	Volgograd	Volgograd Arena
35	Iran v Portugal	25-Jun	Saransk	Mordovia Arena
36	Spain v Morocco	25-Jun	Kaliningrad	Kaliningrad Stadium
37	Denmark v France	26-Jun	Moscow	Luzhniki Stadium
38	Australia v Peru	26-Jun	Sochi	Fisht Stadium
39	Nigeria v Argentina	26-Jun	Saint Petersburg	Saint Petersburg Stadium
40	Iceland v Croatia	26-Jun	Rostov-on-Don	Rostov Arena
41	Serbia v Brazil	27-Jun	Moscow	Spartak Stadium
42	Switzerland v Costa Rica	27-Jun	Nizhny Novgorod	Nizhny Novgorod Stadium
43	Korea Republic v Germany	27-Jun	Kazan	Kazan Arena
44	Mexico v Sweden	27-Jun	Ekaterinburg	Ekaterinburg Arena
45	England v Belgium	28-Jun	Kaliningrad	Kaliningrad Stadium
46	Panama v Tunisia	28-Jun	Saransk	Mordovia Arena
47	Japan v Poland	28-Jun	Volgograd	Volgograd Arena
48	Senegal v Columbia	28-Jun	Samara	Samara Arena
49	1A v 2B	30-Jun	Sochi	Fisht Stadium
50	1C v 2D	30-Jun	Kazan	Kazan Arena
51	1B v 2A	1-Jul	Moscow	Luzhniki Stadium
52	1D v 2C	1-Jul	Nizhny Novgorod	Nizhny Novgorod Stadium
53	1E v 2F	2-Jul	Samara	Samara Arena
54	1G v 2H	2-Jul	Rostov-on-Don	Rostov Arena
55	1F v 2E	3-Jul	Saint Petersburg	Saint Petersburg Stadium
56	1H v 2G	3-Jul	Moscow	Spartak Stadium
57	W49 v W50	6-Jul	Nizhny Novgorod	Nizhny Novgorod Stadium
58	W53 v W54	6-Jul	Kazan	Kazan Arena
59	W51 v W52	7-Jul	Sochi	Fisht Stadium
60	W55 v W56	7-Jul	Samara	Samara Arena
61	W57 v W58	10-Jul	Saint Petersburg	Saint Petersburg Stadium
62	W59 v W60	11-Jul	Moscow	Luzhniki Stadium
63	L61 v L62	14-Jul	Saint Petersburg	Saint Petersburg Stadium
64	W61 v W62	15-Jul	Moscow	Luzhniki Stadium
*/
    }

}
