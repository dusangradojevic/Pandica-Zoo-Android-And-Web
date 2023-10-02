import { Component, OnInit } from '@angular/core';
import { CommentService } from '../service/comment.service';
import { Animal } from '../model/animal';
import { AnimalService } from '../service/animal.service';
import { UserService } from '../service/user.service';
import { User } from '../model/user';
import { Comment } from '../model/comment';

@Component({
  selector: 'app-animal-details',
  templateUrl: './animal-details.component.html',
  styleUrls: ['./animal-details.component.css'],
})
export class AnimalDetailsComponent implements OnInit {
  constructor(
    private userService: UserService,
    private animalService: AnimalService,
    private commentService: CommentService
  ) {}

  ngOnInit(): void {
    this.animal = JSON.parse(localStorage.getItem('currentAnimal'));
    const dataPhoto = {
      photoName: this.animal.photo,
    };
    this.animalService.getPhoto(dataPhoto).subscribe({
      next: (img: Blob) => {
        const reader = new FileReader();
        reader.readAsDataURL(img);
        reader.onload = (t) => {
          this.picture = t.target?.result;
        };
      },
    });
    const dataComments = {
      animalId: this.animal.id,
    };
    this.commentService.getAllByEventId(dataComments).subscribe((res: any) => {
      this.comments = res.comments;
      let userIds = [];
      this.comments.forEach((comment) => {
        userIds.push(comment.userId);
      });
      const data = {
        userIds,
      };
      this.userService.getByIds(data).subscribe((res: any) => {
        this.userCommenters = res.users;
      });
    });
    this.initLoggedUser();
  }

  user: User;
  animal: Animal;
  picture?: string | ArrayBuffer;
  newComment: string;
  comments: Array<Comment> = new Array();
  userCommenters: Array<User> = new Array();
  commentEditedNow: number = -1;
  commentInput: string;

  startUpdateComment(commentId, commentText) {
    this.commentEditedNow = commentId;
    this.commentInput = commentText;
    this.commentEditedNow = commentId;
  }

  removeComment(commentId) {
    const data = {
      commentId: commentId,
    };

    this.commentService.remove(data).subscribe((res: any) => {
      if (res.message == 'Error') {
        alert('Doslo je do neocekivane greske.');
      } else {
        window.location.reload();
      }
    });
  }

  confirmUpdateComment(commentId) {
    if (this.commentInput == '') {
      alert('Komentar ne sme biti prazan. Pokusajte ponovo.');
      return;
    }

    const data = {
      commentId: commentId,
      text: this.commentInput,
    };
    this.commentService.update(data).subscribe((res: any) => {
      if (res.message == 'Error') {
        alert('Doslo je do neocekivane greske.');
      } else {
        alert('Uspesno ste izmenili komentar.');
        window.location.reload();
      }
    });
  }

  cancelUpdateComment() {
    this.commentEditedNow = -1;
  }

  addComment() {
    const data = {
      userId: this.user.id,
      animalId: this.animal.id,
      text: this.newComment,
    };
    this.commentService.insert(data).subscribe((res: any) => {
      if (res.message == 'Error') {
        alert('Doslo je do neocekivane greske.');
      } else {
        alert('Uspesno ste dodali komentar.');
        window.location.reload();
      }
    });
  }

  initLoggedUser() {
    this.user = null;
    if (localStorage.getItem('loggedUser') != null) {
      this.user = JSON.parse(localStorage.getItem('loggedUser'));

      const data = {
        id: this.user.id,
      };
      this.userService.getById(data).subscribe((res: any) => {
        if (res.message == 'Ok') {
          localStorage.setItem('loggedUser', JSON.stringify(res.user));
          this.user = res.user;
        }
      });
    }
  }
}
