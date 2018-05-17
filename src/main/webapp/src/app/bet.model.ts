export interface Bet {
  groupKey?: String;
  scores: ScoreBet[];
  id?: number;
}

export interface ScoreBet {
  home: number;
  away: number;
  id: number;
}
