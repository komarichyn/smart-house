import {Component, OnInit, ViewChild} from '@angular/core';
import {ChartConfiguration, ChartType} from 'chart.js';
import {BaseChartDirective} from 'ng2-charts';
import {SensorService} from "../../service/sensor.service"
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})


export class ChartComponent implements OnInit {
  @ViewChild(BaseChartDirective) chart?: BaseChartDirective;

  constructor(private sensorService: SensorService,
              private datePipe: DatePipe) {
  }

  ngOnInit(): void {
    this.loadData();
  }


  public lineChartData: ChartConfiguration['data'] = {
    datasets: [],
    labels: []
  };

  public lineChartOptions: ChartConfiguration['options'] = {
    elements: {
      line: {
        tension: 0.5
      }
    },
    responsive: true,
    maintainAspectRatio: false,

    scales: {
      // We use this empty structure as a placeholder for dynamic theming.
      x: {},
      'y-axis-0':
        {
          position: 'left',
        },
      'y-axis-1': {
        position: 'right',
        grid: {
          color: 'rgba(255,0,0,0.3)',
        },
        ticks: {
          color: 'red'
        }
      }
    },

    plugins: {
      legend: {display: true}
    }
  };
  public lineChartType: ChartType = 'line';

  public pageNo: number = 0;
  public pageSize: number = 150;
  public totalRecords:number = this.pageSize+ 1;

  public loadData(): void {

    this.sensorService.getSensorData(1, this.pageNo, this.pageSize).subscribe(data => {

      let records = data.content;
      if(data.pagination?.page){
        this.pageNo = data.pagination?.page;
        this.totalRecords = data.pagination?.total
        this.pageSize = data.pagination.size
      }


      let newArray: Array<number> = [];
      let humArray: Array<number> = [];

      this.lineChartData.labels?.splice(0, this.lineChartData.labels?.length);

      // records.reverse();

      records.forEach((el, index) => {
        let val = parseFloat(el.value);
        if (el.type == 1) {//temperature
          if (val < 50 && val > 10) {
            newArray.push(val);
            this.lineChartData.labels?.push(this.datePipe.transform(el.eventDate, "dd-MM-yyyy HH:mm"));
          }
        } else {//humidity
          if (val < 90 && val > 10) {
            humArray.push(val);
          }
        }

      });
      const newData = {
        data: newArray,
        label: 'Temperatura'
      };
      const humData = {
        data: humArray,
        label: "Humidity"
      }
      this.lineChartData.datasets.length = 0;
      this.lineChartData.datasets.push({...newData});
      this.lineChartData.datasets.push({...humData});

      this.chart?.update();
    });

  }


  back() {
    if(this.pageNo > 0){
      this.pageNo--;
      this.loadData();
    }
  }

  next() {
    if((this.pageNo + 1) * this.pageSize < this.totalRecords){
      this.pageNo++;
      this.loadData();
    }
  }
}
