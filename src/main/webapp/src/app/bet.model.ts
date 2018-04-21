export class Bet {
  id: number;
  home: number;
  away: number;

  constructor(id: number, home: number, away: number) {
    this.id = id;
    this.home = home;
    this.away = away;
  }
}
