import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './component/dashboard/dashboard.component';
import { RegisterComponent } from './component/register/register.component';
import { LoginComponent } from './component/login/login.component';
import { NagarsevakComponent } from './component/nagarsevak/nagarsevak.component';
import { NagaradhyakshaComponent } from './component/nagaradhyaksha/nagaradhyaksha.component';
import { AdminComponent } from './component/admin/admin.component';
import { RoleGuard } from './guards/role.guard';
import { UserProfileComponent } from './component/user-profile/user-profile.component';
import { CitizenComponent } from './component/citizen/citizen.component';
import { ComplaintsComponent } from './component/complaints/complaints.component';
import { WorkerComponent } from './component/worker/worker.component';
import { NagarsevakNotifyComponent } from './component/nagarsevak-notify/nagarsevak-notify.component';
import { ManageNagarsevakComponent } from './component/admin/manage-nagarsevak/manage-nagarsevak.component';
import { ManageNagaradhyakshaComponent } from './component/admin/manage-nagaradhyaksha/manage-nagaradhyaksha.component';
import { ManageWorkerComponent } from './component/admin/manage-worker/manage-worker.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';

const routes: Routes = [
  { path: '', component: DashboardComponent ,
    canActivate:[RoleGuard],
    data:{roles:['USER']}},   

  { path:'nagarsevak',component:NagarsevakComponent,
              canActivate:[RoleGuard]
            ,data:{roles:['NAGARSEVAK']}}, 

  {path:'nagar-adhyaksha',component:NagaradhyakshaComponent,
    canActivate:[RoleGuard],
    data:{roles:['NAGARADHYAKSHA']}}  ,

  {path:'admin',component:AdminComponent,
    canActivate:[RoleGuard],
    data:{roles:['ADMIN']}},
    {path:'admin/nagarsevak',component:ManageNagarsevakComponent,canActivate:[RoleGuard],data:{roles:['ADMIN']}},
    {path:'admin/nagaradhyaksha',component:ManageNagaradhyakshaComponent,canActivate:[RoleGuard],data:{roles:['ADMIN']}},
    {path:'admin/worker',component:ManageWorkerComponent,canActivate:[RoleGuard],data:{roles:['ADMIN']}},
    
  { path: 'register', component: RegisterComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
{ path: 'reset-password', component: ResetPasswordComponent },
  
  { path: 'login', component: LoginComponent },
  {path:'profile',component:UserProfileComponent},
  { path: 'citizen', component: CitizenComponent ,canActivate:[RoleGuard], data:{roles:['NAGARSEVAK']} },
  {path:'complaints',component:ComplaintsComponent,canActivate:[RoleGuard],data:{roles:['NAGARSEVAK']}},
  {path:'worker',component:WorkerComponent,canActivate:[RoleGuard],data:{roles:['WORKER']}},
  {path:'notify',component:NagarsevakNotifyComponent,canActivate:[RoleGuard],data:{roles:['NAGARSEVAK']}}
];  


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
