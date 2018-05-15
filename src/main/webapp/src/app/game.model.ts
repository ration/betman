export interface Game {
  id: number;
  name: string;
  description: string;
  matches: Match[];
}

export interface Match {
  id: number;
  home: Team;
  away: Team;
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
