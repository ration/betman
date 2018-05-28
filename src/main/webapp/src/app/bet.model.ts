export interface Bet {
  groupKey?: string;
  scores: ScoreBet[]; // This should be changed to some kind of map in the api, but it seems difficult
  id?: number;
  winner?: number;
  goalKing?: string;
}

export interface ScoreBet {
  home: number;
  away: number;
  id: number;
}
