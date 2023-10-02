import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Event } from 'src/app/model/event';
import { EventService } from 'src/app/service/event.service';

@Component({
  selector: 'app-employee-events',
  templateUrl: './employee-events.component.html',
  styleUrls: ['./employee-events.component.css'],
})
export class EmployeeEventsComponent implements OnInit {
  constructor(private eventService: EventService, private router: Router) {}

  ngOnInit(): void {
    this.eventService.getAll().subscribe((res: any) => {
      this.events = res.events;
      this.initPictures();
    });
  }

  name: string;
  description: string;
  photo: File;
  msgError: string;
  events: Event[] = new Array();
  pictures: Map<number, string | ArrayBuffer> = new Map();
  changingInfo: number = -1;
  changePhoto: File;
  changeName: string;
  changeDescription: string;

  addEvent() {
    const data = new FormData();
    data.set('name', this.name);
    data.set('description', this.description);
    if (this.photo != undefined) {
      data.set('photo', this.photo);
    }

    this.eventService.insert(data).subscribe((res: any) => {
      if (res && res.errors != undefined && res.errors.length) {
        this.msgError = res.errors[0];
      } else {
        alert('Uspesno ste dodali novi dogadjaj!');
        window.location.reload();
      }
    });
  }

  beginChangingInfo(eventIndex) {
    this.changingInfo = eventIndex;

    this.changeName = this.events[eventIndex].name;
    this.changeDescription = this.events[eventIndex].description;
  }

  submitChangingInfo(eventId) {
    const data = new FormData();
    data.set('eventId', eventId.toString());
    data.set('name', this.changeName);
    data.set('description', this.changeDescription);
    if (this.changePhoto != undefined) {
      data.set('photo', this.changePhoto);
    }

    this.eventService.update(data).subscribe((res: any) => {
      if (res && res.errors != undefined && res.errors.length) {
        this.msgError = res.errors[0];
      } else {
        alert('Podaci su uspesno azurirani.');
        window.location.reload();
      }
    });

    this.changingInfo = -1;
  }

  cancelChangingInfo() {
    this.changingInfo = -1;
  }

  deleteEvent(eventIndex, eventId) {
    const data = {
      eventIndex,
      eventId,
    };
    this.eventService.remove(data).subscribe((res: any) => {
      if (res.message == 'Ok') {
        alert('Uspesno izbrisan dogadjaj.');
      } else if (res.message == 'Error') {
        alert(res.errorMessage);
      }
      window.location.reload();
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

  changeImage(event) {
    if (event.target.files.length) {
      this.changePhoto = event.target.files[0];
    }
  }

  selectImage(event) {
    if (event.target.files.length) {
      this.photo = event.target.files[0];
    }
  }
}
