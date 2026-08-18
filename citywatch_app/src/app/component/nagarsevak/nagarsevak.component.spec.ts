import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NagarsevakComponent } from './nagarsevak.component';

describe('NagarsevakComponent', () => {
  let component: NagarsevakComponent;
  let fixture: ComponentFixture<NagarsevakComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NagarsevakComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NagarsevakComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
