import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NagarsevakNotifyComponent } from './nagarsevak-notify.component';

describe('NagarsevakNotifyComponent', () => {
  let component: NagarsevakNotifyComponent;
  let fixture: ComponentFixture<NagarsevakNotifyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NagarsevakNotifyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NagarsevakNotifyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
