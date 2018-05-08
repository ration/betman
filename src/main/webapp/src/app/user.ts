export class User {
  username: string;
  password?: string;
  displayNames?: {};
  activateGroup?: number;
  memberGroups?: number[] = [1];
}
