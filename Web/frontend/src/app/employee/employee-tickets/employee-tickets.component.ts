import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PromoPackage } from 'src/app/model/promo-package';
import { Ticket } from 'src/app/model/ticket';
import { User } from 'src/app/model/user';
import { PromoPackageService } from 'src/app/service/promo-package.service';
import { TicketService } from 'src/app/service/ticket.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-employee-tickets',
  templateUrl: './employee-tickets.component.html',
  styleUrls: ['./employee-tickets.component.css'],
})
export class EmployeeTicketsComponent implements OnInit {
  constructor(
    private router: Router,
    private ticketService: TicketService,
    private userService: UserService,
    private promoPackageService: PromoPackageService
  ) {}

  ngOnInit(): void {
    this.ticketService.getAllPending().subscribe((res: any) => {
      this.tickets = res.tickets;
      let userIds = [];
      let packageIds = [];
      for (let i = 0; i < this.tickets.length; ++i) {
        userIds.push(this.tickets[i].userId);
        packageIds.push(this.tickets[i].promoPackageId);
      }

      const dataUsers = {
        userIds,
      };
      this.userService.getByIds(dataUsers).subscribe((res: any) => {
        this.users = res.users;
      });

      const dataPackages = {
        packageIds,
      };
      this.promoPackageService.getByIds(dataPackages).subscribe((res: any) => {
        this.promoPackages = res.packages;
      });
    });
  }

  tickets: Array<Ticket> = new Array();
  users: Array<User> = new Array();
  promoPackages: Array<PromoPackage> = new Array();

  acceptTicket(userId, ticketId) {
    const data = {
      userId,
      ticketId,
    };
    this.ticketService.accept(data).subscribe((res: any) => {
      if (res.message == 'Ok') {
        alert('Uspesno prihvacen korisnik.');
      } else if (res.message == 'Error') alert('Greska.');
      window.location.reload();
    });
  }

  rejectTicket(userId, ticketId) {
    const data = {
      userId,
      ticketId,
    };
    this.ticketService.reject(data).subscribe((res: any) => {
      if (res.message == 'Ok') {
        alert('Uspesno odbijen korisnik.');
      } else if (res.message == 'Error') alert('Greska.');
      window.location.reload();
    });
  }
}
