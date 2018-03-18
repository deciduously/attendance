#![feature(plugin)]
#![plugin(rocket_codegen)]

extern crate attendance;
extern crate rocket;

use attendance::db;
use attendance::handler::*;

fn main() {
    rocket::ignite()
        .manage(db::init_pool())
        .mount("/", routes![index, mock, files])
        .launch();
}
