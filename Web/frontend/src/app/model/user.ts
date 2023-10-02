export class User {
  id: number = -1;
  username: string = '';
  password: string = '';
  firstname: string = '';
  lastname: string = '';
  phone: string = '';
  email: string = '';
  notifications: Array<string> = [];
  type: string = 'visitor';
  status: string = '';
}
