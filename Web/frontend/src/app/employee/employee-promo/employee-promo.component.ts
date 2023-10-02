import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PromoPackage } from 'src/app/model/promo-package';
import { PromoPackageService } from 'src/app/service/promo-package.service';

@Component({
  selector: 'app-employee-promo',
  templateUrl: './employee-promo.component.html',
  styleUrls: ['./employee-promo.component.css'],
})
export class EmployeePromoComponent implements OnInit {
  constructor(
    private promoPackageService: PromoPackageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.promoPackageService.getAll().subscribe((res: any) => {
      this.promoPackages = res.packages;
    });
  }

  name: string;
  price: number;
  type: string = 'single';
  msgError: string;
  promoPackages: PromoPackage[] = new Array();
  changingInfo: number = -1;
  changeName: string;
  changePrice: number;
  changeType: string;

  addPromoPackage() {
    if (this.price == undefined) {
      this.price = 0;
    }

    const data = {
      name: this.name,
      price: this.price,
      type: this.type,
    };
    this.promoPackageService.insert(data).subscribe((res: any) => {
      if (res && res.errors != undefined && res.errors.length) {
        this.msgError = res.errors[0];
      } else {
        alert('Uspesno ste dodali novi paket!');
        window.location.reload();
      }
    });
  }

  beginChangingInfo(packageIndex) {
    this.changingInfo = packageIndex;

    this.changeName = this.promoPackages[packageIndex].name;
    this.changePrice = this.promoPackages[packageIndex].price;
    this.changeType = this.promoPackages[packageIndex].type;
  }

  submitChangingInfo(packageId) {
    const data = {
      packageId,
      name: this.changeName,
      price: this.changePrice,
      type: this.changeType,
    };

    this.promoPackageService.update(data).subscribe((res: any) => {
      if (res && res.errors != undefined && res.errors.length) {
        this.msgError = res.errors[0];
      } else {
        alert('Podaci su uspesno azurirani.');
        window.location.reload();
      }
    });

    this.changingInfo = -1;
  }

  cancelChangingInfo() {
    this.changingInfo = -1;
  }

  deletePromoPackage(packageId) {
    const data = {
      packageId,
    };
    this.promoPackageService.remove(data).subscribe((res: any) => {
      if (res.message == 'Ok') {
        alert('Uspesno izbrisan promo paket.');
      } else if (res.message == 'Error') {
        alert(res.errorMessage);
      }
      window.location.reload();
    });
  }
}
