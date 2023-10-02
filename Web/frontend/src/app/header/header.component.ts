import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../service/user.service';
import { User } from '../model/user';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  constructor(private router: Router, private userService: UserService) {}

  ngOnInit(): void {
    this.initLoggedUser();
    this.currentPage = localStorage.getItem('currentPage');
  }

  user: User = null;
  notifications: Array<string> = new Array();
  currentPage: string = '';

  logout() {
    localStorage.removeItem('loggedUser');
    this.router.navigate(['']);
  }

  initLoggedUser() {
    this.user = null;
    if (localStorage.getItem('loggedUser') != null) {
      this.user = JSON.parse(localStorage.getItem('loggedUser'));

      const data = {
        id: this.user.id,
      };
      this.userService.getById(data).subscribe((res: any) => {
        if (res.message == 'Ok') {
          localStorage.setItem('loggedUser', JSON.stringify(res.user));
          this.user = res.user;
        }
      });
    }
  }

  // GOTO - General

  goToHomePage() {
    if (localStorage.getItem('loggedUser') != null) {
      const userType = JSON.parse(localStorage.getItem('loggedUser')).type;
      this.router.navigate([userType]);
    } else {
      this.router.navigate(['']);
    }
  }

  goToLogin() {
    localStorage.setItem('currentPage', 'login');
    this.router.navigate(['login']);
  }

  goToRegistration() {
    localStorage.setItem('currentPage', 'register');
    this.router.navigate(['register']);
  }

  goToUserProfile() {
    this.router.navigate(['userProfile']);
  }

  // GOTO - Visitor

  goToVisitorTickets() {
    this.router.navigate(['visitorTickets']);
  }

  goToVisitorEvents() {
    this.router.navigate(['visitorEvents']);
  }

  goToVisitorAnimals() {
    this.router.navigate(['visitorAnimals']);
  }

  goToVisitorContact() {
    this.router.navigate(['visitorContact']);
  }

  // GOTO - Employee

  goToEmployeeTickets() {
    this.router.navigate(['employeeTickets']);
  }

  goToEmployeeAnimals() {
    this.router.navigate(['employeeAnimals']);
  }

  goToEmployeePromo() {
    this.router.navigate(['employeePromo']);
  }

  goToEmployeeEvents() {
    this.router.navigate(['employeeEvents']);
  }

  goToEmployeePromoCodes() {
    this.router.navigate(['employeePromoCodes']);
  }
}
