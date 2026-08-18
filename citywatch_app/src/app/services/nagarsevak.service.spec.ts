import { TestBed } from '@angular/core/testing';

import { NagarsevakService } from './nagarsevak.service';

describe('NagarsevakService', () => {
  let service: NagarsevakService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NagarsevakService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
