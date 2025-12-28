import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { BaseChartDirective } from 'ng2-charts';
import { Chart, registerables } from 'chart.js';
import { environment } from '../../environments/environment';

Chart.register(...registerables);

interface DashboardStats {
  totalClients: number;
  totalComptes: number;
  comptesEpargne: number;
  comptesCourant: number;
  soldeParClient: { [key: string]: number };
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, BaseChartDirective],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  loading = true;
  errorMessage = '';

  public pieChartData: any = {
    labels: ['Comptes Courants', 'Comptes Épargne'],
    datasets: [{
      data: [0, 0],
      backgroundColor: ['#7C3AED', '#A78BFA'],
      borderWidth: 2,
      borderColor: '#ffffff'
    }]
  };
  public pieChartType: any = 'pie';
  public pieChartOptions: any = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          font: { size: 14 },
          padding: 15,
          color: '#7C3AED'
        }
      }
    }
  };

  public barChartData: any = {
    labels: [],
    datasets: [{
      label: 'Solde (MAD)',
      data: [],
      backgroundColor: '#8B5CF6',
      borderColor: '#8B5CF6',
      borderWidth: 2,
      borderRadius: 8
    }]
  };
  public barChartType: any = 'bar';
  public barChartOptions: any = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      y: {
        beginAtZero: true,
        ticks: {
          color: '#7C3AED',
          callback: function(value: number) {
            return value.toLocaleString() + ' MAD';
          }
        },
        grid: {
          color: 'rgba(124, 58, 237, 0.3)'
        }
      },
      x: {
        ticks: {
          color: '#7C3AED'
        },
        grid: {
          color: 'rgba(124, 58, 237, 0.2)'
        }
      }
    },
    plugins: {
      legend: {
        display: true,
        position: 'top',
        labels: {
          color: '#7C3AED'
        }
      }
    }
  };

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadDashboardStats();
  }

  loadDashboardStats(): void {
    console.log('📊 Loading dashboard stats...');

    this.http.get<DashboardStats>(`${environment.backendHost}/api/dashboard/stats`)
      .subscribe({
        next: (data) => {
          console.log('✅ Dashboard data received:', data);
          this.stats = data;
          this.updateCharts(data);
          this.loading = false;
        },
        error: (err) => {
          console.error('❌ Erreur chargement dashboard:', err);
          this.errorMessage = 'Erreur lors du chargement des statistiques';
          this.loading = false;
        }
      });
  }

  updateCharts(data: DashboardStats): void {
    console.log('📈 Updating charts with data:', data);

    this.pieChartData.datasets[0].data = [
      data.comptesCourant,
      data.comptesEpargne
    ];

    if (data.soldeParClient) {
      this.barChartData.labels = Object.keys(data.soldeParClient);
      this.barChartData.datasets[0].data = Object.values(data.soldeParClient);
    }
  }
}
