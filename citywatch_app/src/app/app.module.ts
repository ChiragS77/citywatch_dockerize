import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegisterComponent } from './component/register/register.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule} from '@angular/common/http';
import { LoginComponent } from './component/login/login.component';
import { ReactiveFormsModule } from '@angular/forms';
import { DashboardComponent } from './component/dashboard/dashboard.component';
import { NagarsevakComponent } from './component/nagarsevak/nagarsevak.component';
import { NagaradhyakshaComponent } from './component/nagaradhyaksha/nagaradhyaksha.component';
import { AdminComponent } from './component/admin/admin.component';
import { UserProfileComponent } from './component/user-profile/user-profile.component';
import { NgChartsModule } from 'ng2-charts';
import { CitizenComponent } from './component/citizen/citizen.component';
import { ComplaintsComponent } from './component/complaints/complaints.component';
import { TimeAgoPipe } from './shared/time-ago.pipe';
import { WorkerComponent } from './component/worker/worker.component';
import { NagarsevakNotifyComponent } from './component/nagarsevak-notify/nagarsevak-notify.component';
import { ManageNagarsevakComponent } from './component/admin/manage-nagarsevak/manage-nagarsevak.component';
import { ManageNagaradhyakshaComponent } from './component/admin/manage-nagaradhyaksha/manage-nagaradhyaksha.component';
import { ManageWorkerComponent } from './component/admin/manage-worker/manage-worker.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { NgClass } from '@angular/common';

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    DashboardComponent,
    NagarsevakComponent,
    NagaradhyakshaComponent,
    AdminComponent,
    UserProfileComponent,
    CitizenComponent,
    ComplaintsComponent,
    TimeAgoPipe,
    WorkerComponent,
    NagarsevakNotifyComponent,
    ManageNagarsevakComponent,
    ManageNagaradhyakshaComponent,
    ManageWorkerComponent,
    ForgotPasswordComponent,
    ResetPasswordComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgChartsModule,
    NgClass

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
