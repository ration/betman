export interface Bet {
  groupKey?: string;
  scores: ScoreBet[];
  id?: number;
}

export interface ScoreBet {
  home: number;
  away: number;
  id: number;
}
