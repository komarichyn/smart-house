import { Component, OnInit } from '@angular/core';
import {DeviceService} from "../../../service/device.service";
import {Device} from "../../../entity/device";

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.css']
})
export class OverviewComponent implements OnInit {

  constructor(private deviceService: DeviceService) { }

  devices: Device[] = [];

  ngOnInit(): void {
    this.deviceService.getDevices().subscribe(data => {
      this.devices = data.content;
    });
  }

  onchange(id:number){
    this.devices.forEach(dev =>{
      if(id === dev.id){
        this.deviceService.updateDevice(dev).subscribe(data => {
          console.log(data);
        });
      }
    });
  }

}
