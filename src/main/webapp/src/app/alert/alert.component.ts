import {Component, OnInit} from '@angular/core';
import {AlertService} from '../alert.service';


@Component({
  moduleId: module.id.toString(),
  selector: 'app-alert-message',
  templateUrl: 'alert.component.html'
})

export class AlertComponent implements OnInit {
  message: any;

  constructor(private alertService: AlertService) {
  }

  ngOnInit() {
    this.alertService.getMessage().subscribe(message => {
      this.message = message;
      if (message && message.clearAfter > 0) {
        setTimeout(() => this.message = null, message.clearAfter);
      }
    });
  }
}
