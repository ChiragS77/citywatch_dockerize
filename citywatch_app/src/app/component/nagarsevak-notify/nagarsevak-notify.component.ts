import { Component } from '@angular/core';
import { NagarsevakService } from 'src/app/services/nagarsevak.service';
declare var Razorpay: any;


@Component({
  selector: 'app-nagarsevak-notify',
  templateUrl: './nagarsevak-notify.component.html',
  styleUrls: ['./nagarsevak-notify.component.css']
})
export class NagarsevakNotifyComponent {


  notifications: any[] = [];
  selectedProofImage: string | null = null;

  constructor(private service: NagarsevakService) {}

  ngOnInit(): void {
    this.loadNotifications();
  }

  loadNotifications() {
    this.service.getPaymentNotifications().subscribe({
      next: (res) => this.notifications = res,
      error: (err) => console.error(err)
    });
  }

  payWorker(n: any) {
  this.service.createOrder(n.id).subscribe((order: any) => {

    const options: any = {
      key: order.key,
      amount: order.amount,
      currency: order.currency,
      name: 'CityWatch',
      description: 'Worker Payment',
      order_id: order.orderId,

      handler: (response: any) => {
        this.service.markPaymentSuccess(n.id).subscribe(() => {
          alert('Payment successful ✅');
          this.loadNotifications();
        });
      }
    };

    const rzp = new Razorpay(options);
    rzp.open();
  });
}

openProof(imageUrl: string) {
  this.selectedProofImage = imageUrl;
}

closeProof() {
  this.selectedProofImage = null;
}

}
