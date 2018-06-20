import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';
import {GroupsService} from '../groups.service';
import {Group} from '../group.model';
import {environment} from '../../environments/environment';
import {AuthenticationService} from '../authentication.service';
import {UserService} from '../user.service';
import {AlertService} from '../alert.service';
import {HttpErrorResponse} from '@angular/common/http';
import {Chart} from '../chart.model';
import {Chart as ChartJs} from 'chart.js';

@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css']
})
export class GroupComponent implements OnInit, OnDestroy {

  private sub: Subscription;
  group: Group = null;
  link: any;
  userDisplayName: string;
  groupId: string;
  chart: null;

  constructor(private route: ActivatedRoute,
              private groupService: GroupsService,
              private authService: AuthenticationService,
              private userService: UserService,
              private alertService: AlertService) {
  }

  isAdmin() {
    return this.group.admin === this.authService.currentUser().name;
  }

  ngOnInit() {
    this.groupId = this.route.snapshot.params.group || this.groupService.getActive();
    if (this.groupId) {
      this.loadGroup(this.groupId);
      this.groupService.chart(this.groupId).subscribe((chart: Chart) => {
        this.generateChart(chart);
      });
    }
  }

  ngOnDestroy() {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }

  onSubmit() {
    if (this.group.key) {
      this.groupService.updateDisplayName(this.group.key, this.userDisplayName).subscribe(() => {
        this.alertService.success('Saved', false, 1000);
        if (this.groupId) {
          this.loadGroup(this.groupId);
        }
      }, (error: HttpErrorResponse) => this.alertService.error(error.message));
    }
  }

  updateGroup() {
    this.groupService.update(this.group).subscribe(() => {
      this.alertService.success('Saved');
      this.loadGroup(this.groupId);
    });
  }

  private generateLink() {
    return environment.joinLink + '/join/' + this.group.key;
  }


  private loadGroup(groupId: string) {
    this.groupService.get(groupId).subscribe(ans => {
      this.group = ans;
      this.link = this.generateLink();
      if (this.group.userDisplayName) {
        this.userDisplayName = this.group.userDisplayName;
      } else {
        this.userDisplayName = this.authService.currentUser().name;
      }
    });
  }

  private getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }


  private generateChart(chart: Chart) {
    if (chart.points.size > 0) {
      // TODO the api lies. this should not be needed

      const labels: number[] = Array.from(new Map(Object.entries(chart.points.values().next().value)).values());

      const datasets = Array.from(chart.points.entries(), ([key, value]) => {
        const data = Array.from(new Map(Object.entries(value)).values());
        const color = this.getRandomColor();
        return {
          label: key,
          data: data,
          fill: false,
          backgroundColor: color,
          borderColor: color,
        };
      });

      const ctx = document.getElementById('canvas');

      this.chart = new ChartJs(ctx, {
        type: 'line',
        data: {
          labels: labels,
          datasets: datasets
        },
        options: {
          responsive: true,
          title: {
            display: true,
            text: 'Point accumulation'
          },
          tooltips: {
            mode: 'index',
            intersect: false,
          },
          hover: {
            mode: 'nearest',
            intersect: true
          },
          scales: {
            xAxes: [{
              display: true,
              scaleLabel: {
                display: true,
                labelString: 'Game'
              }
            }],
            yAxes: [{
              display: true,
              scaleLabel: {
                display: true,
                labelString: 'Points'
              }
            }]
          }
        }
      });
    }
  }
}
