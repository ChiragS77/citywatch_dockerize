import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'timeAgo',
  pure: false // 🔥 auto updates UI
})
export class TimeAgoPipe implements PipeTransform {

  transform(value: any): string {
    if (!value) return '';

    let date: Date;

    // ✅ Handle array format (Spring Boot)
    if (Array.isArray(value)) {
      date = new Date(
        value[0],
        value[1] - 1,
        value[2],
        value[3] || 0,
        value[4] || 0,
        value[5] || 0
      );
    } else {
      date = new Date(value);
    }

    const now = new Date().getTime();
    const time = date.getTime();
    const diff = Math.floor((now - time) / 1000);

    if (diff < 60) return 'Just now';
    if (diff < 3600) return Math.floor(diff / 60) + ' min ago';
    if (diff < 86400) return Math.floor(diff / 3600) + ' hr ago';
    if (diff < 2592000) return Math.floor(diff / 86400) + ' days ago';
    if (diff < 31536000) return Math.floor(diff / 2592000) + ' months ago';

    return Math.floor(diff / 31536000) + ' years ago';
  }
}