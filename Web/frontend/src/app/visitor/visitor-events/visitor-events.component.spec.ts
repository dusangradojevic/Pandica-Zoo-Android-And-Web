import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VisitorEventsComponent } from './visitor-events.component';

describe('VisitorEventsComponent', () => {
  let component: VisitorEventsComponent;
  let fixture: ComponentFixture<VisitorEventsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VisitorEventsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VisitorEventsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
