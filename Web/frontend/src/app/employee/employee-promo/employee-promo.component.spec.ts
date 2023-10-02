import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeePromoComponent } from './employee-promo.component';

describe('EmployeePromoComponent', () => {
  let component: EmployeePromoComponent;
  let fixture: ComponentFixture<EmployeePromoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmployeePromoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeePromoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
