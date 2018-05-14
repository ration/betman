import {RouterModule, Routes} from '@angular/router';

import {BettingComponent} from './betting/betting.component';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {AuthGuard} from './auth.guard';
import {HomeComponent} from './home/home.component';
import {GroupComponent} from './group/group.component';
import {NewGroupComponent} from './new-group/new-group.component';
import {JoinComponent} from './join/join.component';

export const appRoutes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'betting', component: BettingComponent },
  { path: 'group/:group', component: GroupComponent },
  { path: 'group', component: GroupComponent },
  { path: 'new-group', component: NewGroupComponent },
  { path: 'join/:key', component: JoinComponent },



  // otherwise redirect to home
  { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(appRoutes);
