import * as express from "express";
import fs from "fs";
import Event from "../model/event";
import Like from "../model/like";

const allowedExtensions: Array<String> = ["jpeg", "jpg", "png"];

export class EventController {
  getAll = (req: express.Request, res: express.Response) => {
    Event.find({}, (err: any, events: any) => {
      if (err) {
        res.json({ message: "Error", errorMessage: "Events do not exist." });
      } else {
        res.json({ message: "Ok", events: events });
      }
    });
  };

  insert = async (req: express.Request, res: express.Response) => {
    const newName = req.body.name;
    const newDescription = req.body.description;

    const events = await Event.find().sort({ id: -1 }).limit(1);
    let newEventId = 0;
    if (events.length > 0) {
      newEventId = events[0].id + 1;
    }

    let errors: Array<String> = [];

    Event.findOne({ name: newName }, (err: any, event: any) => {
      if (event) {
        errors.push("Ime mora biti jedinstveno.");
        res.json({ errors: errors });
        return;
      }

      const pic: Express.Multer.File = req.file;
      let picName = "src/upload/event_defaultPhoto.jpg";

      if (pic != undefined) {
        const arr = pic.mimetype.split("/");
        const extension: string = arr[arr.length - 1];
        const dest = "src/upload/";
        picName = dest + "event_" + newEventId + "." + extension;
        if (!allowedExtensions.some((e) => e.localeCompare(extension) == 0)) {
          errors.push("Ekstenzija slike mora biti .jpg ili .png.");
          res.json({ errors: errors });
          return;
        }
        fs.writeFileSync(picName, pic.buffer);
      }

      const newEvent = new Event({
        id: newEventId,
        name: newName,
        description: newDescription,
        photo: picName,
        likes: 0,
      });

      newEvent
        .save()
        .then(() => {
          res.status(200).json(event);
        })
        .catch(() => {
          res.json({ message: "Error" });
        });
    });
  };

  update = async (req: express.Request, res: express.Response) => {
    const eventId = parseInt(req.body.eventId);
    const newName = req.body.name;
    const newDescription = req.body.description;

    let errors: Array<String> = [];

    await Event.updateOne(
      { id: eventId },
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
      const picName = dest + "event_" + eventId + "." + extension;
      if (!allowedExtensions.some((e) => e.localeCompare(extension) == 0)) {
        errors.push("Ekstenzija slike mora biti .jpg ili .png.");
        res.json({ errors: errors });
        return;
      }
      fs.writeFileSync(picName, pic.buffer);

      // If new photo uploaded, update it
      await Event.updateOne({ id: eventId }, { $set: { photo: picName } });
    }

    Event.findOne({ id: eventId }, (err: any, event: any) => {
      if (err || event == null || !event) {
        errors.push("Doslo je do greske prilikom azuriranja podataka.");
        res.json({ errors: errors });
      } else {
        res.status(200).json({ event: event });
      }
    });
  };

  remove = async (req: express.Request, res: express.Response) => {
    const eventId = req.body.eventId;
    const event = await Event.findOne({ id: eventId });
    if (!event || event == null) {
      res.json({ message: "Error", errorMessage: "Neocekivana greska." });
      return;
    }

    await Like.deleteMany({ eventId: eventId });

    Event.deleteOne({ id: eventId }, (err: any, resp: any) => {
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

  getLikedEventsFlags = async (req: express.Request, res: express.Response) => {
    const userId = req.body.userId;
    const eventIds = req.body.eventIds;
    let eventFlags = [];
    for (let i = 0; i < eventIds.length; ++i) {
      const like = await Like.findOne({ userId: userId, eventId: eventIds[i] });
      eventFlags.push(like == null ? true : false);
    }
    res.json({ message: "Ok", eventFlags: eventFlags });
  };

  like = async (req: express.Request, res: express.Response) => {
    const userId = req.body.userId;
    const eventId = req.body.eventId;

    const likes = await Like.find().sort({ id: -1 }).limit(1);
    let newLikeId = 0;
    if (likes.length > 0) {
      newLikeId = likes[0].id + 1;
    }

    const event = await Event.findOne({ id: eventId });
    await Event.updateOne(
      { id: eventId },
      { $set: { likes: event.likes + 1 } }
    );

    const newLike = new Like({
      id: newLikeId,
      userId: userId,
      eventId: eventId,
    });

    newLike
      .save()
      .then(() => {
        res.status(200).json({ message: "Ok" });
      })
      .catch(() => {
        res.json({ message: "Error" });
      });
  };

  dislike = async (req: express.Request, res: express.Response) => {
    const userId = req.body.userId;
    const eventId = req.body.eventId;

    const event = await Event.findOne({ id: eventId });
    await Event.updateOne(
      { id: eventId },
      { $set: { likes: event.likes - 1 } }
    );

    Like.deleteOne(
      { userId: userId, eventId: eventId },
      (err: any, resp: any) => {
        if (err) {
          res.json({ message: "Error", errorMessage: "Neocekivana greska." });
        } else {
          res.json({ message: "Ok" });
        }
      }
    );
  };
}
