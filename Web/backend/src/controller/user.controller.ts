import * as express from "express";
import User from "../model/user";

export class UserController {
  getByIds = async (req: express.Request, res: express.Response) => {
    const userIds = req.body.userIds;
    let usersReturn = [];
    User.find({ id: { $in: userIds } }, (err: any, users: any) => {
      if (err) {
        res.json({ message: "Error", users: [], errorMessage: err });
        return;
      }
      for (let i = 0; i < users.length; ++i) {
        for (let j = 0; j < userIds.length; ++j) {
          if (users[i].id == userIds[j]) {
            usersReturn.push(users[i]);
          }
        }
      }
      res.json({ message: "Ok", users: usersReturn });
    });
  };

  getAll = (req: express.Request, res: express.Response) => {
    User.find({}, (err: any, users: any) => {
      if (err) res.json({ message: "Error", errorMessage: err, users: [] });
      else res.json({ message: "Ok", users: users });
    });
  };

  getById = (req: express.Request, res: express.Response) => {
    const userId = req.body.id;
    User.findOne({ id: userId }, (err: any, user: any) => {
      if (err) {
        res.json({ message: "Error", errorMessage: "User does not exist." });
      } else {
        res.json({ message: "Ok", user: user });
      }
    });
  };

  getAllPending = (req: express.Request, res: express.Response) => {
    User.find({ status: "pending" }, (err: any, users: any) => {
      if (err) {
        res.json({ message: "Error", errorMessage: "User does not exist." });
      } else {
        res.json({ message: "Ok", users: users });
      }
    });
  };

  getUsernameById = async (req: express.Request, res: express.Response) => {
    const userId = req.body.userId;
    User.findOne({ id: userId }, (err: any, user: any) => {
      if (err) {
        res.json({ message: "Error while fetching username by id." });
      } else {
        res.json({ username: user.username });
      }
    });
  };

  login = (req: express.Request, res: express.Response) => {
    const username = req.body.username;
    const password = req.body.password;
    let errors: Array<String> = [];

    User.findOne(
      { username: username, password: password },
      (err: any, user: any) => {
        if (err || user == null || !user) {
          errors.push("Pogresni podaci.");
          res.json({ errors: errors });
        } else {
          if (user.status == "pending") {
            errors.push("Admin nije odobrio ovaj nalog.");
            res.json({ errors: errors });
            return;
          }

          res.status(200).json({ user: user });
        }
      }
    );
  };

  register = async (req: express.Request, res: express.Response) => {
    const newUsername = req.body.username;
    const newEmail = req.body.email;

    const users = await User.find().sort({ id: -1 }).limit(1);
    let newUserId = 0;
    if (users.length > 0) {
      newUserId = users[0].id + 1;
    }

    let errors: Array<String> = [];

    User.findOne({ username: newUsername }, (err: any, user: any) => {
      if (user) {
        errors.push("Korisnicko ime mora biti jedinstveno.");
        res.json({ errors: errors });
        return;
      }
      User.findOne({ email: newEmail }, (err: any, user: any) => {
        if (user) {
          errors.push("Email adresa mora biti jedinstvena.");
          res.json({ errors: errors });
          return;
        }

        const newUser = new User({
          id: newUserId,
          username: req.body.username,
          password: req.body.password,
          firstname: req.body.firstname,
          lastname: req.body.lastname,
          phone: req.body.phone,
          email: req.body.email,
          notifications: ['Dobrodosli na sajt zooloskog vrta "Pandica"!'],
          type: req.body.type,
          status: req.body.status,
        });

        newUser
          .save()
          .then(() => {
            res.status(200).json(user);
          })
          .catch(() => {
            res.json({ message: "Error" });
          });
      });
    });
  };

  update = async (req: express.Request, res: express.Response) => {
    const userId = parseInt(req.body.userId);
    const newUsername = req.body.username;
    const newEmail = req.body.email;
    const oldUsername = req.body.oldUsername;
    const oldEmail = req.body.oldEmail;

    let errors: Array<String> = [];

    if (newUsername != oldUsername) {
      let user = await User.findOne({ username: newUsername });
      if (user) {
        errors.push("Korisnicko ime mora biti jedinstveno.");
        res.json({ errors: errors });
        return;
      }
    }

    if (newEmail != oldEmail) {
      let user = await User.findOne({ email: newEmail });
      if (user) {
        errors.push("Email adresa mora biti jedinstvena.");
        res.json({ errors: errors });
        return;
      }
    }

    await User.updateOne(
      { id: userId },
      {
        $set: {
          firstname: req.body.firstname,
          lastname: req.body.lastname,
          email: newEmail,
          username: newUsername,
          phone: req.body.phone,
        },
      }
    );

    if (req.body.newPassword != "") {
      User.updateOne(
        { id: userId },
        { $set: { password: req.body.newPassword } }
      );
    }

    User.findOne({ id: userId }, (err: any, user: any) => {
      if (err || user == null || !user) {
        errors.push("Doslo je do greske prilikom azuriranja podataka.");
        res.json({ errors: errors });
      } else {
        res.status(200).json({ user: user });
      }
    });
  };

  accept = (req: express.Request, res: express.Response) => {
    const userId = req.body.userId;
    User.updateOne(
      { id: userId, status: "pending" },
      { status: "accepted" },
      (err: any, resp: any) => {
        if (err) {
          res.json({ message: "Error" });
          return;
        }

        res.json({ message: "Ok" });
        return;
      }
    );
  };

  reject = (req: express.Request, res: express.Response) => {
    let userId = req.body.userId;
    User.updateOne(
      { id: userId, status: "pending" },
      { status: "rejected" },
      (err: any, resp: any) => {
        if (err) {
          res.json({ message: "Error" });
          return;
        }

        res.json({ message: "Ok" });
        return;
      }
    );
  };

  remove = async (req: express.Request, res: express.Response) => {
    let userId = req.body.userId;

    let user = await User.findOne({ id: userId });

    if (!user || user == null) {
      res.json({ message: "Error", errorMessage: "Neocekivana greska." });
      return;
    }

    User.deleteOne({ id: userId }, (err: any, resp: any) => {
      if (err) {
        res.json({ message: "Error", errorMessage: "Neocekivana greska." });
        return;
      }

      res.json({ message: "Ok" });
      return;
    });
  };
}
