import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HttpClientModule} from '@angular/common/http';


import {AppComponent} from './app.component';
import {BettingComponent} from './betting/betting.component';
import {GamesService} from './games.service';
import {FormsModule} from '@angular/forms';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';

import {AlertComponent} from './alert/alert.component';
import {routing} from './app.routing';
import {AlertService} from './alert.service';
import {UserService} from './user.service';
import {AuthGuard} from './auth.guard';
import {HomeComponent} from './home/home.component';
import {AuthenticationService} from './authentication.service';
import {fakeBackendProvider} from './fakes/fake-backend';


@NgModule({
  declarations: [
    AppComponent,
    BettingComponent,
    LoginComponent,
    RegisterComponent,
    AlertComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    routing
  ],
  providers: [GamesService, AlertService, UserService, AuthGuard, AuthenticationService,
    fakeBackendProvider],
  bootstrap: [AppComponent]
})
export class AppModule {
}
