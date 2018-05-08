import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AlertComponent} from './alert.component';
import {AlertService} from '../alert.service';
import {RouterTestingModule} from '@angular/router/testing';

describe('AlertComponent', () => {
  let component: AlertComponent;
  let fixture: ComponentFixture<AlertComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AlertComponent],
      imports: [RouterTestingModule.withRoutes([])],
      providers: [AlertService,]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
