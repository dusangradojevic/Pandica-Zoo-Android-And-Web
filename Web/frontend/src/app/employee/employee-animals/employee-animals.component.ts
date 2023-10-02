import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Animal } from 'src/app/model/animal';
import { AnimalService } from 'src/app/service/animal.service';

@Component({
  selector: 'app-employee-animals',
  templateUrl: './employee-animals.component.html',
  styleUrls: ['./employee-animals.component.css'],
})
export class EmployeeAnimalsComponent implements OnInit {
  constructor(private animalService: AnimalService, private router: Router) {}

  ngOnInit(): void {
    this.animalService.getAll().subscribe((res: any) => {
      this.animals = res.animals;
      this.initPictures();
    });
  }

  name: string;
  description: string;
  photo: File;
  msgError: string;
  animals: Animal[] = new Array();
  pictures: Map<number, string | ArrayBuffer> = new Map();
  changingInfo: number = -1;
  changePhoto: File;
  changeName: string;
  changeDescription: string;

  addAnimal() {
    const data = new FormData();
    data.set('name', this.name);
    data.set('description', this.description);
    if (this.photo != undefined) {
      data.set('photo', this.photo);
    }

    this.animalService.insert(data).subscribe((res: any) => {
      if (res && res.errors != undefined && res.errors.length) {
        this.msgError = res.errors[0];
      } else {
        alert('Uspesno ste dodali novu zivotinju!');
        window.location.reload();
      }
    });
  }

  beginChangingInfo(animalIndex) {
    this.changingInfo = animalIndex;

    this.changeName = this.animals[animalIndex].name;
    this.changeDescription = this.animals[animalIndex].description;
  }

  submitChangingInfo(animalId) {
    const data = new FormData();
    data.set('animalId', animalId.toString());
    data.set('name', this.changeName);
    data.set('description', this.changeDescription);
    if (this.changePhoto != undefined) {
      data.set('photo', this.changePhoto);
    }

    this.animalService.update(data).subscribe((res: any) => {
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

  deleteAnimal(animalIndex, animalId) {
    const data = {
      animalIndex,
      animalId,
    };
    this.animalService.remove(data).subscribe((res: any) => {
      if (res.message == 'Ok') {
        alert('Uspesno izbrisana zivotinja.');
      } else if (res.message == 'Error') {
        alert(res.errorMessage);
      }
      window.location.reload();
    });
  }

  initPictures() {
    this.pictures.clear();
    this.animals.forEach((animal) => {
      const id = animal.id;
      const photoName = '' + animal.photo;

      const data = {
        photoName,
      };

      this.animalService.getPhoto(data).subscribe({
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
