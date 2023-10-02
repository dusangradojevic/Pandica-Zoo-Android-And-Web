import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VisitorAnimalsComponent } from './visitor-animals.component';

describe('VisitorAnimalsComponent', () => {
  let component: VisitorAnimalsComponent;
  let fixture: ComponentFixture<VisitorAnimalsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VisitorAnimalsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VisitorAnimalsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
