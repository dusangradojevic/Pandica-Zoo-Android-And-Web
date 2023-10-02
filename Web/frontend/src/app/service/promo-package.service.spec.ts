import { TestBed } from '@angular/core/testing';

import { PromoPackageService } from './promo-package.service';

describe('PromoPackageService', () => {
  let service: PromoPackageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PromoPackageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
