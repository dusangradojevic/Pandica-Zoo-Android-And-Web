import { Component, OnInit } from '@angular/core';
import { Event } from 'src/app/model/event';
import { User } from 'src/app/model/user';
import { EventService } from 'src/app/service/event.service';

@Component({
  selector: 'app-visitor-events',
  templateUrl: './visitor-events.component.html',
  styleUrls: ['./visitor-events.component.css'],
})
export class VisitorEventsComponent implements OnInit {
  constructor(private eventService: EventService) {}

  ngOnInit(): void {
    this.initLoggedUser();
    this.eventService.getAll().subscribe((res: any) => {
      this.events = res.events;
      this.initPictures();
      let eventIds = [];
      this.events.forEach((event) => {
        eventIds.push(event.id);
      });
      const data = {
        userId: this.user.id,
        eventIds,
      };
      this.eventService.getLikedEventsFlags(data).subscribe((res: any) => {
        this.likedEventsFlags = res.eventFlags;
      });
    });
  }

  user: User;
  events: Array<Event> = new Array();
  pictures: Map<number, string | ArrayBuffer> = new Map();
  eventLikes: Array<number> = new Array();
  likedEventsFlags: Array<boolean> = new Array();

  like(eventId) {
    const data = {
      userId: this.user.id,
      eventId,
    };
    this.eventService.like(data).subscribe((res: any) => {
      if (res.message == 'Error') {
        alert('Doslo je do neocekivane greske');
      } else {
        window.location.reload();
      }
    });
  }

  dislike(eventId) {
    const data = {
      userId: this.user.id,
      eventId,
    };
    this.eventService.dislike(data).subscribe((res: any) => {
      if (res.message == 'Error') {
        alert('Doslo je do neocekivane greske');
      } else {
        window.location.reload();
      }
    });
  }

  initPictures() {
    this.pictures.clear();
    this.events.forEach((event) => {
      const id = event.id;
      const photoName = '' + event.photo;

      const data = {
        photoName,
      };

      this.eventService.getPhoto(data).subscribe({
        next: (img: Blob) => {
          const reader = new FileReader();
          reader.readAsDataURL(img);
          reader.onload = (t) => {
            const picture: string | ArrayBuffer = t.target?.result;
            this.pictures.set(id, picture);
          };
        },
      });
    });
  }

  initLoggedUser() {
    this.user = null;
    if (localStorage.getItem('loggedUser') != null) {
      this.user = JSON.parse(localStorage.getItem('loggedUser'));
    }
  }
}
