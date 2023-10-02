import * as express from "express";
import fs from "fs";
import Animal from "../model/animal";

const allowedExtensions: Array<String> = ["jpeg", "jpg", "png"];

export class AnimalController {
  getAll = (req: express.Request, res: express.Response) => {
    Animal.find({}, (err: any, animals: any) => {
      if (err) {
        res.json({ message: "Error", errorMessage: "Animals do not exist." });
      } else {
        res.json({ message: "Ok", animals: animals });
      }
    });
  };

  insert = async (req: express.Request, res: express.Response) => {
    const newName = req.body.name;

    const animals = await Animal.find().sort({ id: -1 }).limit(1);
    let newAnimalId = 0;
    if (animals.length > 0) {
      newAnimalId = animals[0].id + 1;
    }

    let errors: Array<String> = [];

    Animal.findOne({ name: newName }, (err: any, animal: any) => {
      if (animal) {
        errors.push("Ime mora biti jedinstveno.");
        res.json({ errors: errors });
        return;
      }

      const pic: Express.Multer.File = req.file;
      let picName = "src/upload/animal_defaultPhoto.jpg";

      if (pic != undefined) {
        const arr = pic.mimetype.split("/");
        const extension: string = arr[arr.length - 1];
        const dest = "src/upload/";
        picName = dest + "animal_" + newAnimalId + "." + extension;
        if (!allowedExtensions.some((e) => e.localeCompare(extension) == 0)) {
          errors.push("Ekstenzija slike mora biti .jpg ili .png.");
          res.json({ errors: errors });
          return;
        }
        fs.writeFileSync(picName, pic.buffer);
      }

      const newAnimal = new Animal({
        id: newAnimalId,
        name: newName,
        description: req.body.description,
        photo: picName,
      });

      newAnimal
        .save()
        .then(() => {
          res.status(200).json(animal);
        })
        .catch(() => {
          res.json({ message: "Error" });
        });
    });
  };

  update = async (req: express.Request, res: express.Response) => {
    const animalId = parseInt(req.body.animalId);
    const newName = req.body.name;
    const newDescription = req.body.description;

    let errors: Array<String> = [];

    await Animal.updateOne(
      { id: animalId },
      {
        $set: {
          name: newName,
          description: newDescription,
        },
      }
    );

    const pic: Express.Multer.File = req.file;

    if (pic != undefined) {
      const arr = pic.mimetype.split("/");
      const extension: string = arr[arr.length - 1];
      const dest = "src/upload/";
      const picName = dest + "animal_" + animalId + "." + extension;
      if (!allowedExtensions.some((e) => e.localeCompare(extension) == 0)) {
        errors.push("Ekstenzija slike mora biti .jpg ili .png.");
        res.json({ errors: errors });
        return;
      }
      fs.writeFileSync(picName, pic.buffer);

      // If new photo uploaded, update it
      await Animal.updateOne({ id: animalId }, { $set: { photo: picName } });
    }

    Animal.findOne({ id: animalId }, (err: any, animal: any) => {
      if (err || animal == null || !animal) {
        errors.push("Doslo je do greske prilikom azuriranja podataka.");
        res.json({ errors: errors });
      } else {
        res.status(200).json({ animal: animal });
      }
    });
  };

  remove = async (req: express.Request, res: express.Response) => {
    const animalId = req.body.animalId;

    const animal = await Animal.findOne({ id: animalId });

    if (!animal || animal == null) {
      res.json({ message: "Error", errorMessage: "Neocekivana greska." });
      return;
    }

    Animal.deleteOne({ id: animalId }, (err: any, resp: any) => {
      if (err) {
        res.json({ message: "Error", errorMessage: "Neocekivana greska." });
        return;
      }

      res.json({ message: "Ok" });
      return;
    });
  };

  getPhoto = (req: express.Request, res: express.Response) => {
    const photoName = req.body.photoName;
    let buffer: Buffer = fs.readFileSync("./" + photoName);
    res.contentType("image/jpeg");
    res.send(buffer);
  };
}
