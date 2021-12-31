import {SensorService} from "../app/service/sensor.service";
import {DeviceService} from "../app/service/device.service";


export const environment = {
  production: true,
  providers: [
    {provide: SensorService, useClass: SensorService},
    {provide: DeviceService, useClass: DeviceService}
  ],
  contextPath: '/sensor-service/'
};
