import {SensorService} from "../app/service/sensor.service";


export const environment = {
  production: true,
  providers: [
    {provide: SensorService, useClass: SensorService},
  ],
  contextPath: '/sensor-service/'
};
