import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Animal } from 'src/app/model/animal';
import { AnimalService } from 'src/app/service/animal.service';

@Component({
  selector: 'app-visitor-animals',
  templateUrl: './visitor-animals.component.html',
  styleUrls: ['./visitor-animals.component.css'],
})
export class VisitorAnimalsComponent implements OnInit {
  constructor(private animalService: AnimalService, private router: Router) {}

  ngOnInit(): void {
    this.animalService.getAll().subscribe((res: any) => {
      this.animals = res.animals;
      this.animalCount = this.animals.length;
      this.initPictures();
    });
  }

  animals: Array<Animal> = new Array();
  animalCount: number = 0;
  animalIterator: number = 0;
  pictures: Map<number, string | ArrayBuffer> = new Map();

  nextSlide() {
    if (this.animalIterator + 5 <= this.animalCount - 1) {
      this.animalIterator += 5;
    }
  }

  prevSlide() {
    if (this.animalIterator - 5 >= 0) {
      this.animalIterator -= 5;
    }
  }

  goToAnimalDetails(animal) {
    localStorage.setItem('currentAnimal', JSON.stringify(animal));
    this.router.navigate(['animalDetails']);
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
}
