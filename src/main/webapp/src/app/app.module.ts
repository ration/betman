import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';


import {AppComponent} from './app.component';
import {BettingComponent} from './betting/betting.component';
import {GamesService} from './games.service';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';

import {AlertComponent} from './alert/alert.component';
import {routing} from './app.routing';
import {AlertService} from './alert.service';
import {UserService} from './user.service';
import {AuthGuard} from './auth.guard';
import {HomeComponent} from './home/home.component';
import {AuthenticationService} from './authentication.service';
import {fakeBackendProvider} from './fakes/FakeBackendInterceptor';
import {GroupComponent} from './group/group.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {environment} from 'environments/environment';
import {NewGroupComponent} from './new-group/new-group.component';
import {GroupsService} from './groups.service';
import {JoinComponent} from './join/join.component';


const providers: Array<any> = [
  GamesService, AlertService, UserService, AuthGuard, AuthenticationService, GroupsService
];

if (!environment.production) {
  providers.push(
    fakeBackendProvider
  );
}


@NgModule({
  declarations: [
    AppComponent,
    BettingComponent,
    LoginComponent,
    RegisterComponent,
    AlertComponent,
    HomeComponent,
    GroupComponent,
    NewGroupComponent,
    JoinComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    routing,
    NgbModule.forRoot()
  ],
  providers: [providers],
  bootstrap: [AppComponent]
})
export class AppModule {
}
