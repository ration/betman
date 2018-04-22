import { Component, OnInit } from '@angular/core';
import {AlertService} from '../alert.service';



@Component({
  moduleId: module.id.toString(),
  selector: 'app-alert-message',
  templateUrl: 'alert.component.html'
})

export class AlertComponent {
  message: any;

  constructor(private alertService: AlertService) { }

  ngOnInit() {
    this.alertService.getMessage().subscribe(message => { this.message = message; });
  }
}
