import {Score} from './score.model';

export interface Groups {
  groups: Group[];
}

export class Group {
  name: string;
  key?: string;
  description: string;
  game: string;
  userDisplayName?: string;
  admin?: string;
  users?: string[];
  standings?: Score[];
  winnerPoints?: number;
  goalKingPoints?: number;
  teamGoalPoints?: number;
  exactScorePoints?: number;
}
