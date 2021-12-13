import { Component } from '@angular/core';
import {Location} from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'sensor-chart';

  constructor(private location: Location) {
  }

  back(): void {
    this.location.back();
  }
}
