import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../model/user';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css'],
})
export class AdminComponent implements OnInit {
  constructor(private router: Router, private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getAllPending().subscribe((res: any) => {
      this.users = res.users;
    });
  }

  users: Array<User>;

  acceptUser(userId) {
    const data = {
      userId: userId,
    };
    this.userService.accept(data).subscribe((res: any) => {
      if (res.message == 'Ok') {
        alert('Uspesno prihvacen korisnik.');
      } else if (res.message == 'Error') alert('Greska.');
      window.location.reload();
    });
  }

  rejectUser(userId) {
    const data = {
      userId: userId,
    };
    this.userService.reject(data).subscribe((res: any) => {
      if (res.message == 'Ok') {
        alert('Uspesno odbijen korisnik.');
      } else if (res.message == 'Error') alert('Greska.');
      window.location.reload();
    });
  }
}
