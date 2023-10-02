import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    if (localStorage.getItem('currentPage') == null) {
      localStorage.setItem('currentPage', 'login');
    }
  }

  username: string = '';
  password: string = '';
  msgError: string = '';

  login() {
    const data = {
      username: this.username,
      password: this.password,
    };

    this.userService.login(data).subscribe((res: any) => {
      if (res && res.errors != undefined && res.errors.length > 0) {
        this.msgError = res.errors[0];
      } else {
        localStorage.removeItem('currentPage');
        localStorage.setItem('loggedUser', JSON.stringify(res.user));
        switch (res.user.type) {
          case 'visitor': {
            this.router.navigate(['visitorTickets']);
            break;
          }
          case 'employee': {
            this.router.navigate(['employeeTickets']);
            break;
          }
          default: {
            this.router.navigate(['admin']);
            break;
          }
        }
      }
    });
  }
}
