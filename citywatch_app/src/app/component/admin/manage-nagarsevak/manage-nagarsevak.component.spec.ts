import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageNagarsevakComponent } from './manage-nagarsevak.component';

describe('ManageNagarsevakComponent', () => {
  let component: ManageNagarsevakComponent;
  let fixture: ComponentFixture<ManageNagarsevakComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManageNagarsevakComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageNagarsevakComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
