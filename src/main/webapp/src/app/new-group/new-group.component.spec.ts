import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {NewGroupComponent} from './new-group.component';
import {FormsModule} from '@angular/forms';
import {GroupsService} from '../groups.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {AlertService} from '../alert.service';

describe('NewGroupComponent', () => {
  let component: NewGroupComponent;
  let fixture: ComponentFixture<NewGroupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [NewGroupComponent],
      imports: [FormsModule, HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [GroupsService, AlertService]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

});
