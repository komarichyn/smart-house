import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ChartComponent} from "./component/chart/chart.component";
import {AppComponent} from "./app.component";
import {DashboardComponent} from "./component/dashboard/dashboard.component";
import {OverviewComponent as DeviceOverviewComponent}  from "./component/device/overview/overview.component"

const routes: Routes = [
  // {path: '', redirectTo: '/', pathMatch: 'full'},
  {path: '', component: DashboardComponent},
  {path: 'chart', component: ChartComponent},
  {path: 'device', component: DeviceOverviewComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
