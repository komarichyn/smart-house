import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { NgChartsModule } from 'ng2-charts';
import { HttpClientModule } from '@angular/common/http';
import { environment } from 'src/environments/environment';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ChartComponent } from './component/chart/chart.component';
import {DatePipe} from "@angular/common";
import { DashboardComponent } from './component/dashboard/dashboard.component';
import { OverviewComponent as DeviceOverviewComponent } from './component/device/overview/overview.component';
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    AppComponent,
    ChartComponent,
    DashboardComponent,
    DeviceOverviewComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgChartsModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    ...environment.providers,
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
