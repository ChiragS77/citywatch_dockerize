import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageNagaradhyakshaComponent } from './manage-nagaradhyaksha.component';

describe('ManageNagaradhyakshaComponent', () => {
  let component: ManageNagaradhyakshaComponent;
  let fixture: ComponentFixture<ManageNagaradhyakshaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManageNagaradhyakshaComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageNagaradhyakshaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
