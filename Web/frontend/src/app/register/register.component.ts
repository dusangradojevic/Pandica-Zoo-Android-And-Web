import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';
import { Router } from '@angular/router';
import { User } from '../model/user';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {}

  user: User = new User();
  confirmPassword: string = '';
  msgError: string = '';

  register() {
    const data = {
      username: this.user.username,
      password: this.user.password,
      confirmPassword: this.confirmPassword,
      firstname: this.user.firstname,
      lastname: this.user.lastname,
      phone: this.user.phone,
      email: this.user.email,
      type: this.user.type,
      status: 'pending',
    };

    this.userService.register(data).subscribe((res: any) => {
      if (res && res.errors != undefined && res.errors.length) {
        this.msgError = res.errors[0];
      } else {
        alert('Zahtev za registraciju uspesno poslat!');
        window.location.reload();
      }
    });
  }
}
