import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VisitorContactComponent } from './visitor-contact.component';

describe('VisitorContactComponent', () => {
  let component: VisitorContactComponent;
  let fixture: ComponentFixture<VisitorContactComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VisitorContactComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VisitorContactComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
