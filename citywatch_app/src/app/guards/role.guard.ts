import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(private router : Router){}


 canActivate(route: ActivatedRouteSnapshot): boolean {

  const userRole = localStorage.getItem('role');
  const allowedRoles = route.data['roles'];

  // ✅ If role is allowed → allow access
  if (userRole && allowedRoles.includes(userRole)) {
    return true;
  }

  // ❌ If role NOT allowed → redirect correctly
  if (userRole) {
    const routeMap: any = {
      USER: '/',
      NAGARSEVAK: '/nagarsevak',
      NAGARADHYAKSHA: '/nagar-adhyaksha',
      ADMIN: '/admin',
      WORKER:'/worker'
    };

    this.router.navigate([routeMap[userRole] || '/login']);
  } else {
    // ❌ Not logged in
    this.router.navigate(['/login']);
  }

  return false;
}
  
}
