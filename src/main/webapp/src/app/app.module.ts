import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';


import { AppComponent } from './app.component';
import { BettingComponent } from './betting/betting.component';
import {GamesService} from "./games.service";


@NgModule({
  declarations: [
    AppComponent,
    BettingComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [GamesService],
  bootstrap: [AppComponent]
})
export class AppModule { }
