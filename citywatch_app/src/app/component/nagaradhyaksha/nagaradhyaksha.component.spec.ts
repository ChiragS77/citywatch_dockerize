import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NagaradhyakshaComponent } from './nagaradhyaksha.component';

describe('NagaradhyakshaComponent', () => {
  let component: NagaradhyakshaComponent;
  let fixture: ComponentFixture<NagaradhyakshaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NagaradhyakshaComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NagaradhyakshaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
