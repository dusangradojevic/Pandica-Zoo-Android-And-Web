import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../model/user';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
})
export class UserProfileComponent implements OnInit {
  constructor(private router: Router, private userService: UserService) {}

  ngOnInit(): void {
    this.initLoggedUser();
  }

  user: User;
  changingInfo: boolean = false;
  firstname: string;
  lastname: string;
  email: string;
  username: string;
  phone: string;
  confirmOldPassword: string;
  newPassword: string;
  confirmNewPassword: string;
  type: string;
  msgError: string;

  beginChangingInfo() {
    this.changingInfo = true;

    this.firstname = this.user.firstname;
    this.lastname = this.user.lastname;
    this.email = this.user.email;
    this.username = this.user.username;
    this.phone = this.user.phone;
    this.confirmOldPassword = '';
    this.newPassword = '';
    this.confirmNewPassword = '';
  }

  submitChangingInfo() {
    const data = {
      userId: this.user.id,
      oldUsername: this.user.username,
      oldEmail: this.user.email,
      oldPassword: this.user.password,
      firstname: this.firstname,
      lastname: this.lastname,
      email: this.email,
      username: this.username,
      phone: this.phone,
      confirmOldPassword: this.confirmOldPassword,
      newPassword: this.newPassword,
      confirmNewPassword: this.confirmNewPassword,
      type: this.type,
    };

    this.userService.update(data).subscribe((res: any) => {
      if (res && res.errors != undefined && res.errors.length) {
        this.msgError = res.errors[0];
      } else {
        alert('Podaci su uspesno azurirani.');
        if (this.newPassword) {
          this.logout();
        } else {
          localStorage.setItem('loggedUser', JSON.stringify(res.user));
          window.location.reload();
        }
      }
    });

    this.changingInfo = false;
  }

  cancelChangingInfo() {
    this.changingInfo = false;

    this.confirmOldPassword = '';
    this.newPassword = '';
    this.confirmNewPassword = '';
  }

  initLoggedUser() {
    this.user = null;
    if (localStorage.getItem('loggedUser') != null) {
      this.user = JSON.parse(localStorage.getItem('loggedUser'));

      const dataUser = {
        id: this.user.id,
      };
      this.userService.getById(dataUser).subscribe((res: any) => {
        if (res.message == 'Ok') {
          localStorage.setItem('loggedUser', JSON.stringify(res.user));
          this.user = res.user;
        }
      });
    }
  }

  logout() {
    localStorage.removeItem('loggedUser');
    this.router.navigate(['']);
  }
}
