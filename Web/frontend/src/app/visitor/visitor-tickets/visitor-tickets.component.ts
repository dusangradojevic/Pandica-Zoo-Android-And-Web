import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PromoPackage } from 'src/app/model/promo-package';
import { User } from 'src/app/model/user';
import { PromoPackageService } from 'src/app/service/promo-package.service';
import { TicketService } from 'src/app/service/ticket.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-visitor-tickets',
  templateUrl: './visitor-tickets.component.html',
  styleUrls: ['./visitor-tickets.component.css'],
})
export class VisitorTicketsComponent implements OnInit {
  constructor(
    private userService: UserService,
    private promoPackageService: PromoPackageService,
    private ticketService: TicketService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initLoggedUser();
    this.promoPackageService.getAll().subscribe((res: any) => {
      this.promoPackages = res.packages;
      this.promoPackages.sort((p1, p2) => {
        if (p1.type == p2.type && (p1.price == 0 || p2.price == 0)) {
          return p2.price == 0 ? -1 : 1;
        } else if (p1.type != p2.type) {
          return p2.type == 'single' ? 1 : -1;
        } else {
          return 0;
        }
      });
      let index = 1;
      this.promoPackages.forEach((p) => {
        this.promoPackageNumbers.push(p.price ? index++ : -1);
      });
    });
  }

  user: User;
  promoPackages: Array<PromoPackage> = new Array();
  promoPackageNumbers: Array<number> = new Array();
  promoPackageNumber: number;
  quantity: number;
  promoCode: string;
  msgError: string;

  buyTicket() {
    if (this.promoPackageNumber == undefined || this.quantity == undefined) {
      alert('Redni broj paketa i kolicina moraju biti uneti.');
      return;
    }

    let promoPackageId = -1;
    for (let i = 0; i < this.promoPackageNumbers.length; ++i) {
      if (this.promoPackageNumbers[i] == this.promoPackageNumber) {
        promoPackageId = this.promoPackages[i].id;
        break;
      }
    }

    if (promoPackageId == -1) {
      alert('Redni broj mora imati vrednost nekog od ponudjenih paketa.');
      return;
    }

    const data = {
      id: promoPackageId,
    };
    this.promoPackageService.getById(data).subscribe((res: any) => {
      const promoPackage = res.promoPackage;
      const data = {
        userId: this.user.id,
        promoPackageId: promoPackage.id,
        quantity: this.quantity,
        price: this.quantity * promoPackage.price,
        promoCode: this.promoCode,
        status: 'pending',
      };
      this.ticketService.insert(data).subscribe((res: any) => {
        if (res && res.errors != undefined && res.errors.length) {
          this.msgError = res.errors[0];
        } else {
          alert('Uspesno ste poslali zahtev za kupovinu ulaznice.');
          window.location.reload();
        }
      });
    });
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
}
