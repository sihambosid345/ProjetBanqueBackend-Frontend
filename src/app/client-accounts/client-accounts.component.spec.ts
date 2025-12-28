import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientAccountsComponent } from './client-accounts.component';

describe('ClientAccounts', () => {
  let component: ClientAccountsComponent;
  let fixture: ComponentFixture<ClientAccountsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientAccountsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientAccountsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
