import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { FooterComponent } from './footer/footer.component';
import { HeaderComponent } from './header/header.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { VisitorTicketsComponent } from './visitor/visitor-tickets/visitor-tickets.component';
import { VisitorEventsComponent } from './visitor/visitor-events/visitor-events.component';
import { VisitorAnimalsComponent } from './visitor/visitor-animals/visitor-animals.component';
import { VisitorContactComponent } from './visitor/visitor-contact/visitor-contact.component';
import { EmployeeTicketsComponent } from './employee/employee-tickets/employee-tickets.component';
import { EmployeeAnimalsComponent } from './employee/employee-animals/employee-animals.component';
import { EmployeePromoComponent } from './employee/employee-promo/employee-promo.component';
import { EmployeeEventsComponent } from './employee/employee-events/employee-events.component';
import { AdminComponent } from './admin/admin.component';
import { AnimalDetailsComponent } from './animal-details/animal-details.component';
import { EmployeePromoCodesComponent } from './employee/employee-promo-codes/employee-promo-codes.component';

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HeaderComponent,
    LoginComponent,
    RegisterComponent,
    UserProfileComponent,
    VisitorTicketsComponent,
    VisitorEventsComponent,
    VisitorAnimalsComponent,
    VisitorContactComponent,
    EmployeeTicketsComponent,
    EmployeeAnimalsComponent,
    EmployeePromoComponent,
    EmployeeEventsComponent,
    AdminComponent,
    AnimalDetailsComponent,
    EmployeePromoCodesComponent,
  ],
  imports: [BrowserModule, AppRoutingModule, HttpClientModule, FormsModule],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
