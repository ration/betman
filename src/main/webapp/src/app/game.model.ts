export interface Game {
  id: number;
  name: string;
  description: string;
  matches: Match[];
  teams: Team[];
}

export interface Match {
  id: number;
  home: number;
  away: number;
  date: string;
  description: string;
  homeGoals?: number;
  awayGoals?: number;
}

export class Team {
  name: string;
  iso: string;
  id: number;
}
