export class User {
  name: string;
  password?: string;
  displayNames?: {};
  token?: string;
  activateGroup?: string;
  memberGroups?: number[];
  admin?: boolean;
}
