import { Component, OnInit } from '@angular/core';
import { PromoCodeService } from 'src/app/service/promo-code.service';

@Component({
  selector: 'app-employee-promo-codes',
  templateUrl: './employee-promo-codes.component.html',
  styleUrls: ['./employee-promo-codes.component.css'],
})
export class EmployeePromoCodesComponent implements OnInit {
  constructor(private promoCodeService: PromoCodeService) {}

  ngOnInit(): void {}

  code: string = '';
  discount: number;
  quantity: number;

  addPromoCode() {
    if (
      this.code == '' ||
      this.discount == undefined ||
      this.discount <= 0 ||
      this.quantity == undefined ||
      this.quantity < 0
    ) {
      alert('Niste uneli validne podatke.');
      return;
    }

    const data = {
      code: this.code,
      discount: this.discount,
      quantity: this.quantity,
    };
    this.promoCodeService.insert(data).subscribe((res: any) => {
      if (res.message == 'Error') {
        alert('Doslo je do neocekivane greske.');
      } else {
        alert('Uspesno ste dodali novi promo kod.');
        window.location.reload();
      }
    });
  }
}
