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
  users?: string[];
  standings?: Score[];
}
