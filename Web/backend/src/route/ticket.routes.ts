import express from "express";
import { TicketController } from "../controller/ticket.controller";

const ticketRouter = express.Router();

ticketRouter.route("/getAllPending").get((req, res) => {
  new TicketController().getAllPending(req, res);
});

ticketRouter.route("/insert").post((req, res) => {
  new TicketController().insert(req, res);
});

ticketRouter.route("/accept").post((req, res) => {
  new TicketController().accept(req, res);
});

ticketRouter.route("/reject").post((req, res) => {
  new TicketController().reject(req, res);
});

export default ticketRouter;
