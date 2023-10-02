import express from "express";
import { UserController } from "../controller/user.controller";

const userRouter = express.Router();

const regexFirstCapital = new RegExp("^[A-Z].*$");
const regexFirstLetter = new RegExp("^[A-Za-z].*$");
const regexCharacterNum = new RegExp(".{8,12}");
const regexDigit = new RegExp("^.*[0-9].*$");
const regexCapitalLetter = new RegExp("^.*[A-Z].*$");
const regexSpecialCharacter = new RegExp("^.*[!@#$%^.&*].*$");

userRouter.route("/getUsernameById").post((req, res) => {
  new UserController().getUsernameById(req, res);
});

userRouter.route("/getById").post((req, res) => {
  new UserController().getById(req, res);
});

userRouter.route("/getAllPending").get((req, res) => {
  new UserController().getAllPending(req, res);
});

userRouter.route("/getByIds").post((req, res) => {
  new UserController().getByIds(req, res);
});

userRouter.route("/getAll").get((req, res) => {
  new UserController().getAll(req, res);
});

userRouter.route("/login").post((req, res) => {
  req.checkBody("username", "Korisnicko ime mora biti popunjeno.").notEmpty();
  req.checkBody("password", "Lozinka mora biti popunjena.").notEmpty();

  req.getValidationResult().then((result) => {
    let errors = [];
    result.array().forEach((result) => {
      errors.push(result.msg);
    });

    if (errors.length > 0) return res.json({ errors: errors });

    new UserController().login(req, res);
  });
});

userRouter.route("/register").post((req, res) => {
  req.checkBody("firstname", "Ime mora biti popunjeno.").notEmpty();
  req.checkBody("lastname", "Prezime mora biti popunjeno.").notEmpty();
  req.checkBody("email", "Email mora biti popunjen.").notEmpty();
  req.checkBody("email", "Email mora biti u ispravnom formatu.").isEmail();
  req.checkBody("username", "Korisnicko ime mora biti popunjeno.").notEmpty();
  req.checkBody("phone", "Broj telefona mora biti popunjen.").notEmpty();
  req.checkBody("password", "Lozinka mora biti popunjena.").notEmpty();
  req.checkBody("confirmPassword", "Morate potvrditi lozinku.").notEmpty();
  req.checkBody("type", "Tip mora biti popunjen.").notEmpty();

  req.getValidationResult().then((result) => {
    let errors = [];
    result.array().forEach((result) => {
      errors.push(result.msg);
    });

    if (req.body.type != "visitor" && req.body.type != "employee")
      errors.push("Tip mora biti u ispravnom formatu.");
    if (!(req.body.password === req.body.confirmPassword))
      errors.push("Lozinke se ne poklapaju.");
    if (!regexFirstCapital.test(req.body.firstname))
      errors.push("Ime mora pocinjati velikim slovom.");
    if (!regexFirstCapital.test(req.body.lastname))
      errors.push("Prezime mora pocinjati velikim slovom.");
    if (!regexFirstLetter.test(req.body.password))
      errors.push("Lozinka mora poceti slovom.");
    if (!regexCharacterNum.test(req.body.password))
      errors.push("Lozinka mora imati minimalno 8 znakova.");
    if (!regexDigit.test(req.body.password))
      errors.push("Lozinka mora sadrzati bar jednu cifru.");
    if (!regexCapitalLetter.test(req.body.password))
      errors.push("Lozinka mora sadrzati bar jedno veliko slovo.");
    if (!regexSpecialCharacter.test(req.body.password))
      errors.push("Lozinka mora sadrzati bar jedan specijalan znak.");

    if (errors.length > 0) return res.json({ errors: errors });

    new UserController().register(req, res);
  });
});

userRouter.route("/update").post((req, res) => {
  req.checkBody("firstname", "Ime mora biti popunjeno.").notEmpty();
  req.checkBody("lastname", "Prezime mora biti popunjeno.").notEmpty();
  req.checkBody("email", "Email mora biti popunjen.").notEmpty();
  req.checkBody("email", "Email mora biti u ispravnom formatu.").isEmail();
  req.checkBody("username", "Korisnicko ime mora biti popunjeno.").notEmpty();
  req.checkBody("phone", "Broj telefona mora biti popunjen.").notEmpty();

  req.getValidationResult().then((result) => {
    let errors = [];
    result.array().forEach((result) => {
      errors.push(result.msg);
    });

    if (
      req.body.confirmOldPassword != "" ||
      req.body.newPassword ||
      req.body.confirmNewPassword != ""
    ) {
      if (!(req.body.oldPassword === req.body.confirmOldPassword))
        errors.push("Stara lozinka nije tacna.");
      if (!(req.body.newPassword === req.body.confirmNewPassword))
        errors.push("Nove lozinke se ne poklapaju.");
      if (!regexFirstCapital.test(req.body.firstname))
        errors.push("Ime mora pocinjati velikim slovom.");
      if (!regexFirstCapital.test(req.body.lastname))
        errors.push("Prezime mora pocinjati velikim slovom.");
      if (!regexFirstLetter.test(req.body.newPassword))
        errors.push("Lozinka mora poceti slovom.");
      if (!regexCharacterNum.test(req.body.newPassword))
        errors.push("Lozinka mora imati minimalno 8 znakova.");
      if (!regexDigit.test(req.body.newPassword))
        errors.push("Lozinka mora sadrzati bar jednu cifru.");
      if (!regexCapitalLetter.test(req.body.newPassword))
        errors.push("Lozinka mora sadrzati bar jedno veliko slovo.");
      if (!regexSpecialCharacter.test(req.body.newPassword))
        errors.push("Lozinka mora sadrzati bar jedan specijalan znak.");
    }

    if (errors.length > 0) return res.json({ errors: errors });

    new UserController().update(req, res);
  });
});

userRouter.route("/remove").post((req, res) => {
  new UserController().remove(req, res);
});

userRouter.route("/accept").post((req, res) => {
  new UserController().accept(req, res);
});

userRouter.route("/reject").post((req, res) => {
  new UserController().reject(req, res);
});

export default userRouter;
