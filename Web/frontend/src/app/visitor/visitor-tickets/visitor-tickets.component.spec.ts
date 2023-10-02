import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VisitorTicketsComponent } from './visitor-tickets.component';

describe('VisitorTicketsComponent', () => {
  let component: VisitorTicketsComponent;
  let fixture: ComponentFixture<VisitorTicketsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VisitorTicketsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VisitorTicketsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
