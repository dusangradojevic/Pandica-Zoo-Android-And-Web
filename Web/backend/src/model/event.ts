import mongoose from "mongoose";

const Schema = mongoose.Schema;

let Event = new Schema({
  id: {
    type: Number,
  },
  name: {
    type: String,
  },
  description: {
    type: String,
  },
  photo: {
    type: String,
  },
  likes: {
    type: Number,
  },
});

export default mongoose.model("Event", Event, "events");
