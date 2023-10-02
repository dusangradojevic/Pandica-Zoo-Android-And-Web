import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeePromoCodesComponent } from './employee-promo-codes.component';

describe('EmployeePromoCodesComponent', () => {
  let component: EmployeePromoCodesComponent;
  let fixture: ComponentFixture<EmployeePromoCodesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmployeePromoCodesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeePromoCodesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
