export class User {
  id: number;
  username: string;
  password: string;
  displayNames?: {};
  activateGroup?: number;
  memberGroups: number[] = [1];
}
