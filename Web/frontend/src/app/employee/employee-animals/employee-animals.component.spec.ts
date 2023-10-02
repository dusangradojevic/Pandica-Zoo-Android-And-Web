import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeAnimalsComponent } from './employee-animals.component';

describe('EmployeeAnimalsComponent', () => {
  let component: EmployeeAnimalsComponent;
  let fixture: ComponentFixture<EmployeeAnimalsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmployeeAnimalsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeeAnimalsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
